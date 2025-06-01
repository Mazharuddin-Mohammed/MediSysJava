package com.medisys.desktop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

public class AuditService {
    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);
    private final JdbcTemplate jdbcTemplate;

    public AuditService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void logAction(Long userId, String action, String entityType, Long entityId) {
        String sql = "INSERT INTO audit_logs (user_id, action, entity_type, entity_id, timestamp) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userId, action, entityType, entityId, LocalDateTime.now());

        // Also log to console/file for debugging
        logger.info("Audit: userId={}, action={}, entityType={}, entityId={}", userId, action, entityType, entityId);
    }
}