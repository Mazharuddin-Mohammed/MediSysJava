package com.medisys.desktop.service;

import com.medisys.desktop.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@ExtendWith(org.junit.jupiter.api.extension.ExtensionContext.class)
public class AuthServiceTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("medisys")
            .withUsername("postgres")
            .withPassword("secret");

    private AuthService authService;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(postgres.getJdbcUrl());
        dataSource.setUsername(postgres.getUsername());
        dataSource.setPassword(postgres.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSource);

        // Initialize the database
        jdbcTemplate.execute("CREATE TABLE users (id SERIAL PRIMARY KEY, username VARCHAR(50) NOT NULL UNIQUE, password_hash VARCHAR(255) NOT NULL, role VARCHAR(20) NOT NULL)");
        jdbcTemplate.update("INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)",
                "admin", "$2a$10$7s5g9Xz5z5q8k9t4u8v2wezq5y7u8i9o0p1q2w3e4r5t6y7u8i9o", "ADMIN");

        authService = new AuthService(jdbcTemplate);
    }

    @Test
    public void testAuthenticateSuccess() {
        User user = authService.authenticate("admin", "admin123");
        assertEquals("admin", user.getUsername());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    public void testAuthenticateFailure() {
        assertThrows(RuntimeException.class, () -> authService.authenticate("admin", "wrongpassword"));
    }
}