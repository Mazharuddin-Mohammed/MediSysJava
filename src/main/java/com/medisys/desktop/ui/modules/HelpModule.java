package com.medisys.desktop.ui.modules;

import com.medisys.desktop.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Comprehensive Help Module with detailed guidance for each module
 */
public class HelpModule {

    private final User currentUser;
    private final VBox root;
    private TreeView<String> helpTree;
    private ScrollPane contentScrollPane;
    private VBox contentArea;

    public HelpModule(User currentUser) {
        this.currentUser = currentUser;
        this.root = new VBox(20);
        
        initializeUI();
    }

    private void initializeUI() {
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        // Header
        HBox header = createHeader();

        // Main content area
        HBox mainContent = createMainContent();

        root.getChildren().addAll(header, mainContent);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);

        Text title = new Text("❓ Help & Documentation");
        title.getStyleClass().add("header-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button searchBtn = new Button("🔍 Search Help");
        searchBtn.getStyleClass().add("secondary-button");
        searchBtn.setOnAction(e -> showSearchDialog());

        Button contactBtn = new Button("📞 Contact Support");
        contactBtn.getStyleClass().add("modern-button");
        contactBtn.setOnAction(e -> showContactDialog());

        header.getChildren().addAll(title, spacer, searchBtn, contactBtn);

        return header;
    }

    private HBox createMainContent() {
        HBox mainContent = new HBox(20);
        mainContent.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        // Left panel - Help navigation tree
        VBox leftPanel = createHelpNavigation();
        leftPanel.setPrefWidth(300);

        // Right panel - Help content
        VBox rightPanel = createHelpContent();
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        mainContent.getChildren().addAll(leftPanel, rightPanel);

        return mainContent;
    }

