package com.medisys.desktop.service;

import com.medisys.desktop.model.Patient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.List;

public class PatientService {
    private final JdbcTemplate jdbcTemplate;
    private final AuditService auditService;

    public PatientService(JdbcTemplate jdbcTemplate, AuditService auditService) {
        this.jdbcTemplate = jdbcTemplate;
        this.auditService = auditService;
    }

    public Patient createPatient(Patient patient, Long userId) {
        String sql = "INSERT INTO patients (name, date_of_birth, contact_info) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, patient.getName());
            ps.setString(2, patient.getDateOfBirth());
            ps.setString(3, patient.getContactInfo());
            return ps;
        }, keyHolder);

        Long patientId = keyHolder.getKey().longValue();
        patient.setId(patientId);
        auditService.logAction(userId, "CREATE_PATIENT", "Patient", patientId);
        return patient;
    }

    public Patient getPatient(Long id, Long userId) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        Patient patient = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Patient p = new Patient();
            p.setId(rs.getLong("id"));
            p.setName(rs.getString("name"));
            p.setDateOfBirth(rs.getString("date_of_birth"));
            p.setContactInfo(rs.getString("contact_info"));
            return p;
        });

        auditService.logAction(userId, "VIEW_PATIENT", "Patient", id);
        return patient;
    }

    public List<Patient> getAllPatients(Long userId) {
        String sql = "SELECT * FROM patients";
        List<Patient> patients = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Patient p = new Patient();
            p.setId(rs.getLong("id"));
            p.setName(rs.getString("name"));
            p.setDateOfBirth(rs.getString("date_of_birth"));
            p.setContactInfo(rs.getString("contact_info"));
            return p;
        });

        auditService.logAction(userId, "LIST_PATIENTS", "Patient", null);
        return patients;
    }

    public void updatePatient(Patient patient, Long userId) {
        String sql = "UPDATE patients SET name = ?, date_of_birth = ?, contact_info = ? WHERE id = ?";
        jdbcTemplate.update(sql, patient.getName(), patient.getDateOfBirth(), patient.getContactInfo(), patient.getId());
        auditService.logAction(userId, "UPDATE_PATIENT", "Patient", patient.getId());
    }

    public void deletePatient(Long id, Long userId) {
        String sql = "DELETE FROM patients WHERE id = ?";
        jdbcTemplate.update(sql, id);
        auditService.logAction(userId, "DELETE_PATIENT", "Patient", id);
    }
}