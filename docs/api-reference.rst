API Reference
=============

This section provides detailed information about the MediSys service interfaces and APIs.

Service Interfaces
------------------

Patient Service
~~~~~~~~~~~~~~~

.. code-block:: java

    public interface PatientService {
        Optional<Patient> findById(Long id);
        List<Patient> findAll();
        Patient save(Patient patient);
        void deleteById(Long id);
        List<Patient> searchByName(String name);
    }

Doctor Service
~~~~~~~~~~~~~~

.. code-block:: java

    public interface DoctorService {
        Optional<Doctor> findById(Long id);
        List<Doctor> findAll();
        Doctor save(Doctor doctor);
        void deleteById(Long id);
        List<Doctor> findBySpecialization(String specialization);
    }

Appointment Service
~~~~~~~~~~~~~~~~~~~

.. code-block:: java

    public interface AppointmentService {
        Optional<Appointment> findById(Long id);
        List<Appointment> findAll();
        Appointment save(Appointment appointment);
        void deleteById(Long id);
        List<Appointment> findByDateRange(LocalDate start, LocalDate end);
    }

Data Models
-----------

Patient Entity
~~~~~~~~~~~~~~

.. code-block:: java

    @Entity
    @Table(name = "patients")
    public class Patient {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @Column(nullable = false)
        private String name;
        
        private String email;
        private String phone;
        private LocalDate dateOfBirth;
        private Gender gender;
        
        // Getters and setters...
    }

Doctor Entity
~~~~~~~~~~~~~

.. code-block:: java

    @Entity
    @Table(name = "doctors")
    public class Doctor {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @Column(nullable = false)
        private String name;
        
        private String email;
        private String phone;
        private String specialization;
        private String department;
        
        // Getters and setters...
    }
