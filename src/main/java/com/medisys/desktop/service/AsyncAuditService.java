package com.medisys.desktop.service;

import com.medisys.desktop.model.AuditLog;
import com.medisys.desktop.monitoring.ApplicationMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncAuditService {
    
    private static final Logger logger = LoggerFactory.getLogger(AsyncAuditService.class);
    
    private final AuditService auditService;
    private final ApplicationMetrics metrics;
    
    public AsyncAuditService(AuditService auditService, ApplicationMetrics metrics) {
        this.auditService = auditService;
        this.metrics = metrics;
    }
    
    @Async("auditExecutor")
    public CompletableFuture<Void> logActionAsync(Long userId, String action, String entityType, Long entityId) {
        try {
            auditService.logAction(userId, action, entityType, entityId);
            logger.debug("Async audit log created: userId={}, action={}, entityType={}, entityId={}", 
                        userId, action, entityType, entityId);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            logger.error("Failed to create async audit log", e);
            metrics.recordError("audit");
            return CompletableFuture.failedFuture(e);
        }
    }
    
    @Async("auditExecutor")
    public CompletableFuture<Void> logAuditAsync(AuditLog auditLog) {
        try {
            auditService.logAction(auditLog.getUserId(), auditLog.getAction(), 
                                 auditLog.getEntityType(), auditLog.getEntityId());
            logger.debug("Async audit log created from AuditLog object: {}", auditLog);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            logger.error("Failed to create async audit log from object", e);
            metrics.recordError("audit");
            return CompletableFuture.failedFuture(e);
        }
    }
    
    @Async("auditExecutor")
    public CompletableFuture<Void> logBulkActionsAsync(Long userId, String baseAction, String entityType, int count) {
        try {
            for (int i = 0; i < count; i++) {
                auditService.logAction(userId, baseAction + "_BULK_" + i, entityType, null);
            }
            logger.debug("Bulk audit logs created: userId={}, action={}, count={}", userId, baseAction, count);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            logger.error("Failed to create bulk audit logs", e);
            metrics.recordError("audit");
            return CompletableFuture.failedFuture(e);
        }
    }
    
    @Async("auditExecutor")
    public CompletableFuture<Void> logUserSessionAsync(Long userId, String sessionAction, String sessionId) {
        try {
            AuditLog sessionLog = new AuditLog();
            sessionLog.setUserId(userId);
            sessionLog.setAction("SESSION_" + sessionAction);
            sessionLog.setEntityType("USER_SESSION");
            sessionLog.setEntityId(userId);
            sessionLog.setTimestamp(LocalDateTime.now());
            
            auditService.logAction(sessionLog.getUserId(), sessionLog.getAction(), 
                                 sessionLog.getEntityType(), sessionLog.getEntityId());
            
            logger.info("User session audit logged: userId={}, action={}, sessionId={}", 
                       userId, sessionAction, sessionId);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            logger.error("Failed to log user session audit", e);
            metrics.recordError("audit");
            return CompletableFuture.failedFuture(e);
        }
    }
    
    @Async("auditExecutor")
    public CompletableFuture<Void> logSecurityEventAsync(Long userId, String securityEvent, String details) {
        try {
            auditService.logAction(userId, "SECURITY_" + securityEvent, "SECURITY_EVENT", userId);
            logger.warn("Security event logged: userId={}, event={}, details={}", userId, securityEvent, details);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            logger.error("Failed to log security event", e);
            metrics.recordError("security");
            return CompletableFuture.failedFuture(e);
        }
    }
}
