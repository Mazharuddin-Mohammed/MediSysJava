package com.medisys.desktop.service;

import com.medisys.desktop.model.User;

/**
 * Simple Authentication Service for MediSys
 */
public class AuthService {

    public AuthService() {
        // Initialize authentication service
    }

    public User authenticate(String username, String password) {
        // Simple authentication logic for demo
        if (username != null && password != null &&
            !username.trim().isEmpty() && !password.trim().isEmpty()) {

            // Create a demo user based on username
            User user = new User();
            user.setUsername(username);
            user.setFirstName(getFirstName(username));
            user.setLastName("User");
            user.setEmail(username + "@medisys.com");
            user.setRole(getRole(username));
            user.setActive(true);

            return user;
        }
        return null;
    }

    private String getFirstName(String username) {
        return switch (username.toLowerCase()) {
            case "admin" -> "System";
            case "doctor" -> "Dr. John";
            case "finance" -> "Finance";
            case "nurse" -> "Nurse";
            default -> username.substring(0, 1).toUpperCase() + username.substring(1);
        };
    }

    private String getRole(String username) {
        return switch (username.toLowerCase()) {
            case "admin" -> "admin";
            case "doctor" -> "doctor";
            case "finance" -> "finance";
            case "nurse" -> "nurse";
            default -> "user";
        };
    }
}