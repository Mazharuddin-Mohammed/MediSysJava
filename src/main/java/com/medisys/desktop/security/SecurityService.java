package com.medisys.desktop.security;

import com.medisys.desktop.model.User;
import com.medisys.desktop.monitoring.ApplicationMetrics;
import com.medisys.desktop.service.AsyncAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SecurityService {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 15;
    private static final String ENCRYPTION_ALGORITHM = "AES";
    
    private final ApplicationMetrics metrics;
    private final AsyncAuditService asyncAuditService;
    private final SecretKey encryptionKey;
    
    // Rate limiting and security tracking
    private final Map<String, AtomicInteger> loginAttempts = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lockoutTimes = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lastActivityTimes = new ConcurrentHashMap<>();
    private final Map<Long, String> activeSessions = new ConcurrentHashMap<>();
    
    public SecurityService(ApplicationMetrics metrics, AsyncAuditService asyncAuditService) {
        this.metrics = metrics;
        this.asyncAuditService = asyncAuditService;
        this.encryptionKey = generateEncryptionKey();
    }
    
    private SecretKey generateEncryptionKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
            keyGenerator.init(256);
            return keyGenerator.generateKey();
        } catch (Exception e) {
            logger.error("Failed to generate encryption key", e);
            // Fallback to a default key (not recommended for production)
            byte[] key = "MySecretKey12345".getBytes();
            return new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
        }
    }
    
    public boolean isAccountLocked(String username) {
        LocalDateTime lockoutTime = lockoutTimes.get(username);
        if (lockoutTime == null) {
            return false;
        }
        
        LocalDateTime unlockTime = lockoutTime.plus(LOCKOUT_DURATION_MINUTES, ChronoUnit.MINUTES);
        if (LocalDateTime.now().isAfter(unlockTime)) {
            // Unlock the account
            lockoutTimes.remove(username);
            loginAttempts.remove(username);
            return false;
        }
        
        return true;
    }
    
    public void recordLoginAttempt(String username, boolean successful) {
        if (successful) {
            // Reset failed attempts on successful login
            loginAttempts.remove(username);
            lockoutTimes.remove(username);
            metrics.recordUserAction("login");
        } else {
            // Increment failed attempts
            AtomicInteger attempts = loginAttempts.computeIfAbsent(username, k -> new AtomicInteger(0));
            int currentAttempts = attempts.incrementAndGet();
            
            if (currentAttempts >= MAX_LOGIN_ATTEMPTS) {
                lockoutTimes.put(username, LocalDateTime.now());
                asyncAuditService.logSecurityEventAsync(null, "ACCOUNT_LOCKED", 
                    "Account locked due to " + currentAttempts + " failed login attempts: " + username);
                metrics.recordError("authentication");
            }
            
            asyncAuditService.logSecurityEventAsync(null, "LOGIN_FAILED", 
                "Failed login attempt for username: " + username);
        }
    }
    
    public String createSession(User user) {
        String sessionId = generateSessionId();
        activeSessions.put(user.getId(), sessionId);
        lastActivityTimes.put(sessionId, LocalDateTime.now());
        
        asyncAuditService.logUserSessionAsync(user.getId(), "LOGIN", sessionId);
        metrics.updateActiveSessionsCount(activeSessions.size());
        
        return sessionId;
    }
    
    public void invalidateSession(Long userId) {
        String sessionId = activeSessions.remove(userId);
        if (sessionId != null) {
            lastActivityTimes.remove(sessionId);
            asyncAuditService.logUserSessionAsync(userId, "LOGOUT", sessionId);
        }
        metrics.updateActiveSessionsCount(activeSessions.size());
    }
    
    public boolean isSessionValid(String sessionId) {
        LocalDateTime lastActivity = lastActivityTimes.get(sessionId);
        if (lastActivity == null) {
            return false;
        }
        
        // Session expires after 30 minutes of inactivity
        LocalDateTime expiryTime = lastActivity.plus(30, ChronoUnit.MINUTES);
        return LocalDateTime.now().isBefore(expiryTime);
    }
    
    public void updateSessionActivity(String sessionId) {
        if (sessionId != null && lastActivityTimes.containsKey(sessionId)) {
            lastActivityTimes.put(sessionId, LocalDateTime.now());
        }
    }
    
    private String generateSessionId() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    
    // Field-level encryption for sensitive data
    public String encryptSensitiveData(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            logger.error("Failed to encrypt sensitive data", e);
            metrics.recordError("encryption");
            return data; // Return original data if encryption fails
        }
    }
    
    public String decryptSensitiveData(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, encryptionKey);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData);
        } catch (Exception e) {
            logger.error("Failed to decrypt sensitive data", e);
            metrics.recordError("decryption");
            return encryptedData; // Return encrypted data if decryption fails
        }
    }
    
    // Input validation and sanitization
    public boolean isValidInput(String input, String type) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        switch (type.toLowerCase()) {
            case "username":
                return input.matches("^[a-zA-Z0-9_]{3,50}$");
            case "email":
                return input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
            case "name":
                return input.matches("^[a-zA-Z\\s]{1,100}$");
            case "phone":
                return input.matches("^[+]?[0-9\\s\\-()]{10,15}$");
            default:
                return input.length() <= 255; // Basic length check
        }
    }
    
    public String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        // Remove potentially dangerous characters
        return input.replaceAll("[<>\"'&]", "").trim();
    }
    
    // Get security metrics
    public Map<String, Object> getSecurityMetrics() {
        Map<String, Object> securityMetrics = new ConcurrentHashMap<>();
        securityMetrics.put("activeSessionsCount", activeSessions.size());
        securityMetrics.put("lockedAccountsCount", lockoutTimes.size());
        securityMetrics.put("totalLoginAttempts", loginAttempts.values().stream().mapToInt(AtomicInteger::get).sum());
        return securityMetrics;
    }
}
