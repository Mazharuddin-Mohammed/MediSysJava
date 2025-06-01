package com.medisys.desktop.service;

import com.medisys.desktop.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
public class PatientServiceTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("medisys")
            .withUsername("postgres")
            .withPassword("secret");

    private PatientService patientService;
    private JdbcTemplate jdbcTemplate;
    private AuditService auditService;

    @BeforeEach
    public void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(postgres.getJdbcUrl());
        dataSource.setUsername(postgres.getUsername());
        dataSource.setPassword(postgres.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSource);

        // Initialize the database
        jdbcTemplate.execute("CREATE TABLE patients (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, date_of_birth VARCHAR(10), contact_info VARCHAR(255))");
        jdbcTemplate.execute("CREATE TABLE audit_logs (id SERIAL PRIMARY KEY, user_id BIGINT, action VARCHAR(100) NOT NULL, entity_type VARCHAR(50), entity_id BIGINT, timestamp TIMESTAMP NOT NULL)");
        jdbcTemplate.update("INSERT INTO patients (id, name, date_of_birth, contact_info) VALUES (?, ?, ?, ?)",
                1L, "John Doe", "1990-01-01", "john.doe@example.com");

        auditService = new AuditService(jdbcTemplate);
        patientService = new PatientService(jdbcTemplate, auditService);
    }

    @Test
    public void testGetPatient() {
        Patient patient = patientService.getPatient(1L, 1L);
        assertNotNull(patient);
        assertEquals("John Doe", patient.getName());
        assertEquals("1990-01-01", patient.getDateOfBirth());
        assertEquals("john.doe@example.com", patient.getContactInfo());

        // Check audit log
        Integer auditCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM audit_logs WHERE action = 'VIEW_PATIENT'", Integer.class);
        assertEquals(1, auditCount);
    }
}