package com.medisys.desktop.service;

import com.medisys.desktop.model.Doctor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.List;

public class DoctorService {
    private final JdbcTemplate jdbcTemplate;
    private final AuditService auditService;

    public DoctorService(JdbcTemplate jdbcTemplate, AuditService auditService) {
        this.jdbcTemplate = jdbcTemplate;
        this.auditService = auditService;
    }

    public Doctor createDoctor(Doctor doctor, Long userId) {
        String sql = "INSERT INTO doctors (name, specialty, contact_info, department_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getSpecialty());
            ps.setString(3, doctor.getContactInfo());
            ps.setLong(4, doctor.getDepartmentId());
            return ps;
        }, keyHolder);

        Long doctorId = keyHolder.getKey().longValue();
        doctor.setId(doctorId);
        auditService.logAction(userId, "CREATE_DOCTOR", "Doctor", doctorId);
        return doctor;
    }

    public Doctor getDoctor(Long id, Long userId) {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        Doctor doctor = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Doctor d = new Doctor();
            d.setId(rs.getLong("id"));
            d.setName(rs.getString("name"));
            d.setSpecialty(rs.getString("specialty"));
            d.setContactInfo(rs.getString("contact_info"));
            d.setDepartmentId(rs.getLong("department_id"));
            return d;
        });

        auditService.logAction(userId, "VIEW_DOCTOR", "Doctor", id);
        return doctor;
    }

    public List<Doctor> getAllDoctors(Long userId) {
        String sql = "SELECT * FROM doctors";
        List<Doctor> doctors = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Doctor d = new Doctor();
            d.setId(rs.getLong("id"));
            d.setName(rs.getString("name"));
            d.setSpecialty(rs.getString("specialty"));
            d.setContactInfo(rs.getString("contact_info"));
            d.setDepartmentId(rs.getLong("department_id"));
            return d;
        });

        auditService.logAction(userId, "LIST_DOCTORS", "Doctor", null);
        return doctors;
    }

    public void updateDoctor(Doctor doctor, Long userId) {
        String sql = "UPDATE doctors SET name = ?, specialty = ?, contact_info = ?, department_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, doctor.getName(), doctor.getSpecialty(), doctor.getContactInfo(), doctor.getDepartmentId(), doctor.getId());
        auditService.logAction(userId, "UPDATE_DOCTOR", "Doctor", doctor.getId());
    }

    public void deleteDoctor(Long id, Long userId) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        jdbcTemplate.update(sql, id);
        auditService.logAction(userId, "DELETE_DOCTOR", "Doctor", id);
    }
}