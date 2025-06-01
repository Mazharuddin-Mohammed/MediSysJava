package com.medisys.desktop.service;

import com.medisys.desktop.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthService {
    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> {
            User u = new User();
            u.setId(rs.getLong("id"));
            u.setUsername(rs.getString("username"));
            u.setPasswordHash(rs.getString("password_hash"));
            u.setRole(rs.getString("role"));
            return u;
        });

        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        return user;
    }
}