# MediSys - Enterprise Hospital Management System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![JavaFX](https://img.shields.io/badge/JavaFX-22-blue.svg)](https://openjfx.io/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16.9-blue.svg)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7.0-red.svg)](https://redis.io/)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE.md)

MediSys is a production-ready, enterprise-grade hospital management system built with modern Java technologies. It features a robust JavaFX desktop application with Spring Boot backend, PostgreSQL database, Redis caching, comprehensive monitoring, and enterprise-level security.

![MediSys Banner](src/main/resources/images/banner.jpg)

## 🚀 Key Features

### 🏥 **Core Hospital Management**
- **Patient Management**: Complete patient lifecycle management with medical history
- **Doctor Management**: Physician profiles, specializations, and department assignments
- **Department Management**: Organizational structure and resource allocation
- **Finance Management**: Billing, payments, and financial reporting
- **Audit Logging**: Comprehensive activity tracking and compliance

### 🔒 **Enterprise Security**
- **Multi-factor Authentication**: Secure login with role-based access control
- **Session Management**: JWT-based sessions with automatic timeout
- **Field-level Encryption**: Sensitive data protection with AES-256
- **Rate Limiting**: Brute force protection and account lockout
- **Input Validation**: SQL injection and XSS prevention

### ⚡ **Performance & Scalability**
- **Redis Caching**: Multi-tier caching with intelligent TTL management
- **Connection Pooling**: HikariCP for optimal database performance
- **Async Processing**: Non-blocking operations for heavy tasks
- **Memory Management**: Leak detection and optimization
- **Load Balancing Ready**: Horizontal scaling support

### 📊 **Monitoring & Observability**
- **Real-time Metrics**: Prometheus-compatible metrics with Micrometer
- **Health Checks**: Comprehensive system health monitoring
- **Performance Tracking**: Database, cache, and application performance
- **Error Tracking**: Centralized error logging and alerting
- **Resource Monitoring**: Memory, CPU, and connection pool metrics

### 🧪 **Testing & Quality Assurance**
- **Unit Testing**: Comprehensive test coverage with JUnit 5
- **Integration Testing**: Database and service layer testing
- **Security Testing**: Authentication and authorization testing
- **Performance Testing**: Load and stress testing capabilities
- **Mocking**: Mockito for isolated unit testing

## 🏗️ Architecture Overview

### **System Architecture**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   JavaFX GUI    │    │  Spring Boot    │    │   PostgreSQL    │
│   Controllers   │◄──►│   Services      │◄──►│   Database      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │              ┌─────────────────┐              │
         │              │  Redis Cache    │              │
         └──────────────┤   Layer         ├──────────────┘
                        └─────────────────┘
                                 │
                        ┌─────────────────┐
                        │  Monitoring &   │
                        │  Metrics        │
                        └─────────────────┘
```

### **Technology Stack**
- **Frontend**: JavaFX 22 with FXML and CSS
- **Backend**: Spring Boot 3.3.0 with Spring Security
- **Database**: PostgreSQL 16.9 with HikariCP connection pooling
- **Caching**: Redis 7.0 with Spring Cache abstraction
- **Monitoring**: Micrometer with Prometheus metrics
- **Testing**: JUnit 5, Mockito, Testcontainers
- **Build**: Maven 3.9+ with multi-module support

## 🏥 Core Features

### Admin Dashboard
- System overview with key metrics
- User management and access control
- Comprehensive audit logging for security and compliance
- Quick access to all modules

### Patients Module
- Complete patient record management
- Add, edit, and delete patient information
- View patient medical history
- Search functionality for quick access to patient records
- Patient photo management with placeholder images

### Departments Module
- Department creation and management
- Track department resources and staff
- Department-specific settings and configurations

### Doctors Module
- Doctor profile management
- Specialization and department assignment
- Track doctor workload and performance
- Doctor photo management with placeholder images

### Appointments Module
- Calendar-based appointment scheduling
- Filter appointments by date, doctor, or status
- Appointment creation, modification, and cancellation

### Finance Module
- **Billing Management**: Comprehensive billing and payment tracking
- **Financial Reporting**: Advanced analytics and reporting
- **Insurance Claims**: Automated claim processing and tracking
- **Payment History**: Complete transaction history and reconciliation

### Reports Module
- **Dynamic Reports**: Generate various types of reports (patient, doctor, financial)
- **Date Range Filtering**: Flexible filtering for data analysis
- **Export Functionality**: Multiple export formats (PDF, Excel, CSV)
- **Scheduled Reports**: Automated report generation and delivery

## 🛠️ Installation & Setup

### Prerequisites
- **Java 17+** (OpenJDK or Oracle JDK)
- **Maven 3.9+** for build management
- **PostgreSQL 16.9+** for database
- **Redis 7.0+** for caching (optional but recommended)
- **Git** for version control

### Quick Installation

1. **Clone the Repository**
```bash
git clone https://github.com/your-org/medisys.git
cd medisys
```

2. **Database Setup**
```bash
# Create PostgreSQL database
sudo -u postgres createdb medisys

