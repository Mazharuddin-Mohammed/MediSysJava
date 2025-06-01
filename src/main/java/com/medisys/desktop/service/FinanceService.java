package com.medisys.desktop.service;

import com.medisys.desktop.model.Finance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.List;

public class FinanceService {
    private final JdbcTemplate jdbcTemplate;
    private final AuditService auditService;

    public FinanceService(JdbcTemplate jdbcTemplate, AuditService auditService) {
        this.jdbcTemplate = jdbcTemplate;
        this.auditService = auditService;
    }

    public Finance createFinance(Finance finance, Long userId) {
        String sql = "INSERT INTO finance (patient_id, amount, status) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, finance.getPatientId());
            ps.setDouble(2, finance.getAmount());
            ps.setString(3, finance.getStatus());
            return ps;
        }, keyHolder);

        Long financeId = keyHolder.getKey().longValue();
        finance.setId(financeId);
        auditService.logAction(userId, "CREATE_FINANCE", "Finance", financeId);
        return finance;
    }

    public Finance getFinance(Long id, Long userId) {
        String sql = "SELECT * FROM finance WHERE id = ?";
        Finance finance = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Finance f = new Finance();
            f.setId(rs.getLong("id"));
            f.setPatientId(rs.getLong("patient_id"));
            f.setAmount(rs.getDouble("amount"));
            f.setStatus(rs.getString("status"));
            return f;
        });

        auditService.logAction(userId, "VIEW_FINANCE", "Finance", id);
        return finance;
    }

    public List<Finance> getAllFinance(Long userId) {
        String sql = "SELECT * FROM finance";
        List<Finance> finances = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Finance f = new Finance();
            f.setId(rs.getLong("id"));
            f.setPatientId(rs.getLong("patient_id"));
            f.setAmount(rs.getDouble("amount"));
            f.setStatus(rs.getString("status"));
            return f;
        });

        auditService.logAction(userId, "LIST_FINANCE", "Finance", null);
        return finances;
    }

    public void updateFinance(Finance finance, Long userId) {
        String sql = "UPDATE finance SET patient_id = ?, amount = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, finance.getPatientId(), finance.getAmount(), finance.getStatus(), finance.getId());
        auditService.logAction(userId, "UPDATE_FINANCE", "Finance", finance.getId());
    }

    public void deleteFinance(Long id, Long userId) {
        String sql = "DELETE FROM finance WHERE id = ?";
        jdbcTemplate.update(sql, id);
        auditService.logAction(userId, "DELETE_FINANCE", "Finance", id);
    }
}