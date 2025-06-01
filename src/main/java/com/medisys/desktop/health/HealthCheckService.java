package com.medisys.desktop.health;

import com.medisys.desktop.monitoring.ApplicationMetrics;
import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class HealthCheckService implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final ApplicationMetrics metrics;

    public HealthCheckService(JdbcTemplate jdbcTemplate, DataSource dataSource, ApplicationMetrics metrics) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        this.metrics = metrics;
    }

    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();
        
        // Check database health
        boolean dbHealthy = checkDatabaseHealth(details);
        
        // Check cache health
        boolean cacheHealthy = checkCacheHealth(details);
        
        // Check application metrics
        boolean metricsHealthy = checkMetricsHealth(details);
        
        // Check memory usage
        boolean memoryHealthy = checkMemoryHealth(details);
        
        // Overall health status
        boolean overallHealthy = dbHealthy && cacheHealthy && metricsHealthy && memoryHealthy;
        
        Health.Builder healthBuilder = overallHealthy ? Health.up() : Health.down();
        
        return healthBuilder
                .withDetails(details)
                .build();
    }

    private boolean checkDatabaseHealth(Map<String, Object> details) {
        try {
            // Test database connection
            try (Connection connection = dataSource.getConnection()) {
                boolean isValid = connection.isValid(5); // 5 second timeout
                details.put("database.connection", isValid ? "UP" : "DOWN");
                
                if (isValid) {
                    // Test a simple query
                    Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
                    details.put("database.query", result != null && result == 1 ? "UP" : "DOWN");
                    
                    // Check if metrics indicate database health
                    boolean metricsHealthy = metrics.isDatabaseHealthy();
                    details.put("database.metrics", metricsHealthy ? "UP" : "DOWN");
                    
                    return isValid && result != null && result == 1 && metricsHealthy;
                }
            }
        } catch (SQLException e) {
            details.put("database.connection", "DOWN");
            details.put("database.error", e.getMessage());
        } catch (Exception e) {
            details.put("database.query", "DOWN");
            details.put("database.error", e.getMessage());
        }
        return false;
    }

    private boolean checkCacheHealth(Map<String, Object> details) {
        try {
            boolean cacheHealthy = metrics.isCacheHealthy();
            details.put("cache.status", cacheHealthy ? "UP" : "DOWN");
            return cacheHealthy;
        } catch (Exception e) {
            details.put("cache.status", "DOWN");
            details.put("cache.error", e.getMessage());
            return false;
        }
    }

    private boolean checkMetricsHealth(Map<String, Object> details) {
        try {
            long activeSessions = metrics.getActiveSessionsCount();
            details.put("metrics.activeSessions", activeSessions);
            details.put("metrics.status", "UP");
            
            // Consider unhealthy if too many active sessions (potential memory leak)
            boolean healthy = activeSessions < 1000;
            if (!healthy) {
                details.put("metrics.warning", "High number of active sessions detected");
            }
            
            return healthy;
        } catch (Exception e) {
            details.put("metrics.status", "DOWN");
            details.put("metrics.error", e.getMessage());
            return false;
        }
    }

    private boolean checkMemoryHealth(Map<String, Object> details) {
        try {
            Runtime runtime = Runtime.getRuntime();
            long maxMemory = runtime.maxMemory();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            
            double memoryUsagePercentage = (double) usedMemory / maxMemory * 100;
            
            details.put("memory.max", formatBytes(maxMemory));
            details.put("memory.total", formatBytes(totalMemory));
            details.put("memory.used", formatBytes(usedMemory));
            details.put("memory.free", formatBytes(freeMemory));
            details.put("memory.usagePercentage", String.format("%.2f%%", memoryUsagePercentage));
            
            // Consider unhealthy if memory usage is above 90%
            boolean healthy = memoryUsagePercentage < 90.0;
            details.put("memory.status", healthy ? "UP" : "DOWN");
            
            if (!healthy) {
                details.put("memory.warning", "High memory usage detected");
            }
            
            return healthy;
        } catch (Exception e) {
            details.put("memory.status", "DOWN");
            details.put("memory.error", e.getMessage());
            return false;
        }
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    // Additional health check methods
    public Map<String, Object> getDetailedHealthStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // Application uptime
        long uptime = java.lang.management.ManagementFactory.getRuntimeMXBean().getUptime();
        status.put("uptime", formatDuration(uptime));
        
        // Thread information
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        while (rootGroup.getParent() != null) {
            rootGroup = rootGroup.getParent();
        }
        status.put("activeThreads", rootGroup.activeCount());
        
        // System properties
        status.put("javaVersion", System.getProperty("java.version"));
        status.put("osName", System.getProperty("os.name"));
        status.put("osVersion", System.getProperty("os.version"));
        
        return status;
    }

    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return String.format("%dd %dh %dm", days, hours % 24, minutes % 60);
        } else if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes % 60, seconds % 60);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds % 60);
        } else {
            return String.format("%ds", seconds);
        }
    }
}
