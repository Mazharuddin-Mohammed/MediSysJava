# Installation Guide

This comprehensive guide will walk you through installing MediSys Hospital Management System on your system.

## 📋 Prerequisites

### System Requirements
- **Operating System**: Windows 10+, macOS 10.14+, or Linux (Ubuntu 18.04+)
- **Java**: OpenJDK 17+ or Oracle JDK 17+
- **Memory**: Minimum 4GB RAM (8GB recommended)
- **Storage**: 2GB free disk space
- **Display**: 1366x768 minimum resolution (1920x1080 recommended)

### Required Software
1. **Java Development Kit (JDK) 17+**
2. **Apache Maven 3.8+**
3. **Git** (for source code)
4. **Database** (H2 embedded included, PostgreSQL optional)

## 🚀 Installation Methods

### Method 1: Pre-built Release (Recommended)

#### Step 1: Download Release
```bash
# Download latest release from GitHub
wget https://github.com/Mazharuddin-Mohammed/MediSysJava/releases/latest/download/medisys-1.0.0.jar

# Or download using curl
curl -L -o medisys-1.0.0.jar https://github.com/Mazharuddin-Mohammed/MediSysJava/releases/latest/download/medisys-1.0.0.jar
```

#### Step 2: Run Application
```bash
# Run the application
java -jar medisys-1.0.0.jar

# Or with specific memory settings
java -Xmx2G -jar medisys-1.0.0.jar
```

### Method 2: Build from Source

#### Step 1: Clone Repository
```bash
# Clone the repository
git clone https://github.com/Mazharuddin-Mohammed/MediSysJava.git
cd MediSysJava
```

#### Step 2: Build Application
```bash
# Clean and compile
mvn clean compile

# Run tests (optional)
mvn test

# Package application
mvn package

# Run application
mvn javafx:run
```

## 🔧 Detailed Installation Steps

### Windows Installation

#### 1. Install Java JDK 17+
```powershell
# Download from Oracle or use Chocolatey
choco install openjdk17

# Verify installation
java -version
javac -version
```

#### 2. Install Maven
```powershell
# Using Chocolatey
choco install maven

# Verify installation
mvn -version
```

#### 3. Install Git
```powershell
# Using Chocolatey
choco install git

# Verify installation
git --version
```

#### 4. Clone and Build
```powershell
# Clone repository
git clone https://github.com/Mazharuddin-Mohammed/MediSysJava.git
cd MediSysJava

# Build and run
mvn clean compile
mvn javafx:run
```

### macOS Installation

#### 1. Install Java JDK 17+
```bash
# Using Homebrew
brew install openjdk@17

# Add to PATH
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# Verify installation
java -version
```

#### 2. Install Maven
```bash
# Using Homebrew
brew install maven

# Verify installation
mvn -version
```

#### 3. Install Git
```bash
# Using Homebrew
brew install git

# Verify installation
git --version
```

#### 4. Clone and Build
```bash
# Clone repository
git clone https://github.com/Mazharuddin-Mohammed/MediSysJava.git
cd MediSysJava

# Build and run
mvn clean compile
mvn javafx:run
```

### Linux (Ubuntu/Debian) Installation

#### 1. Update System
```bash
sudo apt update
sudo apt upgrade -y
```

#### 2. Install Java JDK 17+
```bash
# Install OpenJDK 17
sudo apt install openjdk-17-jdk -y

# Verify installation
java -version
javac -version

# Set JAVA_HOME (if needed)
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
source ~/.bashrc
```

#### 3. Install Maven
```bash
# Install Maven
sudo apt install maven -y

# Verify installation
mvn -version
```

#### 4. Install Git
```bash
# Install Git
sudo apt install git -y

# Verify installation
git --version
```

#### 5. Clone and Build
```bash
# Clone repository
git clone https://github.com/Mazharuddin-Mohammed/MediSysJava.git
cd MediSysJava

# Build and run
mvn clean compile
mvn javafx:run
```