    private VBox createHelpNavigation() {
        VBox navigation = new VBox(10);
        navigation.getStyleClass().add("modern-card");
        navigation.setPadding(new Insets(15));

        Text navTitle = new Text("📚 Help Topics");
        navTitle.getStyleClass().add("section-title");

        // Create help tree
        TreeItem<String> rootItem = new TreeItem<>("MediSys Help");
        rootItem.setExpanded(true);

        // Getting Started
        TreeItem<String> gettingStarted = new TreeItem<>("🚀 Getting Started");
        gettingStarted.getChildren().addAll(
            new TreeItem<>("System Overview"),
            new TreeItem<>("First Login"),
            new TreeItem<>("User Roles & Permissions"),
            new TreeItem<>("Navigation Basics")
        );

        // Dashboard
        TreeItem<String> dashboard = new TreeItem<>("🏠 Dashboard");
        dashboard.getChildren().addAll(
            new TreeItem<>("Dashboard Overview"),
            new TreeItem<>("Statistics Cards"),
            new TreeItem<>("Quick Actions"),
            new TreeItem<>("Recent Activities")
        );

        // Patients Module
        TreeItem<String> patients = new TreeItem<>("👥 Patients Management");
        patients.getChildren().addAll(
            new TreeItem<>("Adding New Patients"),
            new TreeItem<>("Patient Registration Form"),
            new TreeItem<>("Photo Upload"),
            new TreeItem<>("Searching Patients"),
            new TreeItem<>("Editing Patient Information"),
            new TreeItem<>("Patient Medical History"),
            new TreeItem<>("Patient Status Management")
        );

        // Doctors Module
        TreeItem<String> doctors = new TreeItem<>("👨‍⚕️ Doctors Management");
        doctors.getChildren().addAll(
            new TreeItem<>("Adding New Doctors"),
            new TreeItem<>("Doctor Registration Form"),
            new TreeItem<>("Professional Information"),
            new TreeItem<>("Specializations & Departments"),
            new TreeItem<>("Doctor Schedules"),
            new TreeItem<>("Doctor Photo Upload"),
            new TreeItem<>("Managing Doctor Status")
        );

        // Appointments Module
        TreeItem<String> appointments = new TreeItem<>("📅 Appointments");
        appointments.getChildren().addAll(
            new TreeItem<>("Scheduling Appointments"),
            new TreeItem<>("Calendar View"),
            new TreeItem<>("Appointment Status"),
            new TreeItem<>("Rescheduling & Cancellation"),
            new TreeItem<>("Doctor Availability"),
            new TreeItem<>("Patient Appointment History")
        );

        // Finance Module
        TreeItem<String> finance = new TreeItem<>("💰 Finance Management");
        finance.getChildren().addAll(
            new TreeItem<>("Creating Bills"),
            new TreeItem<>("Payment Processing"),
            new TreeItem<>("Financial Reports"),
            new TreeItem<>("Insurance Claims"),
            new TreeItem<>("Outstanding Payments"),
            new TreeItem<>("Revenue Analytics")
        );

        // Reports Module
        TreeItem<String> reports = new TreeItem<>("📊 Reports & Analytics");
        reports.getChildren().addAll(
            new TreeItem<>("Generating Reports"),
            new TreeItem<>("PDF Export"),
            new TreeItem<>("CSV Export"),
            new TreeItem<>("Date Range Filtering"),
            new TreeItem<>("Department-wise Reports"),
            new TreeItem<>("Custom Report Templates")
        );

        // System Administration
        TreeItem<String> admin = new TreeItem<>("⚙️ System Administration");
        admin.getChildren().addAll(
            new TreeItem<>("User Management"),
            new TreeItem<>("Role-based Access Control"),
            new TreeItem<>("System Settings"),
            new TreeItem<>("Data Backup"),
            new TreeItem<>("Security Settings"),
            new TreeItem<>("Audit Logs")
        );

        // Troubleshooting
        TreeItem<String> troubleshooting = new TreeItem<>("🔧 Troubleshooting");
        troubleshooting.getChildren().addAll(
            new TreeItem<>("Common Issues"),
            new TreeItem<>("Error Messages"),
            new TreeItem<>("Performance Issues"),
            new TreeItem<>("Login Problems"),
            new TreeItem<>("Data Recovery"),
            new TreeItem<>("System Maintenance")
        );

        rootItem.getChildren().addAll(
            gettingStarted, dashboard, patients, doctors, 
            appointments, finance, reports, admin, troubleshooting
        );

        helpTree = new TreeView<>(rootItem);
        helpTree.getStyleClass().add("modern-tree-view");
        helpTree.setPrefHeight(500);

        // Handle tree selection
        helpTree.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showHelpContent(newVal.getValue());
            }
        });

        navigation.getChildren().addAll(navTitle, helpTree);

        return navigation;
    }

    private VBox createHelpContent() {
        VBox content = new VBox(15);
        content.getStyleClass().add("modern-card");
        content.setPadding(new Insets(20));

        Text contentTitle = new Text("📖 Help Content");
        contentTitle.getStyleClass().add("section-title");

        // Content area
        contentScrollPane = new ScrollPane();
        contentScrollPane.getStyleClass().add("modern-scroll-pane");
        contentScrollPane.setPrefHeight(500);
        contentScrollPane.setFitToWidth(true);

        contentArea = new VBox(15);
        contentArea.setPadding(new Insets(20));

        // Welcome content
        Text welcomeTitle = new Text("Welcome to MediSys Help");
        welcomeTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #2C3E50;");

        Text welcomeText = new Text(
            "Welcome to the MediSys Hospital Management System help documentation. " +
            "This comprehensive guide will help you navigate and use all features of the system effectively.\n\n" +
            "Select a topic from the navigation tree on the left to get detailed information about specific features and modules."
        );
        welcomeText.setStyle("-fx-font-size: 14px; -fx-fill: #34495E; -fx-text-alignment: justify;");
        welcomeText.setWrappingWidth(600);

        contentArea.getChildren().addAll(welcomeTitle, welcomeText);
        contentScrollPane.setContent(contentArea);

        content.getChildren().addAll(contentTitle, contentScrollPane);

        return content;
    }

    private void showHelpContent(String topic) {
        if (contentArea != null && contentScrollPane != null) {
            // Clear existing content
            contentArea.getChildren().clear();

            // Create new content
            Text title = new Text(topic);
            title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #2C3E50;");

            Text content = new Text(getHelpContentForTopic(topic));
            content.setStyle("-fx-font-size: 14px; -fx-fill: #34495E; -fx-text-alignment: justify;");
            content.setWrappingWidth(600);

            // Add content to the area
            contentArea.getChildren().addAll(title, content);

            // Scroll to top
            contentScrollPane.setVvalue(0);
        }
    }

    private String getHelpContentForTopic(String topic) {
        switch (topic) {
            case "System Overview":
                return "MediSys is a comprehensive Hospital Management System designed to streamline healthcare operations. " +
                       "The system includes modules for patient management, doctor management, appointments, finance, and reporting. " +
                       "Each module is designed with a modern, user-friendly interface that follows enterprise-grade design principles.\n\n" +
                       "Key Features:\n" +
                       "• Patient registration and management\n" +
                       "• Doctor profiles and scheduling\n" +
                       "• Appointment booking and management\n" +
                       "• Financial operations and billing\n" +
                       "• Comprehensive reporting and analytics\n" +
                       "• Role-based access control\n" +
                       "• Professional photo upload capabilities";

            case "Adding New Patients":
                return "To add a new patient to the system:\n\n" +
                       "1. Navigate to the Patients module from the main dashboard\n" +
                       "2. Click the '+ Add New Patient' button in the top-right corner\n" +
                       "3. Fill in the patient registration form with the following information:\n" +
                       "   • Personal Information: First name, last name, date of birth\n" +
                       "   • Contact Details: Phone number, email address, address\n" +
                       "   • Medical Information: Blood group, allergies, emergency contact\n" +
                       "   • Insurance Information: Insurance provider, policy number\n" +
                       "4. Upload a patient photo using the 'Upload Photo' button\n" +
                       "5. Review all information for accuracy\n" +
                       "6. Click 'Save' to add the patient to the system\n\n" +
                       "The patient will be assigned a unique Patient ID automatically.";

            case "Adding New Doctors":
                return "To add a new doctor to the system:\n\n" +
                       "1. Navigate to the Doctors module from the main dashboard\n" +
                       "2. Click the '+ Add New Doctor' button\n" +
                       "3. Complete the comprehensive doctor registration form:\n\n" +
                       "Personal Information:\n" +
                       "• First Name and Last Name\n" +
                       "• Email Address and Phone Number\n" +
                       "• Professional photo upload\n\n" +
                       "Professional Information:\n" +
                       "• Doctor ID (unique identifier)\n" +
                       "• Medical Specialization\n" +
                       "• Department Assignment\n" +
                       "• Qualifications (MBBS, MD, etc.)\n" +
                       "• Years of Experience\n" +
                       "• Joining Date\n" +
                       "• Current Status (Active/Inactive/On Leave)\n\n" +
                       "4. Review all information carefully\n" +
                       "5. Click 'OK' to save the doctor profile\n\n" +
                       "The doctor will be immediately available for appointment scheduling.";

            case "Photo Upload":
                return "The system supports photo upload for both patients and doctors:\n\n" +
                       "Supported Formats:\n" +
                       "• PNG (.png)\n" +
                       "• JPEG (.jpg, .jpeg)\n" +
                       "• GIF (.gif)\n\n" +
                       "Upload Process:\n" +
                       "1. Click the 'Upload Photo' button in the registration form\n" +
                       "2. Select an image file from your computer\n" +
                       "3. The photo will be automatically resized and displayed as a preview\n" +
                       "4. If no photo is uploaded, a default placeholder will be used\n\n" +
                       "Best Practices:\n" +
                       "• Use clear, professional headshots\n" +
                       "• Ensure good lighting and image quality\n" +
                       "• Recommended size: 200x200 pixels or larger\n" +
                       "• File size should be under 5MB for optimal performance";

            case "Scheduling Appointments":
                return "To schedule a new appointment:\n\n" +
                       "1. Go to the Appointments module\n" +
                       "2. Click '+ Schedule Appointment'\n" +
                       "3. Select the patient from the dropdown list\n" +
                       "4. Choose the doctor based on specialization\n" +
                       "5. Pick an available date and time slot\n" +
                       "6. Select appointment type (Consultation, Follow-up, Surgery, etc.)\n" +
                       "7. Add any special notes or instructions\n" +
                       "8. Confirm the appointment details\n\n" +
                       "The system will automatically:\n" +
                       "• Check doctor availability\n" +
                       "• Prevent double-booking\n" +
                       "• Send confirmation notifications\n" +
                       "• Update the appointment calendar\n\n" +
                       "Appointment statuses include: Scheduled, Completed, Cancelled, No-Show";

            case "Creating Bills":
                return "To create a new bill in the Finance module:\n\n" +
                       "1. Navigate to the Finance module\n" +
                       "2. Click '+ Create Bill'\n" +
                       "3. Select the patient for billing\n" +
                       "4. Choose the service type:\n" +
                       "   • Consultation fees\n" +
                       "   • Surgery charges\n" +
                       "   • Laboratory tests\n" +
                       "   • Pharmacy items\n" +
                       "   • Room charges\n" +
                       "5. Enter the amount and any applicable taxes\n" +
                       "6. Add itemized details if necessary\n" +
                       "7. Set payment terms and due date\n" +
                       "8. Generate the bill\n\n" +
                       "The system will:\n" +
                       "• Assign a unique bill number\n" +
                       "• Calculate totals automatically\n" +
                       "• Track payment status\n" +
                       "• Generate printable invoices";

            case "Generating Reports":
                return "The Reports module provides comprehensive analytics:\n\n" +
                       "Available Report Types:\n" +
                       "• Patient Reports: Demographics, medical history, visit frequency\n" +
                       "• Doctor Reports: Performance metrics, patient load, revenue\n" +
                       "• Financial Reports: Revenue analysis, outstanding payments\n" +
                       "• Department Reports: Departmental performance and statistics\n\n" +
                       "To generate a report:\n" +
                       "1. Select the report type\n" +
                       "2. Choose date range filters\n" +
                       "3. Select specific departments or doctors (if applicable)\n" +
                       "4. Click 'Generate Report'\n" +
                       "5. Choose export format (PDF or CSV)\n\n" +
                       "Export Options:\n" +
                       "• PDF: Professional formatted reports with MediSys branding\n" +
                       "• CSV: Raw data for further analysis in spreadsheet applications\n\n" +
                       "All PDF reports include watermarked logos for authenticity.";

            case "User Roles & Permissions":
                return "MediSys implements role-based access control:\n\n" +
                       "Available Roles:\n\n" +
                       "1. System Administrator:\n" +
                       "   • Full system access\n" +
                       "   • User management\n" +
                       "   • System configuration\n" +
                       "   • All modules and reports\n\n" +
                       "2. Medical Doctor:\n" +
                       "   • Patient management\n" +
                       "   • Appointment scheduling\n" +
                       "   • Medical records access\n" +
                       "   • Limited financial data\n\n" +
                       "3. Finance Manager:\n" +
                       "   • Billing and payments\n" +
                       "   • Financial reports\n" +
                       "   • Revenue management\n" +
                       "   • Insurance processing\n\n" +
                       "4. Department Head:\n" +
                       "   • Department-specific access\n" +
                       "   • Staff management\n" +
                       "   • Departmental reports\n" +
                       "   • Resource allocation\n\n" +
                       "Each role has carefully defined permissions to ensure data security and operational efficiency.";

            default:
                return "Detailed help content for '" + topic + "' is being prepared. " +
                       "This comprehensive help system covers all aspects of the MediSys Hospital Management System.\n\n" +
                       "For immediate assistance, please contact our support team:\n" +
                       "📧 Email: support@medisys.com\n" +
                       "📞 Phone: +1-800-MEDISYS\n\n" +
                       "Our support team is available Monday through Friday, 9 AM to 6 PM.";
        }
    }

    private void showSearchDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Search Help");
        dialog.setHeaderText("Search Help Topics");
        dialog.setContentText("Help search functionality will be implemented here.");
        dialog.showAndWait();
    }

    private void showContactDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Contact Support");
        dialog.setHeaderText("Contact Information");
        dialog.setContentText(
            "For technical support, please contact:\n\n" +
            "📧 Email: support@medisys.com\n" +
            "📞 Phone: +1-800-MEDISYS\n" +
            "🌐 Website: www.medisys.com/support\n\n" +
            "Support Hours: Monday - Friday, 9 AM - 6 PM"
        );
        dialog.showAndWait();
    }

    public VBox getRoot() {
        return root;
    }
}
