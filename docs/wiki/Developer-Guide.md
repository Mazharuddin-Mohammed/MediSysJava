# Developer Guide

Welcome to the MediSys Hospital Management System Developer Guide. This comprehensive guide covers development setup, architecture, coding standards, and contribution guidelines.

## 📋 Table of Contents

- [Development Environment](#development-environment)
- [Project Structure](#project-structure)
- [Architecture Overview](#architecture-overview)
- [Development Workflow](#development-workflow)
- [Coding Standards](#coding-standards)
- [Testing Guidelines](#testing-guidelines)
- [Database Development](#database-development)
- [UI Development](#ui-development)
- [API Development](#api-development)
- [Deployment](#deployment)

## 🛠️ Development Environment

### Prerequisites
- **Java 17+** (OpenJDK or Oracle JDK)
- **Maven 3.8+** for dependency management
- **Git** for version control
- **IDE**: IntelliJ IDEA (recommended), Eclipse, or VS Code

### IDE Setup

#### IntelliJ IDEA (Recommended)
1. **Import Project**:
   - File → Open → Select `pom.xml`
   - Import as Maven project
2. **Configure SDK**:
   - File → Project Structure → Project SDK → Java 17+
3. **Enable Plugins**:
   - JavaFX
   - Maven Helper
   - Checkstyle-IDEA
   - SonarLint
4. **Code Style**:
   - Import `config/intellij-codestyle.xml`

#### VS Code Setup
1. **Install Extensions**:
   - Extension Pack for Java
   - JavaFX Support
   - Maven for Java
   - Checkstyle for Java
2. **Configure Settings**:
   ```json
   {
     "java.home": "/path/to/java17",
     "maven.executable.path": "/path/to/maven/bin/mvn",
     "java.format.settings.url": "config/eclipse-formatter.xml"
   }
   ```

### Environment Variables
```bash
# Java Configuration
export JAVA_HOME=/path/to/java17
export PATH=$JAVA_HOME/bin:$PATH

# Maven Configuration
export MAVEN_HOME=/path/to/maven
export PATH=$MAVEN_HOME/bin:$PATH

# JavaFX Configuration (if needed)
export JAVAFX_HOME=/path/to/javafx
export PATH_TO_FX=$JAVAFX_HOME/lib
```

## 📁 Project Structure

```
MediSysJava/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/medisys/desktop/
│   │   │       ├── MediSysApp.java          # Main application class
│   │   │       ├── config/                  # Configuration classes
│   │   │       ├── controller/              # JavaFX controllers
│   │   │       ├── model/                   # Data models/entities
│   │   │       ├── service/                 # Business logic services
│   │   │       ├── repository/              # Data access layer
│   │   │       ├── ui/                      # UI components
│   │   │       │   ├── modules/             # Feature modules
│   │   │       │   └── components/          # Reusable components
│   │   │       └── utils/                   # Utility classes
│   │   └── resources/
│   │       ├── css/                         # Stylesheets
│   │       ├── fxml/                        # FXML layouts
│   │       ├── images/                      # Image resources
│   │       └── application.properties       # Configuration
│   └── test/
│       ├── java/                            # Test classes
│       └── resources/                       # Test resources
├── docs/                                    # Documentation
├── config/                                  # Configuration files
├── scripts/                                 # Build/deployment scripts
├── pom.xml                                  # Maven configuration
├── README.md                                # Project overview
├── CONTRIBUTING.md                          # Contribution guidelines
└── .gitignore                              # Git ignore rules
```

### Key Directories

#### `/src/main/java/com/medisys/desktop/`
- **`model/`**: Entity classes (Patient, Doctor, Appointment, etc.)
- **`service/`**: Business logic and data processing
- **`repository/`**: Data access and persistence
- **`controller/`**: JavaFX FXML controllers
- **`ui/modules/`**: Feature-specific UI modules
- **`utils/`**: Helper classes and utilities

#### `/src/main/resources/`
- **`css/`**: Application stylesheets
- **`fxml/`**: JavaFX layout files
- **`images/`**: Icons, logos, and graphics
- **`application.properties`**: Configuration settings

## 🏗️ Architecture Overview

### Design Patterns

#### Model-View-Controller (MVC)
```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│    View     │    │ Controller  │    │    Model    │
│   (FXML)    │◄──►│   (Java)    │◄──►│   (Entity)  │
│             │    │             │    │             │
└─────────────┘    └─────────────┘    └─────────────┘
```

#### Service Layer Pattern
```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ Controller  │    │   Service   │    │ Repository  │
│             │◄──►│   Layer     │◄──►│    Layer    │
│             │    │             │    │             │
└─────────────┘    └─────────────┘    └─────────────┘
```

### Layer Responsibilities

#### Presentation Layer (UI)
- **JavaFX Controllers**: Handle user interactions
- **FXML Files**: Define UI layouts
- **CSS Stylesheets**: Visual styling
- **UI Modules**: Feature-specific interfaces

#### Business Layer (Service)
- **Service Classes**: Business logic implementation
- **Validation**: Data validation and business rules
- **Processing**: Data transformation and calculations
- **Integration**: External service integration

#### Data Layer (Repository)
- **Repository Classes**: Data access abstraction
- **Entity Classes**: Data models
- **Database Operations**: CRUD operations
- **Query Methods**: Custom data retrieval

### Technology Stack

#### Frontend
- **JavaFX 22+**: UI framework
- **FXML**: Declarative UI layouts
- **CSS**: Styling and theming
- **Scene Builder**: Visual FXML editor

#### Backend
- **Java 17+**: Core language
- **Maven**: Build and dependency management
- **H2 Database**: Embedded database
- **JPA/Hibernate**: ORM framework

#### Libraries
- **Apache PDFBox**: PDF generation
- **Jackson**: JSON processing
- **SLF4J**: Logging framework
- **JUnit 5**: Testing framework

## 🔄 Development Workflow

### Git Workflow

#### Branch Strategy
```
master (main)
├── develop
│   ├── feature/patient-management
│   ├── feature/appointment-system
│   └── feature/reporting-module
├── hotfix/security-patch
└── release/v1.1.0
```

#### Commit Convention
```
type(scope): description

feat(patient): add photo upload functionality
fix(appointment): resolve scheduling conflict issue
docs(readme): update installation instructions
style(ui): improve button styling consistency
refactor(service): optimize patient search algorithm
test(patient): add unit tests for patient service
```

### Development Process

#### 1. Setup Development Branch
```bash
# Create feature branch
git checkout -b feature/new-feature-name

# Keep branch updated
git pull origin develop
git merge develop
```

#### 2. Development Cycle
```bash
# Make changes
# Write tests
# Run tests
mvn test

# Check code style
mvn checkstyle:check

# Commit changes
git add .
git commit -m "feat(module): add new feature"
```

#### 3. Code Review Process
```bash
# Push branch
git push origin feature/new-feature-name

# Create pull request
# Address review comments
# Merge after approval
```

## 💻 Coding Standards

### Java Code Style

#### Naming Conventions
```java
// Classes: PascalCase
public class PatientService { }

// Methods: camelCase
public Patient findPatientById(Long id) { }

// Variables: camelCase
private String patientName;

// Constants: UPPER_SNAKE_CASE
private static final int MAX_RETRY_ATTEMPTS = 3;

// Packages: lowercase
package com.medisys.desktop.service;
```

#### Code Structure
```java
/**
 * Service class for managing patient operations.
 * 
 * @author Dr. Mazharuddin Mohammed
 * @version 1.0.0
 * @since 2025-01-01
 */
@Service
public class PatientService {
    
    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);
    
    private final PatientRepository patientRepository;
    
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    
    /**
     * Finds a patient by their unique identifier.
     * 
     * @param id the patient ID
     * @return Optional containing the patient if found
     * @throws IllegalArgumentException if id is null or negative
     */
    public Optional<Patient> findById(Long id) {
        validateId(id);
        
        try {
            return patientRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error finding patient with id: {}", id, e);
            throw new ServiceException("Failed to find patient", e);
        }
    }
    
    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Patient ID must be positive");
        }
    }
}
```

### JavaFX Guidelines

#### Controller Structure
```java
@Component
public class PatientController implements Initializable {
    
    @FXML private TableView<Patient> patientTable;
    @FXML private TextField searchField;
    @FXML private Button addButton;
    
    private final PatientService patientService;
    private final ObservableList<Patient> patients = FXCollections.observableArrayList();
    
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        setupEventHandlers();
        loadPatients();
    }
    
    @FXML
    private void handleAddPatient() {
        // Event handler implementation
    }
    
    private void setupTable() {
        // Table setup logic
    }
}
```

#### FXML Best Practices
```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<VBox xmlns="http://javafx.com/javafx/11.0.1" 
      xmlns:fx="http://javafx.com/fxml/1" 
      fx:controller="com.medisys.desktop.controller.PatientController"
      styleClass="main-container">
    
    <HBox styleClass="toolbar">
        <TextField fx:id="searchField" promptText="Search patients..." />
        <Button fx:id="addButton" text="Add Patient" onAction="#handleAddPatient" />
    </HBox>
    
    <TableView fx:id="patientTable" VBox.vgrow="ALWAYS">
        <columns>
            <TableColumn fx:id="nameColumn" text="Name" />
            <TableColumn fx:id="phoneColumn" text="Phone" />
        </columns>
    </TableView>
    
</VBox>
```

## 🧪 Testing Guidelines

### Test Structure

#### Unit Tests
```java
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    
    @Mock
    private PatientRepository patientRepository;
    
    @InjectMocks
    private PatientService patientService;
    
    @Test
    @DisplayName("Should find patient by valid ID")
    void shouldFindPatientByValidId() {
        // Given
        Long patientId = 1L;
        Patient expectedPatient = createTestPatient();
        when(patientRepository.findById(patientId))
            .thenReturn(Optional.of(expectedPatient));
        
        // When
        Optional<Patient> result = patientService.findById(patientId);
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(patientId);
        verify(patientRepository).findById(patientId);
    }
    
    @Test
    @DisplayName("Should throw exception for null ID")
    void shouldThrowExceptionForNullId() {
        // When & Then
        assertThatThrownBy(() -> patientService.findById(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Patient ID must be positive");
    }
    
    private Patient createTestPatient() {
        return Patient.builder()
            .id(1L)
            .name("John Doe")
            .email("john@example.com")
            .build();
    }
}
```

#### Integration Tests
```java
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class PatientServiceIntegrationTest {
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    @Transactional
    void shouldSaveAndRetrievePatient() {
        // Given
        Patient patient = createTestPatient();
        
        // When
        Patient savedPatient = patientService.save(patient);
        entityManager.flush();
        
        Optional<Patient> retrievedPatient = patientService.findById(savedPatient.getId());
        
        // Then
        assertThat(retrievedPatient).isPresent();
        assertThat(retrievedPatient.get().getName()).isEqualTo(patient.getName());
    }
}
```

### Test Categories
```java
// Unit Tests - Fast, isolated
@Tag("unit")
class PatientServiceTest { }

// Integration Tests - Database interactions
@Tag("integration")
class PatientRepositoryTest { }

// UI Tests - JavaFX components
@Tag("ui")
class PatientControllerTest { }

// Performance Tests - Load testing
@Tag("performance")
class PatientServicePerformanceTest { }
```

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test category
mvn test -Dgroups="unit"
mvn test -Dgroups="integration"

# Run with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=PatientServiceTest

# Run specific test method
mvn test -Dtest=PatientServiceTest#shouldFindPatientByValidId
```

## 🗄️ Database Development

### Entity Design
```java
@Entity
@Table(name = "patients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(unique = true, length = 50)
    private String email;
    
    @Column(length = 20)
    private String phone;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();
}
```

### Repository Pattern
```java
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    Optional<Patient> findByEmail(String email);
    
    List<Patient> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT p FROM Patient p WHERE p.dateOfBirth BETWEEN :startDate AND :endDate")
    List<Patient> findByDateOfBirthBetween(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Modifying
    @Query("UPDATE Patient p SET p.active = false WHERE p.id = :id")
    void deactivatePatient(@Param("id") Long id);
}
```

## 🎨 UI Development

### CSS Styling
```css
/* Main application styles */
.main-container {
    -fx-background-color: #f8f9fa;
    -fx-padding: 20px;
}

.toolbar {
    -fx-spacing: 10px;
    -fx-padding: 10px 0;
    -fx-alignment: center-left;
}

.primary-button {
    -fx-background-color: #2563eb;
    -fx-text-fill: white;
    -fx-font-weight: bold;
    -fx-padding: 8px 16px;
    -fx-background-radius: 4px;
}

.primary-button:hover {
    -fx-background-color: #1d4ed8;
}

.data-table {
    -fx-background-color: white;
    -fx-border-color: #e5e7eb;
    -fx-border-radius: 4px;
}
```

### Component Development
```java
public class PatientCard extends VBox {
    
    private final Patient patient;
    private final Label nameLabel;
    private final Label phoneLabel;
    private final ImageView photoView;
    
    public PatientCard(Patient patient) {
        this.patient = patient;
        this.nameLabel = new Label();
        this.phoneLabel = new Label();
        this.photoView = new ImageView();
        
        initializeComponents();
        setupLayout();
        updateData();
    }
    
    private void initializeComponents() {
        getStyleClass().add("patient-card");
        setSpacing(8);
        setPadding(new Insets(12));
    }
    
    private void setupLayout() {
        HBox header = new HBox(8);
        header.getChildren().addAll(photoView, createInfoBox());
        getChildren().add(header);
    }
    
    private VBox createInfoBox() {
        VBox infoBox = new VBox(4);
        nameLabel.getStyleClass().add("patient-name");
        phoneLabel.getStyleClass().add("patient-phone");
        infoBox.getChildren().addAll(nameLabel, phoneLabel);
        return infoBox;
    }
    
    private void updateData() {
        nameLabel.setText(patient.getName());
        phoneLabel.setText(patient.getPhone());
        // Load and set photo
    }
}
```

## 🚀 Deployment

### Building for Production
```bash
# Clean and package
mvn clean package

# Create executable JAR
mvn package -Dmaven.test.skip=true

# Build with all dependencies
mvn clean compile assembly:single
```

### Docker Deployment
```dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app
COPY target/medisys-1.0.0.jar app.jar
COPY src/main/resources/application-prod.properties application.properties

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
```

### Environment Configuration
```properties
# Production settings
spring.profiles.active=production
spring.datasource.url=jdbc:postgresql://localhost:5432/medisys_prod
logging.level.com.medisys=INFO
server.port=8080
```

## 📚 Additional Resources

### Documentation
- **[Architecture Overview](Architecture-Overview.md)** - Detailed system architecture
- **[API Documentation](API-Documentation.md)** - Service interfaces
- **[Testing Guide](Testing-Guide.md)** - Comprehensive testing strategies
- **[Code Style Guide](Code-Style-Guide.md)** - Detailed coding standards

### Tools & Utilities
- **Scene Builder**: Visual FXML editor
- **Maven Helper**: IntelliJ plugin for Maven
- **Checkstyle**: Code style verification
- **SpotBugs**: Static analysis tool
- **JaCoCo**: Code coverage analysis

### External Resources
- **[JavaFX Documentation](https://openjfx.io/)**
- **[Maven Documentation](https://maven.apache.org/)**
- **[JUnit 5 Documentation](https://junit.org/junit5/)**
- **[Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)**

---

## 📞 Developer Support

For development questions and support:

- **📧 Email**: mazharuddin.mohammed.official@fmail.com
- **🐛 GitHub Issues**: [Development Issues](https://github.com/Mazharuddin-Mohammed/MediSysJava/issues)
- **💬 Discussions**: [GitHub Discussions](https://github.com/Mazharuddin-Mohammed/MediSysJava/discussions)
- **📱 Phone**: +91-9347607780

---

*Developer Guide - Last updated: June 2025 | Version 1.0.0*
