#!/usr/bin/env python3
"""
Convert Markdown documentation files to reStructuredText for Sphinx.
This script processes the wiki markdown files and creates corresponding RST files.
"""

import os
import re
import shutil
from pathlib import Path

def convert_markdown_to_rst(md_content, title):
    """Convert markdown content to reStructuredText format."""
    
    # Convert title
    rst_content = f"{title}\n{'=' * len(title)}\n\n"
    
    # Remove the first markdown title (we already added it)
    md_content = re.sub(r'^# .*\n\n?', '', md_content, count=1)
    
    # Convert headers
    md_content = re.sub(r'^### (.*)', r'~~~~~~~~~~\n\1\n~~~~~~~~~~', md_content, flags=re.MULTILINE)
    md_content = re.sub(r'^## (.*)', r'----------\n\1\n----------', md_content, flags=re.MULTILINE)
    md_content = re.sub(r'^# (.*)', r'==========\n\1\n==========', md_content, flags=re.MULTILINE)
    
    # Convert code blocks
    md_content = re.sub(r'```(\w+)?\n(.*?)\n```', r'.. code-block:: \1\n\n\2\n', md_content, flags=re.DOTALL)
    md_content = re.sub(r'```\n(.*?)\n```', r'.. code-block::\n\n\1\n', md_content, flags=re.DOTALL)
    
    # Convert inline code
    md_content = re.sub(r'`([^`]+)`', r'``\1``', md_content)
    
    # Convert links
    md_content = re.sub(r'\[([^\]]+)\]\(([^)]+)\)', r'`\1 <\2>`_', md_content)
    
    # Convert bold text
    md_content = re.sub(r'\*\*(.*?)\*\*', r'**\1**', md_content)
    
    # Convert italic text
    md_content = re.sub(r'\*(.*?)\*', r'*\1*', md_content)
    
    # Convert lists
    md_content = re.sub(r'^- (.*)', r'* \1', md_content, flags=re.MULTILINE)
    md_content = re.sub(r'^\d+\. (.*)', r'#. \1', md_content, flags=re.MULTILINE)
    
    # Convert notes and warnings
    md_content = re.sub(r'> \*\*(.*?)\*\*', r'.. note::\n   \1', md_content)
    md_content = re.sub(r'> (.*)', r'.. note::\n   \1', md_content)
    
    # Convert images
    md_content = re.sub(r'!\[([^\]]*)\]\(([^)]+)\)', r'.. image:: \2\n   :alt: \1', md_content)
    
    # Add proper spacing for code blocks
    md_content = re.sub(r'(\.\. code-block::.*?\n\n)(.*?)(\n\n)', 
                       lambda m: m.group(1) + '\n'.join('   ' + line for line in m.group(2).split('\n')) + m.group(3),
                       md_content, flags=re.DOTALL)
    
    rst_content += md_content
    
    return rst_content

def create_rst_files():
    """Create RST files from markdown documentation."""
    
    # Define the mapping of markdown files to RST files
    file_mappings = {
        '../docs/wiki/Quick-Start.md': ('quick-start.rst', 'Quick Start Guide'),
        '../docs/wiki/Installation-Guide.md': ('installation.rst', 'Installation Guide'),
        '../docs/wiki/User-Manual.md': ('user-manual.rst', 'User Manual'),
        '../docs/wiki/Developer-Guide.md': ('developer-guide.rst', 'Developer Guide'),
        '../docs/wiki/FAQ.md': ('faq.rst', 'Frequently Asked Questions'),
        '../CONTRIBUTING.md': ('contributing.rst', 'Contributing Guide'),
        '../README.md': ('readme.rst', 'Project Overview'),
    }
    
    for md_file, (rst_file, title) in file_mappings.items():
        if os.path.exists(md_file):
            print(f"Converting {md_file} to {rst_file}")
            
            with open(md_file, 'r', encoding='utf-8') as f:
                md_content = f.read()
            
            rst_content = convert_markdown_to_rst(md_content, title)

            # Ensure the directory exists (only if rst_file has a directory)
            rst_dir = os.path.dirname(rst_file)
            if rst_dir:
                os.makedirs(rst_dir, exist_ok=True)

            with open(rst_file, 'w', encoding='utf-8') as f:
                f.write(rst_content)
            
            print(f"✅ Created {rst_file}")
        else:
            print(f"⚠️  Warning: {md_file} not found")

