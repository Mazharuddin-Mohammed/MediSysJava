package com.medisys.desktop.security;

import com.medisys.desktop.model.User;
import com.medisys.desktop.monitoring.ApplicationMetrics;
import com.medisys.desktop.service.AsyncAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {

    @Mock
    private ApplicationMetrics metrics;

    @Mock
    private AsyncAuditService asyncAuditService;

    private SecurityService securityService;

    @BeforeEach
    public void setUp() {
        securityService = new SecurityService(metrics, asyncAuditService);
    }

    @Test
    public void testRecordLoginAttempt_Successful() {
        // Arrange
        String username = "testuser";

        // Act
        securityService.recordLoginAttempt(username, true);

        // Assert
        assertFalse(securityService.isAccountLocked(username));
        verify(metrics, times(1)).recordUserAction("login");
    }

    @Test
    public void testRecordLoginAttempt_Failed() {
        // Arrange
        String username = "testuser";

        // Act
        securityService.recordLoginAttempt(username, false);

        // Assert
        assertFalse(securityService.isAccountLocked(username));
        verify(asyncAuditService, times(1)).logSecurityEventAsync(eq(null), eq("LOGIN_FAILED"), anyString());
    }

    @Test
    public void testAccountLockout_AfterMaxAttempts() {
        // Arrange
        String username = "testuser";

        // Act - Simulate 5 failed login attempts
        for (int i = 0; i < 5; i++) {
            securityService.recordLoginAttempt(username, false);
        }

        // Assert
        assertTrue(securityService.isAccountLocked(username));
        verify(asyncAuditService, times(1)).logSecurityEventAsync(eq(null), eq("ACCOUNT_LOCKED"), anyString());
        verify(metrics, times(1)).recordError("authentication");
    }

    @Test
    public void testCreateSession_Success() {
        // Arrange
        User user = new User(1L, "testuser", "hashedpassword", "admin");

        // Act
        String sessionId = securityService.createSession(user);

        // Assert
        assertNotNull(sessionId);
        assertTrue(sessionId.length() > 0);
        verify(asyncAuditService, times(1)).logUserSessionAsync(eq(user.getId()), eq("LOGIN"), eq(sessionId));
        verify(metrics, times(1)).updateActiveSessionsCount(anyLong());
    }

    @Test
    public void testInvalidateSession_Success() {
        // Arrange
        User user = new User(1L, "testuser", "hashedpassword", "admin");
        String sessionId = securityService.createSession(user);

        // Act
        securityService.invalidateSession(user.getId());

        // Assert
        verify(asyncAuditService, times(1)).logUserSessionAsync(eq(user.getId()), eq("LOGOUT"), eq(sessionId));
        verify(metrics, times(2)).updateActiveSessionsCount(anyLong()); // Once for create, once for invalidate
    }

    @Test
    public void testIsSessionValid_ValidSession() {
        // Arrange
        User user = new User(1L, "testuser", "hashedpassword", "admin");
        String sessionId = securityService.createSession(user);

        // Act
        boolean isValid = securityService.isSessionValid(sessionId);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testIsSessionValid_InvalidSession() {
        // Arrange
        String invalidSessionId = "invalid-session-id";

        // Act
        boolean isValid = securityService.isSessionValid(invalidSessionId);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testUpdateSessionActivity() {
        // Arrange
        User user = new User(1L, "testuser", "hashedpassword", "admin");
        String sessionId = securityService.createSession(user);

        // Act
        securityService.updateSessionActivity(sessionId);

        // Assert
        assertTrue(securityService.isSessionValid(sessionId));
    }

    @Test
    public void testEncryptDecryptSensitiveData() {
        // Arrange
        String originalData = "sensitive information";

        // Act
        String encryptedData = securityService.encryptSensitiveData(originalData);
        String decryptedData = securityService.decryptSensitiveData(encryptedData);

        // Assert
        assertNotEquals(originalData, encryptedData);
        assertEquals(originalData, decryptedData);
    }

    @Test
    public void testIsValidInput_Username() {
        // Test valid usernames
        assertTrue(securityService.isValidInput("testuser", "username"));
        assertTrue(securityService.isValidInput("test_user123", "username"));
        
        // Test invalid usernames
        assertFalse(securityService.isValidInput("te", "username")); // Too short
        assertFalse(securityService.isValidInput("test@user", "username")); // Invalid character
        assertFalse(securityService.isValidInput("", "username")); // Empty
        assertFalse(securityService.isValidInput(null, "username")); // Null
    }

    @Test
    public void testIsValidInput_Email() {
        // Test valid emails
        assertTrue(securityService.isValidInput("test@example.com", "email"));
        assertTrue(securityService.isValidInput("user.name+tag@domain.co.uk", "email"));
        
        // Test invalid emails
        assertFalse(securityService.isValidInput("invalid-email", "email"));
        assertFalse(securityService.isValidInput("@example.com", "email"));
        assertFalse(securityService.isValidInput("test@", "email"));
    }

    @Test
    public void testSanitizeInput() {
        // Test input sanitization
        assertEquals("Hello World", securityService.sanitizeInput("Hello World"));
        assertEquals("Hello World", securityService.sanitizeInput("Hello<script>World"));
        assertEquals("Hello World", securityService.sanitizeInput("Hello\"World'"));
        assertEquals("", securityService.sanitizeInput("   "));
        assertNull(securityService.sanitizeInput(null));
    }

    @Test
    public void testGetSecurityMetrics() {
        // Arrange
        User user = new User(1L, "testuser", "hashedpassword", "admin");
        securityService.createSession(user);

        // Act
        var metrics = securityService.getSecurityMetrics();

        // Assert
        assertNotNull(metrics);
        assertTrue(metrics.containsKey("activeSessionsCount"));
        assertTrue(metrics.containsKey("lockedAccountsCount"));
        assertTrue(metrics.containsKey("totalLoginAttempts"));
        assertEquals(1L, metrics.get("activeSessionsCount"));
    }
}
