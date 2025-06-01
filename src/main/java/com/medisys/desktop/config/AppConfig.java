package com.medisys.desktop.config;

import com.medisys.desktop.controller.LoginController;
import com.medisys.desktop.controller.PatientController;
import com.medisys.desktop.service.AuditService;
import com.medisys.desktop.service.AuthService;
import com.medisys.desktop.service.PatientService;
// import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/medisys?currentSchema=public");
        dataSource.setUsername("postgres");
        dataSource.setPassword("secret");

        // Connection pool settings
        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(2);
        dataSource.setConnectionTimeout(30000);
        dataSource.setIdleTimeout(600000);
        dataSource.setMaxLifetime(1800000);
        dataSource.setLeakDetectionThreshold(60000);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // Flyway disabled due to compatibility issues with PostgreSQL 16.9
    // @Bean
    // public Flyway flyway(DataSource dataSource) {
    //     return Flyway.configure()
    //             .dataSource(dataSource)
    //             .locations("classpath:db/migration")
    //             .load();
    // }

    @Bean
    public AuthService authService(JdbcTemplate jdbcTemplate) {
        return new AuthService(jdbcTemplate);
    }

    @Bean
    public PatientService patientService(JdbcTemplate jdbcTemplate, AuditService auditService) {
        return new PatientService(jdbcTemplate, auditService);
    }

    @Bean
    public AuditService auditService(JdbcTemplate jdbcTemplate) {
        return new AuditService(jdbcTemplate);
    }

    @Bean
    public LoginController loginController(AuthService authService, ApplicationContext applicationContext) {
        return new LoginController(authService, applicationContext);
    }

    @Bean
    public PatientController patientController(PatientService patientService) {
        return new PatientController(patientService);
    }
}