## 🗄️ Database Setup

### H2 Database (Default)
- **No additional setup required**
- Database files created automatically in project directory
- Suitable for development and small deployments

### PostgreSQL (Production)

#### 1. Install PostgreSQL
```bash
# Ubuntu/Debian
sudo apt install postgresql postgresql-contrib

# macOS
brew install postgresql

# Windows
# Download from https://www.postgresql.org/download/windows/
```

#### 2. Create Database
```sql
-- Connect to PostgreSQL
sudo -u postgres psql

-- Create database and user
CREATE DATABASE medisys;
CREATE USER medisys_user WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE medisys TO medisys_user;
```

#### 3. Configure Application
```properties
# Edit src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medisys
spring.datasource.username=medisys_user
spring.datasource.password=secure_password
spring.datasource.driver-class-name=org.postgresql.Driver
```

## ⚙️ Configuration

### Application Properties
```properties
# Database Configuration
spring.datasource.url=jdbc:h2:file:./medisys_db
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update

# Server Configuration
server.port=8080
server.servlet.context-path=/medisys

# Logging Configuration
logging.level.com.medisys=DEBUG
logging.file.name=logs/medisys.log

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### JVM Options
```bash
# Memory settings
-Xms512m -Xmx2g

# JavaFX module path (if needed)
--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml

# System properties
-Dfile.encoding=UTF-8
-Djava.awt.headless=false
```

## 🔍 Verification

### 1. Check Installation
```bash
# Verify Java
java -version
# Should show: openjdk version "17.0.x" or higher

# Verify Maven
mvn -version
# Should show: Apache Maven 3.8.x or higher

# Verify Git
git --version
# Should show: git version 2.x.x or higher
```

### 2. Test Application
```bash
# Navigate to project directory
cd MediSysJava

# Run application
mvn javafx:run

# Application should start and show login window
```

### 3. Default Login
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: System Administrator

## 🛠️ Troubleshooting

### Common Issues

#### Java Version Issues
```bash
# Check Java version
java -version

# If wrong version, set JAVA_HOME
export JAVA_HOME=/path/to/java17
export PATH=$JAVA_HOME/bin:$PATH
```

#### Maven Issues
```bash
# Clear Maven cache
mvn clean

# Force update dependencies
mvn clean install -U

# Skip tests if failing
mvn clean install -DskipTests
```

#### JavaFX Issues
```bash
# If JavaFX modules not found
mvn javafx:run -Djavafx.runtime.path=/path/to/javafx

# Or add to pom.xml
<javafx.runtime.path>/path/to/javafx</javafx.runtime.path>
```

#### Database Issues
```bash
# Check H2 database files
ls -la *.db

# Reset database (caution: deletes all data)
rm -f *.db *.trace.db *.lock.db
```

### Error Solutions

#### "Module not found" Error
```bash
# Add JavaFX modules explicitly
--add-modules javafx.controls,javafx.fxml,javafx.web
```

#### "Permission denied" Error
```bash
# Make script executable
chmod +x run.sh

# Or run with sudo (Linux/macOS)
sudo mvn javafx:run
```

#### "Port already in use" Error
```bash
# Find process using port
lsof -i :8080

# Kill process
kill -9 <PID>

# Or change port in application.properties
server.port=8081
```

## 🚀 Next Steps

After successful installation:

1. **[Quick Start Guide](Quick-Start.md)** - Get familiar with the interface
2. **[User Manual](User-Manual.md)** - Learn core features
3. **[Admin Guide](Admin-Guide.md)** - Set up users and permissions
4. **[Configuration](Configuration.md)** - Customize settings

## 📞 Support

If you encounter issues during installation:

- **📧 Email**: mazharuddin.mohammed.official@fmail.com
- **🐛 GitHub Issues**: [Report Installation Issues](https://github.com/Mazharuddin-Mohammed/MediSysJava/issues)
- **📱 Phone**: +91-9347607780

---

*Installation Guide - Last updated: June 2025*
