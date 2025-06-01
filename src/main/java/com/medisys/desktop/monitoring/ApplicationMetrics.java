package com.medisys.desktop.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ApplicationMetrics {
    
    private final MeterRegistry meterRegistry;
    private final ConcurrentHashMap<String, Counter> counters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Timer> timers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> gauges = new ConcurrentHashMap<>();
    
    public ApplicationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        initializeMetrics();
    }
    
    private void initializeMetrics() {
        // Database operation counters
        getCounter("database.operations.total", "operation", "select");
        getCounter("database.operations.total", "operation", "insert");
        getCounter("database.operations.total", "operation", "update");
        getCounter("database.operations.total", "operation", "delete");
        
        // Error counters
        getCounter("application.errors.total", "type", "database");
        getCounter("application.errors.total", "type", "validation");
        getCounter("application.errors.total", "type", "authentication");
        
        // User activity counters
        getCounter("user.actions.total", "action", "login");
        getCounter("user.actions.total", "action", "logout");
        getCounter("user.actions.total", "action", "patient_create");
        getCounter("user.actions.total", "action", "patient_update");
        getCounter("user.actions.total", "action", "patient_delete");
        
        // Cache metrics
        getCounter("cache.operations.total", "operation", "hit");
        getCounter("cache.operations.total", "operation", "miss");
        
        // Active sessions gauge
        gauges.put("active.sessions", meterRegistry.gauge("application.active.sessions", new AtomicLong(0)));
    }
    
    public void recordDatabaseOperation(String operation, Duration duration) {
        getCounter("database.operations.total", "operation", operation).increment();
        getTimer("database.operation.duration", "operation", operation).record(duration);
    }
    
    public void recordError(String errorType) {
        getCounter("application.errors.total", "type", errorType).increment();
    }
    
    public void recordUserAction(String action) {
        getCounter("user.actions.total", "action", action).increment();
    }
    
    public void recordCacheOperation(String operation) {
        getCounter("cache.operations.total", "operation", operation).increment();
    }
    
    public void updateActiveSessionsCount(long count) {
        AtomicLong gauge = gauges.get("active.sessions");
        if (gauge != null) {
            gauge.set(count);
        }
    }
    
    public Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }
    
    public void recordCustomTimer(Timer.Sample sample, String timerName, String... tags) {
        sample.stop(getTimer(timerName, tags));
    }
    
    private Counter getCounter(String name, String... tags) {
        String key = name + ":" + String.join(":", tags);
        return counters.computeIfAbsent(key, k -> 
            Counter.builder(name)
                .tags(tags)
                .register(meterRegistry)
        );
    }
    
    private Timer getTimer(String name, String... tags) {
        String key = name + ":" + String.join(":", tags);
        return timers.computeIfAbsent(key, k -> 
            Timer.builder(name)
                .tags(tags)
                .register(meterRegistry)
        );
    }
    
    // Health check methods
    public boolean isDatabaseHealthy() {
        // Check if database errors are below threshold
        Counter dbErrors = counters.get("application.errors.total:type:database");
        return dbErrors == null || dbErrors.count() < 10; // Less than 10 errors
    }
    
    public boolean isCacheHealthy() {
        // Check cache hit ratio
        Counter hits = counters.get("cache.operations.total:operation:hit");
        Counter misses = counters.get("cache.operations.total:operation:miss");
        
        if (hits == null || misses == null) return true;
        
        double total = hits.count() + misses.count();
        if (total == 0) return true;
        
        double hitRatio = hits.count() / total;
        return hitRatio > 0.5; // At least 50% hit ratio
    }
    
    public long getActiveSessionsCount() {
        AtomicLong gauge = gauges.get("active.sessions");
        return gauge != null ? gauge.get() : 0;
    }
}
