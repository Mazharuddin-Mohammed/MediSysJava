package com.medisys.desktop.service;

import com.medisys.desktop.model.Department;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.List;

public class DepartmentService {
    private final JdbcTemplate jdbcTemplate;
    private final AuditService auditService;

    public DepartmentService(JdbcTemplate jdbcTemplate, AuditService auditService) {
        this.jdbcTemplate = jdbcTemplate;
        this.auditService = auditService;
    }

    public Department createDepartment(Department department, Long userId) {
        String sql = "INSERT INTO departments (name, head_of_department) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, department.getName());
            ps.setString(2, department.getHeadOfDepartment());
            return ps;
        }, keyHolder);

        Long deptId = keyHolder.getKey().longValue();
        department.setId(deptId);
        auditService.logAction(userId, "CREATE_DEPARTMENT", "Department", deptId);
        return department;
    }

    public Department getDepartment(Long id, Long userId) {
        String sql = "SELECT * FROM departments WHERE id = ?";
        Department department = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Department d = new Department();
            d.setId(rs.getLong("id"));
            d.setName(rs.getString("name"));
            d.setHeadOfDepartment(rs.getString("head_of_department"));
            return d;
        });

        auditService.logAction(userId, "VIEW_DEPARTMENT", "Department", id);
        return department;
    }

    public List<Department> getAllDepartments(Long userId) {
        String sql = "SELECT * FROM departments";
        List<Department> departments = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Department d = new Department();
            d.setId(rs.getLong("id"));
            d.setName(rs.getString("name"));
            d.setHeadOfDepartment(rs.getString("head_of_department"));
            return d;
        });

        auditService.logAction(userId, "LIST_DEPARTMENTS", "Department", null);
        return departments;
    }

    public void updateDepartment(Department department, Long userId) {
        String sql = "UPDATE departments SET name = ?, head_of_department = ? WHERE id = ?";
        jdbcTemplate.update(sql, department.getName(), department.getHeadOfDepartment(), department.getId());
        auditService.logAction(userId, "UPDATE_DEPARTMENT", "Department", department.getId());
    }

    public void deleteDepartment(Long id, Long userId) {
        String sql = "DELETE FROM departments WHERE id = ?";
        jdbcTemplate.update(sql, id);
        auditService.logAction(userId, "DELETE_DEPARTMENT", "Department", id);
    }
}