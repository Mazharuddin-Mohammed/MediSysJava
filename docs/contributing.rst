Contributing Guide
==================

Welcome to MediSys! We're excited that you're interested in contributing to our healthcare management platform. This guide will help you get started with contributing to the project.

----------
📋 Table of Contents
----------

* `Code of Conduct <#code-of-conduct>`_
* `Getting Started <#getting-started>`_
* `Development Setup <#development-setup>`_
* `Contributing Guidelines <#contributing-guidelines>`_
* `Pull Request Process <#pull-request-process>`_
* `Coding Standards <#coding-standards>`_
* `Testing Guidelines <#testing-guidelines>`_
* `Documentation <#documentation>`_
* `Issue Reporting <#issue-reporting>`_
* `Community <#community>`_

----------
🤝 Code of Conduct
----------

By participating in this project, you agree to abide by our Code of Conduct:

~~~~~~~~~~
Our Pledge
~~~~~~~~~~
* **Be Respectful**: Treat all contributors with respect and kindness
* **Be Inclusive**: Welcome contributors from all backgrounds and experience levels
* **Be Professional**: Maintain professional communication in all interactions
* **Be Constructive**: Provide helpful feedback and suggestions
* **Be Patient**: Help newcomers learn and grow

~~~~~~~~~~
Unacceptable Behavior
~~~~~~~~~~
* Harassment, discrimination, or offensive language
* Personal attacks or trolling
* Publishing private information without permission
* Any conduct that would be inappropriate in a professional setting

----------
🚀 Getting Started
----------

~~~~~~~~~~
Prerequisites
~~~~~~~~~~
* **Java 17+** (OpenJDK or Oracle JDK)
* **Maven 3.8+** for dependency management
* **JavaFX 22+** for GUI components
* **Git** for version control
* **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions

