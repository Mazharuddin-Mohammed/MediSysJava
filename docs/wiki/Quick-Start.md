# Quick Start Guide

Get up and running with MediSys Hospital Management System in just 5 minutes! This guide will help you quickly install, configure, and start using MediSys.

## ⚡ 5-Minute Setup

### Step 1: Prerequisites Check (1 minute)
```bash
# Check Java version (must be 17+)
java -version

# Check Maven version (must be 3.8+)
mvn -version

# If not installed, see detailed installation in Installation Guide
```

### Step 2: Download & Run (2 minutes)
```bash
# Option A: Download pre-built release (Recommended)
wget https://github.com/Mazharuddin-Mohammed/MediSysJava/releases/latest/download/medisys-1.0.0.jar
java -jar medisys-1.0.0.jar

# Option B: Build from source
git clone https://github.com/Mazharuddin-Mohammed/MediSysJava.git
cd MediSysJava
mvn javafx:run
```

### Step 3: First Login (1 minute)
1. **Application launches** automatically
2. **Login with default credentials**:
   - Username: `admin`
   - Password: `admin123`
3. **Click "Login"** to access the system

### Step 4: Explore Features (1 minute)
1. **Dashboard**: View system overview
2. **Patients**: Add your first patient
3. **Doctors**: Register medical staff
4. **Appointments**: Schedule appointments
5. **Reports**: Generate sample reports

## 🎯 First Steps After Login

### 1. Change Default Password
```
1. Go to User Management
2. Select admin user
3. Click "Change Password"
4. Enter new secure password
5. Save changes
```

### 2. Add Your First Patient
```
1. Click "👥 Patients" in navigation
2. Click "➕ Add Patient"
3. Fill required fields:
   - Name: John Doe
   - Date of Birth: 1990-01-01
   - Gender: Male
   - Phone: +1-555-0123
4. Click "Save"
```

### 3. Register a Doctor
```
1. Click "👨‍⚕️ Doctors" in navigation
2. Click "➕ Add Doctor"
3. Fill required fields:
   - Name: Dr. Sarah Smith
   - Specialization: Cardiology
   - Email: sarah.smith@hospital.com
   - Phone: +1-555-0124
4. Click "Save"
```

### 4. Schedule an Appointment
```
1. Click "📅 Appointments" in navigation
2. Click "➕ New Appointment"
3. Select patient: John Doe
4. Select doctor: Dr. Sarah Smith
5. Choose date and time
6. Add reason: "Regular checkup"
7. Click "Save"
```

### 5. Generate Your First Report
```
1. Click "📊 Reports" in navigation
2. Select "Patient Report"
3. Choose date range: Last 30 days
4. Click "Generate Report"
5. Export as PDF or CSV
```

## 🚀 Sample Data

MediSys comes with pre-loaded sample data for immediate testing:

### Sample Patients
- **John Doe** - Male, 34 years old, Cardiology patient
- **Jane Smith** - Female, 28 years old, Dermatology patient
- **Robert Johnson** - Male, 45 years old, Orthopedics patient

### Sample Doctors
- **Dr. Sarah Wilson** - Cardiologist, 10 years experience
- **Dr. Michael Brown** - Dermatologist, 8 years experience
- **Dr. Emily Davis** - Orthopedic Surgeon, 12 years experience

### Sample Appointments
- **Today's appointments** with various doctors
- **Past appointments** for history demonstration
- **Future appointments** for scheduling examples

## 🎨 Interface Overview

### Main Navigation
- **🏠 Dashboard**: System overview and statistics
- **👥 Patients**: Patient management and records
- **👨‍⚕️ Doctors**: Doctor profiles and schedules
- **📅 Appointments**: Scheduling and calendar
- **🚨 Emergency**: Emergency admissions
- **💰 Finance**: Billing and payments
- **📊 Reports**: Analytics and reporting
- **👤 Users**: User management and roles
- **❓ Help**: Documentation and support

### Quick Actions
- **➕ Add Patient**: Quick patient registration
- **➕ Add Doctor**: Quick doctor registration
- **📅 New Appointment**: Quick appointment booking
- **💰 New Bill**: Quick billing
- **📊 Generate Report**: Quick report generation

## 🔐 User Roles & Access

