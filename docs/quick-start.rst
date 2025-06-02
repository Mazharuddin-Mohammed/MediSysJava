Quick Start Guide
=================

Get up and running with MediSys Hospital Management System in just 5 minutes! This guide will help you quickly install, configure, and start using MediSys.

----------
⚡ 5-Minute Setup
----------

~~~~~~~~~~
Step 1: Prerequisites Check (1 minute)
~~~~~~~~~~
.. code-block:: bash

      ==========
   Check Java version (must be 17+)
   ==========
   java -version

==========
Check Maven version (must be 3.8+)
==========
mvn -version

==========
If not installed, see detailed installation in Installation Guide
==========


~~~~~~~~~~
Step 2: Download & Run (2 minutes)
~~~~~~~~~~
.. code-block:: bash

      ==========
   Option A: Download pre-built release (Recommended)
   ==========
   wget https://github.com/Mazharuddin-Mohammed/MediSysJava/releases/latest/download/medisys-1.0.0.jar
   java -jar medisys-1.0.0.jar

==========
Option B: Build from source
==========
git clone https://github.com/Mazharuddin-Mohammed/MediSysJava.git
cd MediSysJava
mvn javafx:run


~~~~~~~~~~
Step 3: First Login (1 minute)
~~~~~~~~~~
#. **Application launches** automatically
#. **Login with default credentials**:
   - Username: ``admin``
   - Password: ``admin123``
#. **Click "Login"** to access the system

~~~~~~~~~~
Step 4: Explore Features (1 minute)
~~~~~~~~~~
#. **Dashboard**: View system overview
#. **Patients**: Add your first patient
#. **Doctors**: Register medical staff
#. **Appointments**: Schedule appointments
#. **Reports**: Generate sample reports

----------
🎯 First Steps After Login
----------

~~~~~~~~~~
#. Change Default Password
~~~~~~~~~~
.. code-block::

      1. Go to User Management
   #. Select admin user
   #. Click "Change Password"
   #. Enter new secure password
   #. Save changes


~~~~~~~~~~
#. Add Your First Patient
~~~~~~~~~~
.. code-block::

      1. Click "👥 Patients" in navigation
   #. Click "➕ Add Patient"
   #. Fill required fields:
      - Name: John Doe
      - Date of Birth: 1990-01-01
      - Gender: Male
      - Phone: +1-555-0123
   #. Click "Save"


~~~~~~~~~~
#. Register a Doctor
~~~~~~~~~~
.. code-block::

      1. Click "👨‍⚕️ Doctors" in navigation
   #. Click "➕ Add Doctor"
   #. Fill required fields:
      - Name: Dr. Sarah Smith
      - Specialization: Cardiology
      - Email: sarah.smith@hospital.com
      - Phone: +1-555-0124
   #. Click "Save"


~~~~~~~~~~
#. Schedule an Appointment
~~~~~~~~~~
.. code-block::

      1. Click "📅 Appointments" in navigation
   #. Click "➕ New Appointment"
   #. Select patient: John Doe
   #. Select doctor: Dr. Sarah Smith
   #. Choose date and time
   #. Add reason: "Regular checkup"
   #. Click "Save"


~~~~~~~~~~
#. Generate Your First Report
~~~~~~~~~~
.. code-block::

      1. Click "📊 Reports" in navigation
   #. Select "Patient Report"
   #. Choose date range: Last 30 days
   #. Click "Generate Report"
   #. Export as PDF or CSV


----------
🚀 Sample Data
----------

MediSys comes with pre-loaded sample data for immediate testing:

~~~~~~~~~~
Sample Patients
~~~~~~~~~~
* **John Doe** - Male, 34 years old, Cardiology patient
* **Jane Smith** - Female, 28 years old, Dermatology patient
* **Robert Johnson** - Male, 45 years old, Orthopedics patient

~~~~~~~~~~
Sample Doctors
~~~~~~~~~~
* **Dr. Sarah Wilson** - Cardiologist, 10 years experience
* **Dr. Michael Brown** - Dermatologist, 8 years experience
* **Dr. Emily Davis** - Orthopedic Surgeon, 12 years experience

~~~~~~~~~~
Sample Appointments
~~~~~~~~~~
* **Today's appointments** with various doctors
* **Past appointments** for history demonstration
* **Future appointments** for scheduling examples

----------
🎨 Interface Overview
----------

~~~~~~~~~~
Main Navigation
~~~~~~~~~~
* **🏠 Dashboard**: System overview and statistics
* **👥 Patients**: Patient management and records
* **👨‍⚕️ Doctors**: Doctor profiles and schedules
* **📅 Appointments**: Scheduling and calendar
* **🚨 Emergency**: Emergency admissions
* **💰 Finance**: Billing and payments
* **📊 Reports**: Analytics and reporting
* **👤 Users**: User management and roles
* **❓ Help**: Documentation and support

~~~~~~~~~~
Quick Actions
~~~~~~~~~~
* **➕ Add Patient**: Quick patient registration
* **➕ Add Doctor**: Quick doctor registration
* **📅 New Appointment**: Quick appointment booking
* **💰 New Bill**: Quick billing
* **📊 Generate Report**: Quick report generation

----------
🔐 User Roles & Access
----------

~~~~~~~~~~
System Administrator (admin/admin123)
~~~~~~~~~~
* **Full access** to all modules
* **User management** capabilities
* **System configuration** access
* **All reports** and analytics