~~~~~~~~~~
Quick Start
~~~~~~~~~~
#. **Fork the repository** on GitHub
#. **Clone your fork** locally:
   .. code-block:: bash

         git clone https://github.com/YOUR_USERNAME/MediSysJava.git
      cd MediSysJava
      .. code-block::

   3. **Set up upstream remote**:
   ````bash
   git remote add upstream https://github.com/Mazharuddin-Mohammed/MediSysJava.git
   `````
#. **Install dependencies**:
   `````bash
   mvn clean install
   `````
#. **Run the application**:
   `````bash
   mvn javafx:run
   `````

----------
🛠️ Development Setup
----------

~~~~~~~~~~
Environment Configuration
~~~~~~~~~~
#. **Java Setup**:
   `````bash
   # Verify Java version
   java -version
   javac -version
   `````

#. **Maven Configuration**:
   `````bash
   # Verify Maven installation
   mvn -version
   `````

#. **IDE Setup**:
   - Import as Maven project
   - Configure Java 17+ as project SDK
   - Enable JavaFX module support
   - Install recommended plugins (Checkstyle, SpotBugs)

~~~~~~~~~~
Database Setup
~~~~~~~~~~
* **H2 Database** (embedded) - No additional setup required
* **PostgreSQL** (optional) - For production deployment
* Database files are automatically created in project directory

~~~~~~~~~~
Running Tests
~~~~~~~~~~
bash
==========
Run all tests
==========
mvn test

==========
Run specific test class
==========
mvn test -Dtest=PatientServiceTest

==========
Run with coverage
==========
mvn test jacoco:report


----------
📝 Contributing Guidelines
----------

~~~~~~~~~~
Types of Contributions
~~~~~~~~~~

#### 🐛 Bug Fixes
* Fix existing functionality issues
* Improve error handling
* Resolve performance problems
* Address security vulnerabilities

#### ✨ New Features
* Patient management enhancements
* Doctor scheduling improvements
* Financial reporting features
* UI/UX improvements
* Integration capabilities

#### 📚 Documentation
* Code documentation improvements
* User guide updates
* API documentation
* Tutorial creation
* Wiki contributions

#### 🧪 Testing
* Unit test additions
* Integration test improvements
* Performance testing
* Security testing
* UI testing

~~~~~~~~~~
Contribution Workflow
~~~~~~~~~~

#. **Check existing issues** before starting work
#. **Create an issue** for new features or significant changes
#. **Discuss the approach** with maintainers
#. **Create a feature branch** from ``master``
#. **Implement your changes** following coding standards
#. **Write/update tests** for your changes
#. **Update documentation** as needed
#. **Submit a pull request** with clear description

~~~~~~~~~~
Branch Naming Convention
~~~~~~~~~~
.. code-block::

      feature/patient-photo-upload
   bugfix/appointment-scheduling-error
   hotfix/security-vulnerability
   docs/contributing-guide
   test/patient-service-coverage


----------
🔄 Pull Request Process
----------

~~~~~~~~~~
Before Submitting
~~~~~~~~~~
* [ ] Code follows project coding standards
* [ ] All tests pass locally
* [ ] New tests added for new functionality
* [ ] Documentation updated
* [ ] No merge conflicts with master branch
* [ ] Commit messages are clear and descriptive

~~~~~~~~~~
PR Template
~~~~~~~~~~
.. code-block:: markdown

      ----------
   Description
   ----------
   Brief description of changes made

----------
Type of Change
----------
* [ ] Bug fix
* [ ] New feature
* [ ] Documentation update
* [ ] Performance improvement
* [ ] Code refactoring

----------
Testing
----------
* [ ] Unit tests added/updated
* [ ] Integration tests pass
* [ ] Manual testing completed

----------
Screenshots (if applicable)
----------
Add screenshots for UI changes

----------
Checklist
----------
* [ ] Code follows style guidelines
* [ ] Self-review completed
* [ ] Documentation updated
* [ ] Tests added/updated


~~~~~~~~~~
Review Process
~~~~~~~~~~
#. **Automated checks** must pass (CI/CD pipeline)
#. **Code review** by at least one maintainer
#. **Testing verification** in development environment
#. **Documentation review** for completeness
#. **Final approval** and merge by maintainer

----------
💻 Coding Standards
----------

~~~~~~~~~~
Java Code Style
~~~~~~~~~~
* **Indentation**: 4 spaces (no tabs)
* **Line length**: Maximum 120 characters
* **Naming conventions**: 
  - Classes: PascalCase (``PatientService``)
  - Methods: camelCase (``findPatientById``)
  - Constants: UPPER_SNAKE_CASE (``MAX_RETRY_ATTEMPTS``)
  - Packages: lowercase (``com.medisys.desktop.service``)

~~~~~~~~~~
Code Quality
~~~~~~~~~~
* **Null safety**: Use Optional where appropriate
* **Exception handling**: Proper try-catch blocks with meaningful messages
* **Resource management**: Use try-with-resources for I/O operations
* **Performance**: Avoid unnecessary object creation in loops
* **Security**: Validate all user inputs

~~~~~~~~~~
JavaFX Guidelines
~~~~~~~~~~
* **FXML usage**: Prefer FXML for complex UI layouts
* **CSS styling**: Use external CSS files for styling
* **Event handling**: Use proper event handling patterns
* **Threading**: Keep UI updates on JavaFX Application Thread

----------
🧪 Testing Guidelines
----------

~~~~~~~~~~
Test Structure
~~~~~~~~~~
.. code-block:: java

      @Test
   @DisplayName("Should find patient by valid ID")
   void shouldFindPatientByValidId() {
       // Given
       Long patientId = 1L;
       Patient expectedPatient = createTestPatient();

       // When
       Optional<Patient.. note::
      result = patientService.findById(patientId);

       // Then
       assertThat(result).isPresent();
       assertThat(result.get().getId()).isEqualTo(patientId);
   }


~~~~~~~~~~
Testing Requirements
~~~~~~~~~~
* **Unit tests**: Minimum 80% code coverage
* **Integration tests**: For service layer interactions
* **UI tests**: For critical user workflows
* **Performance tests**: For data-intensive operations

~~~~~~~~~~
Test Categories
~~~~~~~~~~
* **@UnitTest**: Fast, isolated tests
* **@IntegrationTest**: Database/external service tests
* **@UITest**: JavaFX component tests
* **@PerformanceTest**: Load and stress tests

----------
📖 Documentation
----------

~~~~~~~~~~
Code Documentation
~~~~~~~~~~
* **Javadoc**: All public methods and classes
* **Inline comments**: Complex logic explanation
* **README updates**: For new features or setup changes
* **API documentation**: For service interfaces

~~~~~~~~~~
Documentation Standards
~~~~~~~~~~
.. code-block:: java

      /**
    * Retrieves patient information by ID with medical history.
    * 
    * @param patientId the unique identifier for the patient
    * @param includeMedicalHistory whether to include medical history
    * @return Optional containing patient if found, empty otherwise
    * @throws IllegalArgumentException if patientId is null or negative
    * @since 1.2.0
    */
   public Optional<Patient.. note::
      findPatientById(Long patientId, boolean includeMedicalHistory) {
       // Implementation
   }


----------
🐛 Issue Reporting
----------

~~~~~~~~~~
Bug Reports
~~~~~~~~~~
Use the bug report template:
.. code-block:: markdown

      **Bug Description**
   Clear description of the bug

**Steps to Reproduce**
#. Go to '...'
#. Click on '...'
#. See error

**Expected Behavior**
What should happen

**Actual Behavior**
What actually happens

**Environment**
* OS: [e.g., Windows 10, Ubuntu 20.04]
* Java Version: [e.g., OpenJDK 17]
* Application Version: [e.g., 1.2.0]

**Screenshots**
Add screenshots if applicable


~~~~~~~~~~
Feature Requests
~~~~~~~~~~
Use the feature request template:
.. code-block:: markdown

      **Feature Description**
   Clear description of the proposed feature

**Use Case**
Why is this feature needed?

**Proposed Solution**
How should this feature work?

**Alternatives Considered**
Other approaches you've considered

**Additional Context**
Any other relevant information


----------
👥 Community
----------

~~~~~~~~~~
Communication Channels
~~~~~~~~~~
* **GitHub Issues**: Bug reports and feature requests
* **GitHub Discussions**: General questions and ideas
* **Email**: mazharuddin.mohammed.official@fmail.com
* **LinkedIn**: `Dr. Mazharuddin Mohammed <https://www.linkedin.com/in/mazharuddin-mohammed>`_

~~~~~~~~~~
Getting Help
~~~~~~~~~~
* **Wiki**: Comprehensive documentation and guides
* **Code Examples**: Sample implementations in ``/examples`
* **FAQ**: Common questions and solutions
* **Mentorship**: Available for new contributors

~~~~~~~~~~
Recognition
~~~~~~~~~~
Contributors will be:
* **Listed** in CONTRIBUTORS.md
* **Mentioned** in release notes
* **Featured** in project documentation
* **Invited** to maintainer team (for significant contributions)

----------
📄 License
----------

By contributing to MediSys, you agree that your contributions will be licensed under the same license as the project.

---

----------
🙏 Thank You
----------

Thank you for contributing to MediSys! Your efforts help improve healthcare management systems and make a positive impact on healthcare providers and patients worldwide.

For questions or support, please reach out to:
* **Dr. Mazharuddin Mohammed**
* **Email**: mazharuddin.mohammed.official@fmail.com
* **Location**: Hyderabad, India
* **GitHub**: `Mazharuddin-Mohammed <https://github.com/Mazharuddin-Mohammed>`_
