package com.medisys.desktop.service;

import com.medisys.desktop.model.Patient;
import com.medisys.desktop.monitoring.ApplicationMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.SimpleCacheManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CachedPatientServiceTest {

    @Mock
    private PatientService patientService;

    @Mock
    private ApplicationMetrics metrics;

    private CachedPatientService cachedPatientService;
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        cacheManager = new ConcurrentMapCacheManager("patients");
        cachedPatientService = new CachedPatientService(patientService, metrics);
    }

    @Test
    public void testGetPatient_CacheMiss() {
        // Arrange
        Long patientId = 1L;
        Long userId = 100L;
        Patient expectedPatient = new Patient(patientId, "John Doe", "1990-01-01", "john@example.com");
        
        when(patientService.getPatient(patientId, userId)).thenReturn(expectedPatient);

        // Act
        Patient result = cachedPatientService.getPatient(patientId, userId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPatient.getName(), result.getName());
        verify(patientService, times(1)).getPatient(patientId, userId);
        verify(metrics, times(1)).recordCacheOperation("miss");
    }

    @Test
    public void testCreatePatient_Success() {
        // Arrange
        Patient newPatient = new Patient(null, "Jane Doe", "1985-05-15", "jane@example.com");
        Patient createdPatient = new Patient(2L, "Jane Doe", "1985-05-15", "jane@example.com");
        Long userId = 100L;
        
        when(patientService.createPatient(newPatient, userId)).thenReturn(createdPatient);

        // Act
        Patient result = cachedPatientService.createPatient(newPatient, userId);

        // Assert
        assertNotNull(result);
        assertEquals(createdPatient.getId(), result.getId());
        verify(patientService, times(1)).createPatient(newPatient, userId);
        verify(metrics, times(1)).recordUserAction("patient_create");
    }

    @Test
    public void testGetAllPatients_Success() {
        // Arrange
        Long userId = 100L;
        List<Patient> expectedPatients = Arrays.asList(
            new Patient(1L, "John Doe", "1990-01-01", "john@example.com"),
            new Patient(2L, "Jane Doe", "1985-05-15", "jane@example.com")
        );
        
        when(patientService.getAllPatients(userId)).thenReturn(expectedPatients);

        // Act
        List<Patient> result = cachedPatientService.getAllPatients(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(patientService, times(1)).getAllPatients(userId);
        verify(metrics, times(1)).recordCacheOperation("miss");
    }

    @Test
    public void testUpdatePatient_Success() {
        // Arrange
        Patient patient = new Patient(1L, "John Updated", "1990-01-01", "john.updated@example.com");
        Long userId = 100L;

        // Act
        cachedPatientService.updatePatient(patient, userId);

        // Assert
        verify(patientService, times(1)).updatePatient(patient, userId);
        verify(metrics, times(1)).recordUserAction("patient_update");
    }

    @Test
    public void testDeletePatient_Success() {
        // Arrange
        Long patientId = 1L;
        Long userId = 100L;

        // Act
        cachedPatientService.deletePatient(patientId, userId);

        // Assert
        verify(patientService, times(1)).deletePatient(patientId, userId);
        verify(metrics, times(1)).recordUserAction("patient_delete");
    }

    @Test
    public void testGetPatient_DatabaseError() {
        // Arrange
        Long patientId = 1L;
        Long userId = 100L;
        
        when(patientService.getPatient(patientId, userId))
            .thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cachedPatientService.getPatient(patientId, userId);
        });
        
        verify(metrics, times(1)).recordError("database");
    }

    @Test
    public void testWarmCache_Success() {
        // Arrange
        Long userId = 100L;
        List<Patient> patients = Arrays.asList(
            new Patient(1L, "John Doe", "1990-01-01", "john@example.com")
        );
        
        when(patientService.getAllPatients(userId)).thenReturn(patients);

        // Act
        cachedPatientService.warmCache(userId);

        // Assert
        verify(patientService, times(1)).getAllPatients(userId);
    }

    @Test
    public void testWarmCache_Error() {
        // Arrange
        Long userId = 100L;
        
        when(patientService.getAllPatients(userId))
            .thenThrow(new RuntimeException("Cache warming failed"));

        // Act
        cachedPatientService.warmCache(userId);

        // Assert
        verify(metrics, times(1)).recordError("cache");
    }
}