### System Administrator (admin/admin123)
- **Full access** to all modules
- **User management** capabilities
- **System configuration** access
- **All reports** and analytics

### Doctor (doctor/admin123)
- **Patient records** access
- **Appointment management**
- **Medical history** viewing
- **Patient reports** generation

### Finance (finance/admin123)
- **Billing management**
- **Payment processing**
- **Financial reports**
- **Revenue analytics**

## 📱 Key Features to Try

### 1. Patient Management
- **Add patients** with photos
- **Search and filter** patients
- **View medical history**
- **Update patient information**

### 2. Appointment System
- **Calendar-based scheduling**
- **Doctor availability checking**
- **Appointment status tracking**
- **Conflict prevention**

### 3. Financial Operations
- **Create patient bills**
- **Process payments**
- **Track outstanding amounts**
- **Generate financial reports**

### 4. Reporting & Analytics
- **Patient demographics**
- **Doctor performance**
- **Financial summaries**
- **Department analytics**

## 🛠️ Customization Options

### 1. User Interface
- **Color themes**: Modify CSS files in `src/main/resources/css/`
- **Logos and branding**: Replace images in `src/main/resources/images/`
- **Layout adjustments**: Modify FXML files in `src/main/resources/fxml/`

### 2. Database Configuration
```properties
# Edit src/main/resources/application.properties
spring.datasource.url=jdbc:h2:file:./medisys_db
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update
```

### 3. System Settings
- **Hospital name**: Update in system configuration
- **Contact information**: Modify in application settings
- **Report headers**: Customize in report templates

## 🔧 Common Tasks

### Adding Multiple Patients
1. **Prepare CSV file** with patient data
2. **Use bulk import** feature (coming soon)
3. **Or add manually** one by one

### Setting Up Departments
1. **Go to Admin settings**
2. **Create departments**: Cardiology, Neurology, etc.
3. **Assign doctors** to departments
4. **Configure department settings**

### Configuring Appointment Slots
1. **Go to Doctor management**
2. **Select doctor**
3. **Set working hours**
4. **Define time slots**
5. **Set break times**

## 🆘 Quick Troubleshooting

### Application Won't Start
```bash
# Check Java version
java -version

# Ensure Java 17+
export JAVA_HOME=/path/to/java17

# Try with more memory
java -Xmx2G -jar medisys-1.0.0.jar
```

### Login Issues
- **Check credentials**: admin/admin123
- **Clear browser cache** (if applicable)
- **Restart application**
- **Check logs** for error messages

### Database Issues
```bash
# Check database files
ls -la *.db

# Reset database (caution: deletes data)
rm -f *.db *.trace.db *.lock.db
```

### Performance Issues
- **Close unused windows**
- **Restart application**
- **Check system resources**
- **Increase memory**: `java -Xmx4G -jar medisys.jar`

## 📚 Next Steps

After completing this quick start:

1. **[User Manual](User-Manual.md)** - Detailed feature documentation
2. **[Installation Guide](Installation-Guide.md)** - Complete installation instructions
3. **[Admin Guide](Admin-Guide.md)** - System administration
4. **[FAQ](FAQ.md)** - Common questions and answers

## 🎯 Success Checklist

- [ ] ✅ Application starts successfully
- [ ] ✅ Login with default credentials works
- [ ] ✅ Dashboard displays system overview
- [ ] ✅ Can add a new patient
- [ ] ✅ Can register a new doctor
- [ ] ✅ Can schedule an appointment
- [ ] ✅ Can generate a report
- [ ] ✅ Can export report as PDF/CSV
- [ ] ✅ All modules are accessible
- [ ] ✅ Sample data is visible

## 📞 Need Help?

If you encounter any issues during quick start:

- **📧 Email**: mazharuddin.mohammed.official@fmail.com
- **🐛 GitHub Issues**: [Report Problems](https://github.com/Mazharuddin-Mohammed/MediSysJava/issues)
- **💬 Discussions**: [Ask Questions](https://github.com/Mazharuddin-Mohammed/MediSysJava/discussions)
- **📱 Phone**: +91-9347607780

---

**Congratulations! You're now ready to use MediSys Hospital Management System! 🎉**

*Quick Start Guide - Last updated: June 2025 | Version 1.0.0*
