MediSys Hospital Management System Documentation
==============================================

.. image:: https://img.shields.io/badge/Java-17-orange.svg
   :target: https://openjdk.java.net/projects/jdk/17/
   :alt: Java 17

.. image:: https://img.shields.io/badge/JavaFX-22-blue.svg
   :target: https://openjfx.io/
   :alt: JavaFX 22

.. image:: https://img.shields.io/badge/Spring%20Boot-3.3.0-green.svg
   :target: https://spring.io/projects/spring-boot
   :alt: Spring Boot 3.3.0

.. image:: https://img.shields.io/badge/build-passing-brightgreen.svg
   :alt: Build Status

.. image:: https://img.shields.io/badge/license-MIT-blue.svg
   :target: https://github.com/Mazharuddin-Mohammed/MediSysJava/blob/master/LICENSE
   :alt: MIT License

.. image:: https://readthedocs.org/projects/medisysjava/badge/?version=latest
   :target: https://medisysjava.readthedocs.io/en/latest/?badge=latest
   :alt: Documentation Status

Welcome to the comprehensive documentation for **MediSys Hospital Management System** - a modern, enterprise-grade healthcare management solution featuring a vibrant JavaFX desktop application with comprehensive patient management, clinical workflows, and financial operations.

.. image:: _static/images/banner.jpg
   :alt: MediSys Banner
   :align: center
   :width: 100%

Quick Navigation
================

.. grid:: 2
   :gutter: 3

   .. grid-item-card:: 🚀 Quick Start
      :link: quick-start
      :link-type: doc

      Get MediSys running in 5 minutes with our quick start guide.

   .. grid-item-card:: 📖 User Manual
      :link: user-manual
      :link-type: doc

      Complete user documentation covering all features and modules.

   .. grid-item-card:: 🛠️ Installation
      :link: installation
      :link-type: doc

      Detailed installation instructions for all platforms.

   .. grid-item-card:: 💻 Developer Guide
      :link: developer-guide
      :link-type: doc

      Development setup, architecture, and contribution guidelines.

System Overview
===============

MediSys is built with modern technologies and follows enterprise-grade architecture patterns:

**Core Features:**

* **👥 Patient Management** - Complete patient lifecycle management
* **👨‍⚕️ Doctor Management** - Physician profiles and scheduling
* **📅 Appointment System** - Calendar-based scheduling with conflict prevention
* **💰 Financial Management** - Billing, payments, and financial reporting
* **📊 Reports & Analytics** - Comprehensive reporting with PDF/CSV export
* **🔐 Security** - Role-based access control and data protection

**Technology Stack:**

* **Frontend:** JavaFX 22 with FXML and CSS
* **Backend:** Spring Boot 3.3.0 with Spring Security
* **Database:** PostgreSQL 16.9 with H2 for development
* **Build:** Maven 3.9+ with multi-module support
* **Testing:** JUnit 5, Mockito, Testcontainers

Getting Started
===============

.. note::
   **Default Login Credentials:**
   
   * **Username:** ``admin``
   * **Password:** ``admin123``
   * **Role:** System Administrator

For immediate setup, see our :doc:`quick-start` guide, or follow the complete :doc:`installation` instructions for production deployment.

Documentation Structure
=======================

This documentation is organized into several main sections:

.. toctree::
   :maxdepth: 2
   :caption: User Documentation

   quick-start
   installation
   user-manual
   faq

.. toctree::
   :maxdepth: 2
   :caption: Developer Resources

   developer-guide
   contributing
   architecture
   api-reference

.. toctree::
   :maxdepth: 2
   :caption: Administration

   admin-guide
   security
   deployment
   troubleshooting

.. toctree::
   :maxdepth: 1
   :caption: Reference

   changelog
   license
   contact

Key Features
============

Patient Management
------------------

* Complete patient registration with photo upload
* Advanced search and filtering capabilities
* Medical history tracking and documentation
* Patient demographics and contact management
* Integration with appointment and billing systems

Doctor Management
-----------------

* Physician profile creation and management
* Specialization and department assignment
* Schedule management and availability tracking
* Performance metrics and workload analysis
* Professional credentials and certification tracking

Appointment System
------------------

* Calendar-based appointment scheduling
* Real-time doctor availability checking
* Appointment status tracking and management
* Conflict prevention and resolution
* Patient appointment history and analytics

Financial Operations
--------------------

* Comprehensive billing and invoice generation
* Multiple payment method support
* Insurance claim management and processing
* Financial reporting and revenue analytics
* Outstanding payment tracking and reminders

Reports & Analytics
-------------------

* Dynamic report generation with custom filters
* Professional PDF reports with watermarked logos
* CSV export for data analysis and integration
* Department-wise and date-range filtering
* Real-time dashboard with key performance indicators

Screenshots
===========

.. image:: _static/images/screenshots/Dashboard.png
   :alt: Main Dashboard
   :align: center
   :width: 80%

*Main Dashboard with real-time statistics and quick access modules*

.. image:: _static/images/screenshots/PatientManagement.png
   :alt: Patient Management
   :align: center
   :width: 80%

*Patient Management interface with search and filtering capabilities*

Support & Community
===================

**Developer Contact:**

* **Dr. Mazharuddin Mohammed**
* **Email:** mazharuddin.mohammed.official@fmail.com
* **Phone:** +91-9347607780
* **Location:** Hyderabad, India
* **GitHub:** `Mazharuddin-Mohammed <https://github.com/Mazharuddin-Mohammed>`_
* **LinkedIn:** `Dr. Mazharuddin Mohammed <https://www.linkedin.com/in/mazharuddin-mohammed>`_

**Community Resources:**

* **GitHub Repository:** `MediSysJava <https://github.com/Mazharuddin-Mohammed/MediSysJava>`_
* **Issue Tracker:** `Report Bugs <https://github.com/Mazharuddin-Mohammed/MediSysJava/issues>`_
* **Discussions:** `GitHub Discussions <https://github.com/Mazharuddin-Mohammed/MediSysJava/discussions>`_
* **Contributing:** See our :doc:`contributing` guide

License
=======

MediSys is released under the MIT License. See the :doc:`license` page for full details.

.. note::
   This project is actively maintained and welcomes contributions from the community. 
   See our :doc:`contributing` guide to get started.

Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`
