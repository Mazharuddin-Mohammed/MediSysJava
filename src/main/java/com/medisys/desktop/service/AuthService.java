package com.medisys.desktop.service;

/**
 * Simple Authentication Service for MediSys
 */
public class AuthService {

    public AuthService() {
        // Initialize authentication service
    }

    public boolean authenticate(String username, String password) {
        // Simple authentication logic for demo
        return username != null && password != null &&
               !username.trim().isEmpty() && !password.trim().isEmpty();
    }
}