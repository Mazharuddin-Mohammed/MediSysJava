-- MediSys Test Database Setup Script
-- This script creates the necessary tables and test data for the application

-- Create database (run this as postgres user)
-- CREATE DATABASE medisys;
-- CREATE USER medisys_user WITH PASSWORD 'medisys_password';
-- GRANT ALL PRIVILEGES ON DATABASE medisys TO medisys_user;

-- Connect to medisys database and run the following:

-- Users table with enhanced fields
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    address TEXT,
    date_of_birth DATE,
    gender VARCHAR(10),
    profile_photo_path VARCHAR(500),
    active BOOLEAN DEFAULT true,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    created_by INTEGER REFERENCES users(id),
    department_id INTEGER
);

-- Patients table with enhanced fields
CREATE TABLE IF NOT EXISTS patients (
    id SERIAL PRIMARY KEY,
    patient_id VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10),
    blood_group VARCHAR(5),
    email VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    city VARCHAR(50),
    state VARCHAR(50),
    zip_code VARCHAR(10),
    country VARCHAR(50) DEFAULT 'India',
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relation VARCHAR(50),
    insurance_provider VARCHAR(100),
    insurance_policy_number VARCHAR(50),
    insurance_group_number VARCHAR(50),
    profile_photo_path VARCHAR(500),
    medical_history TEXT,
    allergies TEXT,
    current_medications TEXT,
    active BOOLEAN DEFAULT true,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER REFERENCES users(id)
);

-- Doctors table with enhanced fields
CREATE TABLE IF NOT EXISTS doctors (
    id SERIAL PRIMARY KEY,
    doctor_id VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    specialization VARCHAR(100),
    license_number VARCHAR(50) UNIQUE,
    email VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    date_of_birth DATE,
    gender VARCHAR(10),
    qualification VARCHAR(200),
    experience_years INTEGER,
    consultation_fee DECIMAL(10,2),
    profile_photo_path VARCHAR(500),
    bio TEXT,
    languages_spoken VARCHAR(200),
    available_days VARCHAR(50),
    available_time_start TIME,
    available_time_end TIME,
    department_id INTEGER REFERENCES departments(id),
    user_id INTEGER REFERENCES users(id),
    active BOOLEAN DEFAULT true,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER REFERENCES users(id)
);

-- Departments table with enhanced fields
CREATE TABLE IF NOT EXISTS departments (
    id SERIAL PRIMARY KEY,
    department_code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    head_doctor_id INTEGER,
    location VARCHAR(100),
    floor_number INTEGER,
    phone VARCHAR(20),
    email VARCHAR(100),
    budget DECIMAL(15,2),
    staff_count INTEGER DEFAULT 0,
    bed_count INTEGER DEFAULT 0,
    active BOOLEAN DEFAULT true,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER REFERENCES users(id)
);

