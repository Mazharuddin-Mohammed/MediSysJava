package com.medisys.desktop.performance;

import com.medisys.desktop.model.Patient;
import com.medisys.desktop.service.CachedPatientService;
import com.medisys.desktop.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PerformanceTestSuite {

    @Autowired
    private PatientService patientService;

    @Autowired
    private CachedPatientService cachedPatientService;

    private static final int CONCURRENT_USERS = 50;
    private static final int OPERATIONS_PER_USER = 100;
    private static final Long TEST_USER_ID = 1L;

    @BeforeEach
    void setUp() {
        // Warm up the cache
        cachedPatientService.warmCache(TEST_USER_ID);
    }

    @Test
    void testConcurrentPatientCreation() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_USERS);
        CountDownLatch latch = new CountDownLatch(CONCURRENT_USERS);
        List<Future<PerformanceResult>> futures = new ArrayList<>();

        Instant startTime = Instant.now();

        // Submit concurrent tasks
        for (int i = 0; i < CONCURRENT_USERS; i++) {
            final int userId = i;
            Future<PerformanceResult> future = executor.submit(() -> {
                try {
                    return performPatientCreationTest(userId);
                } finally {
                    latch.countDown();
                }
            });
            futures.add(future);
        }

        // Wait for all tasks to complete
        latch.await(5, TimeUnit.MINUTES);
        Instant endTime = Instant.now();

        // Collect results
        List<PerformanceResult> results = new ArrayList<>();
        for (Future<PerformanceResult> future : futures) {
            try {
                results.add(future.get());
            } catch (ExecutionException e) {
                fail("Performance test failed: " + e.getCause().getMessage());
            }
        }

        executor.shutdown();

        // Analyze results
        analyzePerformanceResults(results, startTime, endTime, "Patient Creation");
    }

    @Test
    void testConcurrentPatientRetrieval() throws InterruptedException {
        // Pre-populate data
        List<Patient> testPatients = createTestPatients(1000);
        
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_USERS);
        CountDownLatch latch = new CountDownLatch(CONCURRENT_USERS);
        List<Future<PerformanceResult>> futures = new ArrayList<>();

        Instant startTime = Instant.now();

        // Submit concurrent retrieval tasks
        for (int i = 0; i < CONCURRENT_USERS; i++) {
            Future<PerformanceResult> future = executor.submit(() -> {
                try {
                    return performPatientRetrievalTest(testPatients);
                } finally {
                    latch.countDown();
                }
            });
            futures.add(future);
        }

        latch.await(3, TimeUnit.MINUTES);
        Instant endTime = Instant.now();

        // Collect and analyze results
        List<PerformanceResult> results = new ArrayList<>();
        for (Future<PerformanceResult> future : futures) {
            try {
                results.add(future.get());
            } catch (ExecutionException e) {
                fail("Performance test failed: " + e.getCause().getMessage());
            }
        }

        executor.shutdown();
        analyzePerformanceResults(results, startTime, endTime, "Patient Retrieval");
    }

    @Test
    void testCachePerformance() {
        List<Long> patientIds = IntStream.range(1, 101)
                .mapToLong(i -> (long) i)
                .boxed()
                .toList();

        // Test without cache (direct service)
        Instant startTime = Instant.now();
        for (int i = 0; i < 1000; i++) {
            for (Long patientId : patientIds) {
                try {
                    patientService.getPatient(patientId, TEST_USER_ID);
                } catch (Exception e) {
                    // Expected for non-existent patients
                }
            }
        }
        Duration withoutCache = Duration.between(startTime, Instant.now());

        // Test with cache
        startTime = Instant.now();
        for (int i = 0; i < 1000; i++) {
            for (Long patientId : patientIds) {
                try {
                    cachedPatientService.getPatient(patientId, TEST_USER_ID);
                } catch (Exception e) {
                    // Expected for non-existent patients
                }
            }
        }
        Duration withCache = Duration.between(startTime, Instant.now());

        System.out.println("Performance Comparison:");
        System.out.println("Without Cache: " + withoutCache.toMillis() + "ms");
        System.out.println("With Cache: " + withCache.toMillis() + "ms");
        System.out.println("Performance Improvement: " + 
                          (100.0 * (withoutCache.toMillis() - withCache.toMillis()) / withoutCache.toMillis()) + "%");

        // Cache should be significantly faster after first run
        assertTrue(withCache.toMillis() < withoutCache.toMillis() * 0.5, 
                  "Cache should provide at least 50% performance improvement");
    }

    @Test
    void testMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        
        // Force garbage collection
        System.gc();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        // Create large dataset
        List<Patient> patients = createTestPatients(10000);
        
        long afterCreationMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterCreationMemory - initialMemory;

        System.out.println("Memory Usage Analysis:");
        System.out.println("Initial Memory: " + formatBytes(initialMemory));
        System.out.println("After Creation: " + formatBytes(afterCreationMemory));
        System.out.println("Memory Used: " + formatBytes(memoryUsed));
        System.out.println("Memory per Patient: " + formatBytes(memoryUsed / patients.size()));

        // Clear references and force GC
        patients.clear();
        patients = null;
        System.gc();
        
        long afterCleanupMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("After Cleanup: " + formatBytes(afterCleanupMemory));

        // Memory should be mostly reclaimed
        assertTrue(afterCleanupMemory < afterCreationMemory * 0.8, 
                  "Memory should be reclaimed after cleanup");
    }

    private PerformanceResult performPatientCreationTest(int userId) {
        List<Long> operationTimes = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;

        for (int i = 0; i < OPERATIONS_PER_USER; i++) {
            Patient patient = new Patient();
            patient.setName("Test Patient " + userId + "_" + i);
            patient.setDateOfBirth("1990-01-01");
            patient.setContactInfo("test" + userId + "_" + i + "@example.com");

            Instant start = Instant.now();
            try {
                cachedPatientService.createPatient(patient, TEST_USER_ID);
                successCount++;
            } catch (Exception e) {
                errorCount++;
            }
            Instant end = Instant.now();
            
            operationTimes.add(Duration.between(start, end).toMillis());
        }

        return new PerformanceResult(operationTimes, successCount, errorCount);
    }

    private PerformanceResult performPatientRetrievalTest(List<Patient> testPatients) {
        List<Long> operationTimes = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;

        for (int i = 0; i < OPERATIONS_PER_USER; i++) {
            Patient randomPatient = testPatients.get(i % testPatients.size());
            
            Instant start = Instant.now();
            try {
                cachedPatientService.getPatient(randomPatient.getId(), TEST_USER_ID);
                successCount++;
            } catch (Exception e) {
                errorCount++;
            }
            Instant end = Instant.now();
            
            operationTimes.add(Duration.between(start, end).toMillis());
        }

        return new PerformanceResult(operationTimes, successCount, errorCount);
    }

    private List<Patient> createTestPatients(int count) {
        List<Patient> patients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Patient patient = new Patient();
            patient.setId((long) i);
            patient.setName("Test Patient " + i);
            patient.setDateOfBirth("1990-01-01");
            patient.setContactInfo("test" + i + "@example.com");
            patients.add(patient);
        }
        return patients;
    }

    private void analyzePerformanceResults(List<PerformanceResult> results, 
                                         Instant startTime, Instant endTime, String testName) {
        long totalOperations = results.stream().mapToLong(r -> r.successCount + r.errorCount).sum();
        long totalSuccesses = results.stream().mapToLong(r -> r.successCount).sum();
        long totalErrors = results.stream().mapToLong(r -> r.errorCount).sum();
        
        List<Long> allTimes = results.stream()
                .flatMap(r -> r.operationTimes.stream())
                .sorted()
                .toList();

        double avgTime = allTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
        long p50 = allTimes.get((int) (allTimes.size() * 0.5));
        long p95 = allTimes.get((int) (allTimes.size() * 0.95));
        long p99 = allTimes.get((int) (allTimes.size() * 0.99));
        long maxTime = allTimes.get(allTimes.size() - 1);

        Duration totalDuration = Duration.between(startTime, endTime);
        double throughput = (double) totalOperations / totalDuration.toSeconds();

        System.out.println("\n" + testName + " Performance Results:");
        System.out.println("=====================================");
        System.out.println("Total Operations: " + totalOperations);
        System.out.println("Successful Operations: " + totalSuccesses);
        System.out.println("Failed Operations: " + totalErrors);
        System.out.println("Success Rate: " + String.format("%.2f%%", (100.0 * totalSuccesses / totalOperations)));
        System.out.println("Total Duration: " + totalDuration.toSeconds() + "s");
        System.out.println("Throughput: " + String.format("%.2f ops/sec", throughput));
        System.out.println("Average Response Time: " + String.format("%.2f ms", avgTime));
        System.out.println("50th Percentile: " + p50 + "ms");
        System.out.println("95th Percentile: " + p95 + "ms");
        System.out.println("99th Percentile: " + p99 + "ms");
        System.out.println("Max Response Time: " + maxTime + "ms");

        // Performance assertions
        assertTrue(totalSuccesses > totalErrors, "Success rate should be higher than error rate");
        assertTrue(p95 < 1000, "95th percentile should be under 1 second");
        assertTrue(throughput > 10, "Throughput should be at least 10 ops/sec");
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    private static class PerformanceResult {
        final List<Long> operationTimes;
        final int successCount;
        final int errorCount;

        PerformanceResult(List<Long> operationTimes, int successCount, int errorCount) {
            this.operationTimes = operationTimes;
            this.successCount = successCount;
            this.errorCount = errorCount;
        }
    }
}