~~~~~~~~~~
Doctor (doctor/admin123)
~~~~~~~~~~
* **Patient records** access
* **Appointment management**
* **Medical history** viewing
* **Patient reports** generation

~~~~~~~~~~
Finance (finance/admin123)
~~~~~~~~~~
* **Billing management**
* **Payment processing**
* **Financial reports**
* **Revenue analytics**

----------
📱 Key Features to Try
----------

~~~~~~~~~~
#. Patient Management
~~~~~~~~~~
* **Add patients** with photos
* **Search and filter** patients
* **View medical history**
* **Update patient information**

~~~~~~~~~~
#. Appointment System
~~~~~~~~~~
* **Calendar-based scheduling**
* **Doctor availability checking**
* **Appointment status tracking**
* **Conflict prevention**

~~~~~~~~~~
#. Financial Operations
~~~~~~~~~~
* **Create patient bills**
* **Process payments**
* **Track outstanding amounts**
* **Generate financial reports**

~~~~~~~~~~
#. Reporting & Analytics
~~~~~~~~~~
* **Patient demographics**
* **Doctor performance**
* **Financial summaries**
* **Department analytics**

----------
🛠️ Customization Options
----------

~~~~~~~~~~
#. User Interface
~~~~~~~~~~
* **Color themes**: Modify CSS files in ``src/main/resources/css/``
* **Logos and branding**: Replace images in ``src/main/resources/images/``
* **Layout adjustments**: Modify FXML files in ``src/main/resources/fxml/``

~~~~~~~~~~
#. Database Configuration
~~~~~~~~~~
.. code-block:: properties

      ==========
   Edit src/main/resources/application.properties
   ==========
   spring.datasource.url=jdbc:h2:file:./medisys_db
   spring.datasource.driver-class-name=org.h2.Driver
   spring.jpa.hibernate.ddl-auto=update


~~~~~~~~~~
#. System Settings
~~~~~~~~~~
* **Hospital name**: Update in system configuration
* **Contact information**: Modify in application settings
* **Report headers**: Customize in report templates

----------
🔧 Common Tasks
----------

~~~~~~~~~~
Adding Multiple Patients
~~~~~~~~~~
#. **Prepare CSV file** with patient data
#. **Use bulk import** feature (coming soon)
#. **Or add manually** one by one

~~~~~~~~~~
Setting Up Departments
~~~~~~~~~~
#. **Go to Admin settings**
#. **Create departments**: Cardiology, Neurology, etc.
#. **Assign doctors** to departments
#. **Configure department settings**

~~~~~~~~~~
Configuring Appointment Slots
~~~~~~~~~~
#. **Go to Doctor management**
#. **Select doctor**
#. **Set working hours**
#. **Define time slots**
#. **Set break times**

----------
🆘 Quick Troubleshooting
----------

~~~~~~~~~~
Application Won't Start
~~~~~~~~~~
.. code-block:: bash

      ==========
   Check Java version
   ==========
   java -version

==========
Ensure Java 17+
==========
export JAVA_HOME=/path/to/java17

==========
Try with more memory
==========
java -Xmx2G -jar medisys-1.0.0.jar


~~~~~~~~~~
Login Issues
~~~~~~~~~~
* **Check credentials**: admin/admin123
* **Clear browser cache** (if applicable)
* **Restart application**
* **Check logs** for error messages

~~~~~~~~~~
Database Issues
~~~~~~~~~~
.. code-block:: bash

      ==========
   Check database files
   ==========
   ls -la *.db

==========
Reset database (caution: deletes data)
==========
rm -f *.db *.trace.db *.lock.db


~~~~~~~~~~
Performance Issues
~~~~~~~~~~
* **Close unused windows**
* **Restart application**
* **Check system resources**
* **Increase memory**: ``java -Xmx4G -jar medisys.jar``

----------
📚 Next Steps
----------

After completing this quick start:

#. **`User Manual <User-Manual.md>`_** - Detailed feature documentation
#. **`Installation Guide <Installation-Guide.md>`_** - Complete installation instructions
#. **`Admin Guide <Admin-Guide.md>`_** - System administration
#. **`FAQ <FAQ.md>`_** - Common questions and answers

----------
🎯 Success Checklist
----------

* [ ] ✅ Application starts successfully
* [ ] ✅ Login with default credentials works
* [ ] ✅ Dashboard displays system overview
* [ ] ✅ Can add a new patient
* [ ] ✅ Can register a new doctor
* [ ] ✅ Can schedule an appointment
* [ ] ✅ Can generate a report
* [ ] ✅ Can export report as PDF/CSV
* [ ] ✅ All modules are accessible
* [ ] ✅ Sample data is visible

----------
📞 Need Help?
----------

If you encounter any issues during quick start:

* **📧 Email**: mazharuddin.mohammed.official@fmail.com
* **🐛 GitHub Issues**: `Report Problems <https://github.com/Mazharuddin-Mohammed/MediSysJava/issues>`_
* **💬 Discussions**: `Ask Questions <https://github.com/Mazharuddin-Mohammed/MediSysJava/discussions>`_
* **📱 Phone**: +91-9347607780

---

**Congratulations! You're now ready to use MediSys Hospital Management System! 🎉**

*Quick Start Guide - Last updated: June 2025 | Version 1.0.0*
