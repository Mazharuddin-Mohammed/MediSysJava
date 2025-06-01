package com.medisys.desktop.service;

import com.medisys.desktop.model.Patient;
import com.medisys.desktop.monitoring.ApplicationMetrics;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class CachedPatientService {
    
    private final PatientService patientService;
    private final ApplicationMetrics metrics;
    
    public CachedPatientService(PatientService patientService, ApplicationMetrics metrics) {
        this.patientService = patientService;
        this.metrics = metrics;
    }
    
    @Cacheable(value = "patients", key = "#id")
    public Patient getPatient(Long id, Long userId) {
        Instant start = Instant.now();
        try {
            Patient patient = patientService.getPatient(id, userId);
            metrics.recordCacheOperation("miss");
            metrics.recordDatabaseOperation("select", Duration.between(start, Instant.now()));
            return patient;
        } catch (Exception e) {
            metrics.recordError("database");
            throw e;
        }
    }
    
    @Cacheable(value = "patients", key = "'all-' + #userId")
    public List<Patient> getAllPatients(Long userId) {
        Instant start = Instant.now();
        try {
            List<Patient> patients = patientService.getAllPatients(userId);
            metrics.recordCacheOperation("miss");
            metrics.recordDatabaseOperation("select", Duration.between(start, Instant.now()));
            return patients;
        } catch (Exception e) {
            metrics.recordError("database");
            throw e;
        }
    }
    
    @CachePut(value = "patients", key = "#result.id")
    @CacheEvict(value = "patients", key = "'all-' + #userId")
    public Patient createPatient(Patient patient, Long userId) {
        Instant start = Instant.now();
        try {
            Patient createdPatient = patientService.createPatient(patient, userId);
            metrics.recordDatabaseOperation("insert", Duration.between(start, Instant.now()));
            metrics.recordUserAction("patient_create");
            return createdPatient;
        } catch (Exception e) {
            metrics.recordError("database");
            throw e;
        }
    }
    
    @CachePut(value = "patients", key = "#patient.id")
    @CacheEvict(value = "patients", key = "'all-' + #userId")
    public void updatePatient(Patient patient, Long userId) {
        Instant start = Instant.now();
        try {
            patientService.updatePatient(patient, userId);
            metrics.recordDatabaseOperation("update", Duration.between(start, Instant.now()));
            metrics.recordUserAction("patient_update");
        } catch (Exception e) {
            metrics.recordError("database");
            throw e;
        }
    }
    
    @CacheEvict(value = "patients", key = "#id")
    @CacheEvict(value = "patients", key = "'all-' + #userId")
    public void deletePatient(Long id, Long userId) {
        Instant start = Instant.now();
        try {
            patientService.deletePatient(id, userId);
            metrics.recordDatabaseOperation("delete", Duration.between(start, Instant.now()));
            metrics.recordUserAction("patient_delete");
        } catch (Exception e) {
            metrics.recordError("database");
            throw e;
        }
    }
    
    // Cache warming method
    public void warmCache(Long userId) {
        try {
            getAllPatients(userId);
        } catch (Exception e) {
            metrics.recordError("cache");
        }
    }
    
    // Cache invalidation for specific user
    @CacheEvict(value = "patients", key = "'all-' + #userId")
    public void invalidateUserCache(Long userId) {
        // This method just triggers cache eviction
    }
    
    // Clear all patient caches
    @CacheEvict(value = "patients", allEntries = true)
    public void clearAllCache() {
        // This method clears all patient cache entries
    }
}
