CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS patients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    date_of_birth VARCHAR(10),
    contact_info VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    head_of_department VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS doctors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialty VARCHAR(50),
    contact_info VARCHAR(255),
    department_id BIGINT,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS finance (
    id SERIAL PRIMARY KEY,
    patient_id BIGINT,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(id)
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id SERIAL PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    timestamp TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert sample data
INSERT INTO users (username, password_hash, role)
VALUES ('admin', '$2a$10$7s5g9Xz5z5q8k9t4u8v2wezq5y7u8i9o0p1q2w3e4r5t6y7u8i9o', 'admin')
ON CONFLICT (username) DO NOTHING;

INSERT INTO patients (name, date_of_birth, contact_info)
VALUES ('John Doe', '1990-01-01', 'john.doe@example.com')
ON CONFLICT (id) DO NOTHING;

INSERT INTO departments (name, head_of_department)
VALUES ('Cardiology', 'Dr. Smith')
ON CONFLICT (id) DO NOTHING;

INSERT INTO doctors (name, specialty, contact_info, department_id)
VALUES ('Dr. Smith', 'Cardiology', 'smith@example.com', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO finance (patient_id, amount, status)
VALUES (1, 1500.00, 'PENDING')
ON CONFLICT (id) DO NOTHING;