def create_additional_rst_files():
    """Create additional RST files for complete documentation."""
    
    # Architecture documentation
    architecture_content = """System Architecture
==================

MediSys follows a modern, layered architecture pattern designed for scalability, maintainability, and performance.

Architecture Overview
---------------------

.. code-block:: text

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

Technology Stack
----------------

Frontend
~~~~~~~~

* **JavaFX 22**: Modern desktop UI framework
* **FXML**: Declarative UI layouts
* **CSS**: Professional styling and theming
* **Scene Builder**: Visual FXML editor

Backend
~~~~~~~

* **Spring Boot 3.3.0**: Enterprise application framework
* **Spring Security**: Authentication and authorization
* **Spring Data JPA**: Data persistence layer
* **HikariCP**: High-performance connection pooling

Database
~~~~~~~~

* **PostgreSQL 16.9**: Primary production database
* **H2**: Embedded database for development
* **Redis**: Caching and session management
* **Flyway**: Database migration management

Design Patterns
---------------

Model-View-Controller (MVC)
~~~~~~~~~~~~~~~~~~~~~~~~~~~

The application follows the MVC pattern with clear separation of concerns:

* **Model**: Entity classes representing business data
* **View**: JavaFX FXML files defining the user interface
* **Controller**: Java classes handling user interactions and business logic

Service Layer Pattern
~~~~~~~~~~~~~~~~~~~~~

Business logic is encapsulated in service classes that provide:

* **Data validation**: Input validation and business rules
* **Transaction management**: Database transaction handling
* **Business operations**: Core healthcare management operations
* **Integration**: External service integration points

Repository Pattern
~~~~~~~~~~~~~~~~~~

Data access is abstracted through repository interfaces:

* **CRUD operations**: Standard create, read, update, delete operations
* **Custom queries**: Specialized data retrieval methods
* **Database abstraction**: Independence from specific database implementations
* **Testing support**: Easy mocking for unit tests
"""
    
    with open('docs/architecture.rst', 'w', encoding='utf-8') as f:
        f.write(architecture_content)
    
    # API Reference
    api_content = """API Reference
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
"""
    
    with open('docs/api-reference.rst', 'w', encoding='utf-8') as f:
        f.write(api_content)
    
    # Additional documentation files
    additional_files = {
        'docs/admin-guide.rst': 'Administration Guide',
        'docs/security.rst': 'Security Configuration',
        'docs/deployment.rst': 'Deployment Guide',
        'docs/troubleshooting.rst': 'Troubleshooting',
        'docs/changelog.rst': 'Changelog',
        'docs/license.rst': 'License',
        'docs/contact.rst': 'Contact Information'
    }
    
    for file_path, title in additional_files.items():
        if not os.path.exists(file_path):
            content = f"""{title}
{'=' * len(title)}

This section is under development. Please check back later for updates.

For immediate assistance, please contact:

* **Email**: mazharuddin.mohammed.official@fmail.com
* **Phone**: +91-9347607780
* **GitHub**: `Issues <https://github.com/Mazharuddin-Mohammed/MediSysJava/issues>`_
"""
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"✅ Created placeholder {file_path}")

def copy_static_files():
    """Copy static files for documentation."""
    
    # Create static directories
    os.makedirs('_static/css', exist_ok=True)
    os.makedirs('_static/js', exist_ok=True)
    os.makedirs('_static/images', exist_ok=True)
    
    # Copy images if they exist
    image_sources = [
        '../src/main/resources/images/banner.jpg',
        '../src/main/resources/images/logo.jpg',
        '../src/main/resources/images/screenshots/'
    ]
    
    for source in image_sources:
        if os.path.exists(source):
            if os.path.isfile(source):
                shutil.copy2(source, '_static/images/')
                print(f"✅ Copied {source}")
            elif os.path.isdir(source):
                for file in os.listdir(source):
                    if file.lower().endswith(('.png', '.jpg', '.jpeg', '.gif')):
                        shutil.copy2(os.path.join(source, file), '_static/images/')
                        print(f"✅ Copied {os.path.join(source, file)}")
    
    # Create custom CSS
    custom_css = """/* Custom styles for MediSys documentation */

.wy-nav-content {
    max-width: 1200px;
}

.rst-content .admonition {
    margin: 24px 0;
}

.rst-content .admonition.note {
    background-color: #e7f3ff;
    border-left: 4px solid #2563eb;
}

.rst-content .admonition.warning {
    background-color: #fff3cd;
    border-left: 4px solid #ffc107;
}

.rst-content img {
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.wy-side-nav-search {
    background-color: #2563eb;
}

.wy-side-nav-search input[type=text] {
    border-radius: 4px;
}

.rst-content code.literal {
    background-color: #f8f9fa;
    border: 1px solid #e9ecef;
    border-radius: 3px;
    padding: 2px 4px;
}
"""
    
    with open('_static/css/custom.css', 'w', encoding='utf-8') as f:
        f.write(custom_css)
    
    # Create custom JavaScript
    custom_js = """// Custom JavaScript for MediSys documentation

document.addEventListener('DOMContentLoaded', function() {
    // Add copy button functionality
    const codeBlocks = document.querySelectorAll('pre');
    codeBlocks.forEach(function(block) {
        const button = document.createElement('button');
        button.className = 'copy-button';
        button.textContent = 'Copy';
        button.addEventListener('click', function() {
            navigator.clipboard.writeText(block.textContent);
            button.textContent = 'Copied!';
            setTimeout(function() {
                button.textContent = 'Copy';
            }, 2000);
        });
        block.appendChild(button);
    });
});
"""
    
    with open('_static/js/custom.js', 'w', encoding='utf-8') as f:
        f.write(custom_js)
    
    print("✅ Created custom CSS and JavaScript files")

def main():
    """Main function to convert all documentation."""
    print("🚀 Converting MediSys documentation to Sphinx format...")
    
    # Convert markdown files to RST
    create_rst_files()
    
    # Create additional RST files
    create_additional_rst_files()
    
    # Copy static files
    copy_static_files()
    
    print("✅ Documentation conversion completed!")
    print("📚 Ready for Read the Docs build")

if __name__ == '__main__':
    main()