-- File uploads table for managing all uploaded files
CREATE TABLE IF NOT EXISTS file_uploads (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT,
    file_type VARCHAR(50),
    mime_type VARCHAR(100),
    entity_type VARCHAR(50) NOT NULL, -- 'user', 'patient', 'doctor', 'document'
    entity_id INTEGER NOT NULL,
    upload_purpose VARCHAR(50), -- 'profile_photo', 'document', 'report'
    uploaded_by INTEGER REFERENCES users(id),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Appointments table
CREATE TABLE IF NOT EXISTS appointments (
    id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL,
    doctor_id INTEGER NOT NULL,
    appointment_date TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'scheduled',
    notes TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

-- Medical records table
CREATE TABLE IF NOT EXISTS medical_records (
    id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL,
    doctor_id INTEGER NOT NULL,
    visit_date TIMESTAMP NOT NULL,
    diagnosis TEXT,
    treatment TEXT,
    medications TEXT,
    notes TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

-- Billing table
CREATE TABLE IF NOT EXISTS billing (
    id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL,
    appointment_id INTEGER,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    billing_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_date TIMESTAMP,
    payment_method VARCHAR(50),
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

-- Audit log table
CREATE TABLE IF NOT EXISTS audit_log (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    action VARCHAR(100) NOT NULL,
    table_name VARCHAR(50),
    record_id INTEGER,
    old_values JSONB,
    new_values JSONB,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address INET,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert default admin user (password: admin123)
INSERT INTO users (username, password_hash, role, first_name, last_name, email, phone, gender) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFDYZt98qhrKX.vM7Z0PQUO', 'admin', 'System', 'Administrator', 'admin@medisys.com', '+91-9876543210', 'Other')
ON CONFLICT (username) DO NOTHING;

-- Insert test users with complete profiles
INSERT INTO users (username, password_hash, role, first_name, last_name, email, phone, gender, date_of_birth, address) VALUES
('doctor', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFDYZt98qhrKX.vM7Z0PQUO', 'doctor', 'Dr. John', 'Smith', 'john.smith@medisys.com', '+91-9876543211', 'Male', '1980-05-15', '123 Medical Street, Healthcare City'),
('finance', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFDYZt98qhrKX.vM7Z0PQUO', 'finance', 'Sarah', 'Johnson', 'sarah.johnson@medisys.com', '+91-9876543212', 'Female', '1985-08-22', '456 Finance Avenue, Business District'),
('dept_head', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFDYZt98qhrKX.vM7Z0PQUO', 'department_head', 'Michael', 'Brown', 'michael.brown@medisys.com', '+91-9876543213', 'Male', '1975-12-10', '789 Leadership Lane, Admin Block'),
('nurse1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFDYZt98qhrKX.vM7Z0PQUO', 'nurse', 'Emily', 'Davis', 'emily.davis@medisys.com', '+91-9876543214', 'Female', '1990-03-18', '321 Care Street, Nursing Wing'),
('receptionist1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFDYZt98qhrKX.vM7Z0PQUO', 'receptionist', 'Lisa', 'Wilson', 'lisa.wilson@medisys.com', '+91-9876543215', 'Female', '1992-07-25', '654 Front Desk Road, Reception Area')
ON CONFLICT (username) DO NOTHING;

-- Insert test departments with enhanced data
INSERT INTO departments (department_code, name, description, location, floor_number, phone, email, budget, staff_count, bed_count, created_by) VALUES
('CARD', 'Cardiology', 'Heart and cardiovascular system', 'East Wing', 2, '+91-11-2345-6701', 'cardiology@medisys.com', 5000000.00, 15, 20, 1),
('NEURO', 'Neurology', 'Brain and nervous system', 'West Wing', 3, '+91-11-2345-6702', 'neurology@medisys.com', 4500000.00, 12, 15, 1),
('PEDIA', 'Pediatrics', 'Children healthcare', 'South Wing', 1, '+91-11-2345-6703', 'pediatrics@medisys.com', 3500000.00, 18, 25, 1),
('EMERG', 'Emergency', 'Emergency medical care', 'Main Building', 0, '+91-11-2345-6704', 'emergency@medisys.com', 6000000.00, 25, 10, 1),
('ORTHO', 'Orthopedics', 'Bone and joint care', 'North Wing', 2, '+91-11-2345-6705', 'orthopedics@medisys.com', 4000000.00, 14, 18, 1),
('GYNE', 'Gynecology', 'Women healthcare', 'East Wing', 1, '+91-11-2345-6706', 'gynecology@medisys.com', 3800000.00, 16, 22, 1);

-- Insert test doctors with enhanced data
INSERT INTO doctors (doctor_id, first_name, last_name, specialization, license_number, email, phone, qualification, experience_years, consultation_fee, bio, languages_spoken, available_days, available_time_start, available_time_end, department_id, user_id, created_by) VALUES
('DOC001', 'Dr. John', 'Smith', 'Cardiologist', 'MD001', 'john.smith@medisys.com', '+91-9876543221', 'MBBS, MD Cardiology', 15, 1500.00, 'Experienced cardiologist specializing in heart diseases and interventional cardiology.', 'English, Hindi', 'Mon,Tue,Wed,Thu,Fri', '09:00:00', '17:00:00', 1, 2, 1),
('DOC002', 'Dr. Sarah', 'Johnson', 'Neurologist', 'MD002', 'sarah.johnson@medisys.com', '+91-9876543222', 'MBBS, MD Neurology', 12, 1800.00, 'Specialist in brain and nervous system disorders with expertise in stroke management.', 'English, Hindi, Tamil', 'Mon,Wed,Fri,Sat', '10:00:00', '18:00:00', 2, 3, 1),
('DOC003', 'Dr. Michael', 'Brown', 'Pediatrician', 'MD003', 'michael.brown@medisys.com', '+91-9876543223', 'MBBS, MD Pediatrics', 10, 1200.00, 'Child specialist with focus on preventive care and childhood development.', 'English, Hindi', 'Mon,Tue,Thu,Fri,Sat', '08:00:00', '16:00:00', 3, 4, 1),
('DOC004', 'Dr. Emily', 'Davis', 'Emergency Medicine', 'MD004', 'emily.davis@medisys.com', '+91-9876543224', 'MBBS, MD Emergency Medicine', 8, 2000.00, 'Emergency medicine specialist available for critical care and trauma cases.', 'English, Hindi, Bengali', 'All Days', '00:00:00', '23:59:59', 4, 5, 1),
('DOC005', 'Dr. Rajesh', 'Kumar', 'Orthopedic Surgeon', 'MD005', 'rajesh.kumar@medisys.com', '+91-9876543225', 'MBBS, MS Orthopedics', 18, 2500.00, 'Senior orthopedic surgeon specializing in joint replacement and sports injuries.', 'English, Hindi, Punjabi', 'Tue,Wed,Thu,Fri,Sat', '09:00:00', '17:00:00', 5, NULL, 1)
ON CONFLICT (license_number) DO NOTHING;

-- Insert test patients with enhanced data
INSERT INTO patients (patient_id, first_name, last_name, date_of_birth, gender, blood_group, email, phone, address, city, state, zip_code, emergency_contact_name, emergency_contact_phone, emergency_contact_relation, insurance_provider, insurance_policy_number, created_by) VALUES
('PAT001', 'Alice', 'Johnson', '1985-03-15', 'Female', 'A+', 'alice.johnson@email.com', '+91-9876543301', '123 Main St, Sector 15', 'New Delhi', 'Delhi', '110001', 'Robert Johnson', '+91-9876543401', 'Husband', 'Star Health Insurance', 'SHI123456789', 1),
('PAT002', 'Bob', 'Wilson', '1978-07-22', 'Male', 'B+', 'bob.wilson@email.com', '+91-9876543302', '456 Oak Ave, Block A', 'Mumbai', 'Maharashtra', '400001', 'Mary Wilson', '+91-9876543402', 'Wife', 'HDFC ERGO', 'HDFC987654321', 1),
('PAT003', 'Carol', 'Davis', '1992-11-08', 'Female', 'O-', 'carol.davis@email.com', '+91-9876543303', '789 Pine Rd, Phase 2', 'Bangalore', 'Karnataka', '560001', 'James Davis', '+91-9876543403', 'Father', 'ICICI Lombard', 'ICICI456789123', 1),
('PAT004', 'David', 'Miller', '1965-05-30', 'Male', 'AB+', 'david.miller@email.com', '+91-9876543304', '321 Elm St, Colony', 'Chennai', 'Tamil Nadu', '600001', 'Susan Miller', '+91-9876543404', 'Wife', 'Bajaj Allianz', 'BA789123456', 1),
('PAT005', 'Eva', 'Garcia', '1988-09-12', 'Female', 'A-', 'eva.garcia@email.com', '+91-9876543305', '654 Maple Dr, Residency', 'Hyderabad', 'Telangana', '500001', 'Carlos Garcia', '+91-9876543405', 'Brother', 'Max Bupa', 'MB321654987', 1);

-- Insert test appointments
INSERT INTO appointments (patient_id, doctor_id, appointment_date, status) VALUES 
(1, 1, '2024-06-15 10:00:00', 'scheduled'),
(2, 2, '2024-06-15 14:30:00', 'scheduled'),
(3, 3, '2024-06-16 09:15:00', 'scheduled'),
(4, 4, '2024-06-16 16:45:00', 'completed'),
(5, 1, '2024-06-17 11:30:00', 'scheduled');

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_patients_name ON patients(name);
CREATE INDEX IF NOT EXISTS idx_appointments_date ON appointments(appointment_date);
CREATE INDEX IF NOT EXISTS idx_appointments_patient ON appointments(patient_id);
CREATE INDEX IF NOT EXISTS idx_appointments_doctor ON appointments(doctor_id);
CREATE INDEX IF NOT EXISTS idx_medical_records_patient ON medical_records(patient_id);
CREATE INDEX IF NOT EXISTS idx_billing_patient ON billing(patient_id);
CREATE INDEX IF NOT EXISTS idx_audit_log_timestamp ON audit_log(timestamp);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);

-- Grant permissions to medisys_user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO medisys_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO medisys_user;

-- Display setup completion message
SELECT 'MediSys database setup completed successfully!' as status;