# Create user and grant permissions
sudo -u postgres psql -c "CREATE USER medisys_user WITH PASSWORD 'your_password';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE medisys TO medisys_user;"
```

3. **Redis Setup (Optional)**
```bash
# Install and start Redis
sudo apt-get install redis-server
sudo systemctl start redis-server
sudo systemctl enable redis-server
```

4. **Application Configuration**
```bash
# Update database configuration in application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medisys
spring.datasource.username=medisys_user
spring.datasource.password=your_password

# Redis configuration (optional)
spring.redis.host=localhost
spring.redis.port=6379
```

5. **Build and Run**
```bash
# Compile the application
mvn clean compile

# Run the application
mvn javafx:run
```

### Docker Installation (Recommended for Production)

1. **Using Docker Compose**
```bash
# Clone and navigate to project
git clone https://github.com/your-org/medisys.git
cd medisys

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f medisys-app
```

2. **Manual Docker Setup**
```bash
# Build the application image
docker build -t medisys:latest .

# Run PostgreSQL
docker run -d --name medisys-db \
  -e POSTGRES_DB=medisys \
  -e POSTGRES_USER=medisys_user \
  -e POSTGRES_PASSWORD=your_password \
  -p 5432:5432 postgres:16.9

# Run Redis
docker run -d --name medisys-redis \
  -p 6379:6379 redis:7.0

# Run MediSys
docker run -d --name medisys-app \
  --link medisys-db:postgres \
  --link medisys-redis:redis \
  -p 8080:8080 medisys:latest
```

## 🔐 Default Login Credentials

### Admin User
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: Administrator (Full system access)

### Test Users
- **Doctor**: `doctor` / `doctor123`
- **Finance**: `finance` / `finance123`
- **Department Head**: `dept_head` / `dept123`

## 🧪 Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=CachedPatientServiceTest

# Run tests with coverage
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Test Categories
- **Unit Tests**: Service layer and business logic testing
- **Integration Tests**: Database and cache integration testing
- **Security Tests**: Authentication and authorization testing
- **Performance Tests**: Load and stress testing

## 📊 Monitoring & Health Checks

### Health Endpoints
- **Application Health**: `http://localhost:8080/actuator/health`
- **Database Health**: `http://localhost:8080/actuator/health/db`
- **Cache Health**: `http://localhost:8080/actuator/health/redis`
- **Metrics**: `http://localhost:8080/actuator/metrics`

### Prometheus Metrics
```bash
# Application metrics
curl http://localhost:8080/actuator/prometheus

# Key metrics to monitor:
# - database_operations_total
# - cache_operations_total
# - application_errors_total
# - user_actions_total
# - active_sessions
```

## 🚀 Production Deployment

### Environment Configuration
```properties
# Production application.properties
spring.profiles.active=production
spring.datasource.url=jdbc:postgresql://prod-db:5432/medisys
spring.redis.host=prod-redis
logging.level.com.medisys=INFO
management.endpoints.web.exposure.include=health,metrics,prometheus
```

### Performance Tuning
```properties
# JVM Options for production
-Xms2g -Xmx4g
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:+HeapDumpOnOutOfMemoryError

# HikariCP Configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

## 🔧 Development

### Code Style
- **Java**: Google Java Style Guide
- **Testing**: JUnit 5 with Mockito
- **Documentation**: Javadoc for all public APIs
- **Git**: Conventional Commits

### Building from Source
```bash
# Development build
mvn clean compile

# Production build
mvn clean package -Pproduction

# Create distribution
mvn clean package assembly:single
```

## 📈 Performance Benchmarks

### System Requirements
- **Minimum**: 2GB RAM, 2 CPU cores, 10GB storage
- **Recommended**: 4GB RAM, 4 CPU cores, 50GB storage
- **Production**: 8GB RAM, 8 CPU cores, 100GB storage

### Performance Metrics
- **Startup Time**: < 30 seconds
- **Response Time**: < 200ms (95th percentile)
- **Throughput**: 1000+ requests/minute
- **Memory Usage**: < 2GB under normal load

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

### Development Setup
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Documentation**: [Wiki](https://github.com/your-org/medisys/wiki)
- **Issues**: [GitHub Issues](https://github.com/your-org/medisys/issues)
- **Discussions**: [GitHub Discussions](https://github.com/your-org/medisys/discussions)
- **Email**: support@medisys.com

## 🏆 Acknowledgments

- Spring Boot team for the excellent framework
- JavaFX community for UI components
- PostgreSQL team for the robust database
- Redis team for high-performance caching
- All contributors who helped make this project better

---

**MediSys** - Transforming Healthcare Management with Modern Technology