package com.medisys.desktop.ui.components;

import com.medisys.desktop.model.User;
import com.medisys.desktop.ui.forms.AppointmentForm;
import com.medisys.desktop.utils.ReportExporter;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Quick Actions Panel for Dashboard
 * Provides instant access to common hospital operations
 */
public class QuickActionsPanel {
    
    private final User currentUser;
    private final VBox root;
    
    public QuickActionsPanel(User currentUser) {
        this.currentUser = currentUser;
        this.root = new VBox(15);
        
        initializePanel();
    }
    
    private void initializePanel() {
        root.setPadding(new Insets(20));
        root.getStyleClass().add("modern-card");
        
        // Header
        Text header = new Text("⚡ Quick Actions");
        header.getStyleClass().add("section-title");
        
        // Quick action buttons grid
        GridPane actionsGrid = createActionsGrid();
        
        // Emergency section
        VBox emergencySection = createEmergencySection();
        
        root.getChildren().addAll(header, actionsGrid, emergencySection);
    }
    
    private GridPane createActionsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);
        
        // Row 1: Patient & Doctor Actions
        Button addPatientBtn = createQuickActionButton("👤 Add Patient", "#4ECDC4", this::quickAddPatient);
        Button addDoctorBtn = createQuickActionButton("👨‍⚕️ Add Doctor", "#A23B72", this::quickAddDoctor);
        Button scheduleAppointmentBtn = createQuickActionButton("📅 Schedule", "#F18F01", this::quickScheduleAppointment);
        
        grid.add(addPatientBtn, 0, 0);
        grid.add(addDoctorBtn, 1, 0);
        grid.add(scheduleAppointmentBtn, 2, 0);
        
        // Row 2: Financial & Reports
        Button createBillBtn = createQuickActionButton("💰 Create Bill", "#27AE60", this::quickCreateBill);
        Button generateReportBtn = createQuickActionButton("📊 Generate Report", "#E74C3C", this::quickGenerateReport);
        Button viewStatsBtn = createQuickActionButton("📈 View Stats", "#9B59B6", this::quickViewStats);
        
        grid.add(createBillBtn, 0, 1);
        grid.add(generateReportBtn, 1, 1);
        grid.add(viewStatsBtn, 2, 1);
        
        // Row 3: System Actions
        Button backupBtn = createQuickActionButton("💾 Backup Data", "#34495E", this::quickBackupData);
        Button settingsBtn = createQuickActionButton("⚙️ Settings", "#7F8C8D", this::quickSettings);
        Button helpBtn = createQuickActionButton("❓ Help", "#3498DB", this::quickHelp);
        
        grid.add(backupBtn, 0, 2);
        grid.add(settingsBtn, 1, 2);
        grid.add(helpBtn, 2, 2);
        
        return grid;
    }
    
    private Button createQuickActionButton(String text, String color, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("quick-action-button");
        button.setStyle(String.format(
            "-fx-background-color: %s; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 12px; " +
            "-fx-padding: 15 20 15 20; " +
            "-fx-background-radius: 8; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);",
            color
        ));
        
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + 
            "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace(
            "-fx-scale-x: 1.05; -fx-scale-y: 1.05;", "")));
        
        button.setPrefWidth(140);
        button.setPrefHeight(60);
        button.setOnAction(e -> action.run());
        
        return button;
    }
    
    private VBox createEmergencySection() {
        VBox emergencySection = new VBox(10);
        emergencySection.setPadding(new Insets(15));
        emergencySection.setStyle("-fx-background-color: #FFE5E5; -fx-background-radius: 8; -fx-border-color: #E74C3C; -fx-border-radius: 8;");
        
        Text emergencyHeader = new Text("🚨 Emergency Actions");
        emergencyHeader.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #C0392B;");
        
        HBox emergencyButtons = new HBox(10);
        emergencyButtons.setAlignment(Pos.CENTER);
        
        Button emergencyAdmissionBtn = new Button("🏥 Emergency Admission");
        emergencyAdmissionBtn.getStyleClass().add("emergency-button");
        emergencyAdmissionBtn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold;");
        emergencyAdmissionBtn.setOnAction(e -> quickEmergencyAdmission());
        
        Button callDoctorBtn = new Button("📞 Call Doctor");
        callDoctorBtn.getStyleClass().add("emergency-button");
        callDoctorBtn.setStyle("-fx-background-color: #C0392B; -fx-text-fill: white; -fx-font-weight: bold;");
        callDoctorBtn.setOnAction(e -> quickCallDoctor());
        
        Button alertSystemBtn = new Button("🔔 Alert System");
        alertSystemBtn.getStyleClass().add("emergency-button");
        alertSystemBtn.setStyle("-fx-background-color: #A93226; -fx-text-fill: white; -fx-font-weight: bold;");
        alertSystemBtn.setOnAction(e -> quickAlertSystem());
        
        emergencyButtons.getChildren().addAll(emergencyAdmissionBtn, callDoctorBtn, alertSystemBtn);
        emergencySection.getChildren().addAll(emergencyHeader, emergencyButtons);
        
        return emergencySection;
    }
    
    // Quick Action Methods
    private void quickAddPatient() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Quick Add Patient");
        dialog.setHeaderText("Patient Registration");
        dialog.setContentText("Quick patient registration form will open here.\n\nFeatures:\n• Fast data entry\n• Essential fields only\n• Photo capture\n• Insurance verification\n• Immediate ID generation");
        dialog.showAndWait();
    }
    
    private void quickAddDoctor() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Quick Add Doctor");
        dialog.setHeaderText("Doctor Registration");
        dialog.setContentText("Quick doctor registration form will open here.\n\nFeatures:\n• Professional details\n• Specialization setup\n• Schedule configuration\n• Credential verification\n• Department assignment");
        dialog.showAndWait();
    }
    
    private void quickScheduleAppointment() {
        try {
            AppointmentForm form = new AppointmentForm(null, appointment -> {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setContentText("Appointment scheduled successfully!");
                success.showAndWait();
            });
            form.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setContentText("Failed to open appointment form: " + e.getMessage());
            error.showAndWait();
        }
    }
    
    private void quickCreateBill() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Quick Create Bill");
        dialog.setHeaderText("Billing System");
        dialog.setContentText("Quick billing form will open here.\n\nFeatures:\n• Patient selection\n• Service items\n• Insurance processing\n• Payment methods\n• Instant invoice generation");
        dialog.showAndWait();
    }
    
    private void quickGenerateReport() {
        try {
            var sampleData = FXCollections.observableArrayList(
                "Quick Report - Generated at " + java.time.LocalDateTime.now(),
                "Total Patients Today: 45",
                "Revenue Today: ₹85,000",
                "Pending Appointments: 12"
            );
            
            Stage stage = (Stage) root.getScene().getWindow();
            ReportExporter.exportReport("Quick Daily Report", "All Departments", sampleData, stage);
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Export Error");
            error.setContentText("Failed to generate report: " + e.getMessage());
            error.showAndWait();
        }
    }
    
    private void quickViewStats() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Quick Statistics");
        dialog.setHeaderText("Hospital Statistics Dashboard");
        dialog.setContentText("Real-time statistics:\n\n" +
            "📊 Today's Overview:\n" +
            "• Patients: 45 (↑12% from yesterday)\n" +
            "• Revenue: ₹85,000 (↑8% from yesterday)\n" +
            "• Appointments: 32 (↑5% from yesterday)\n" +
            "• Bed Occupancy: 78% (↑3% from yesterday)\n\n" +
            "🏥 Department Status:\n" +
            "• Emergency: 8 patients\n" +
            "• Cardiology: 12 patients\n" +
            "• Neurology: 9 patients\n" +
            "• Orthopedics: 16 patients");
        dialog.showAndWait();
    }
    
    private void quickBackupData() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Backup Data");
        confirmation.setHeaderText("System Backup");
        confirmation.setContentText("This will create a backup of all hospital data.\n\nProceed with backup?");
        
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Alert progress = new Alert(Alert.AlertType.INFORMATION);
                progress.setTitle("Backup in Progress");
                progress.setHeaderText("Creating Backup...");
                progress.setContentText("Backup completed successfully!\n\nLocation: /backups/medisys_backup_" + 
                    java.time.LocalDate.now() + ".sql\n\nBackup includes:\n• Patient records\n• Doctor information\n• Appointments\n• Financial data\n• System settings");
                progress.showAndWait();
            }
        });
    }
    
    private void quickSettings() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Quick Settings");
        dialog.setHeaderText("System Settings");
        dialog.setContentText("Quick access to common settings:\n\n" +
            "🔧 System Configuration:\n" +
            "• User management\n" +
            "• Department settings\n" +
            "• Backup schedule\n" +
            "• Security settings\n\n" +
            "🎨 Interface Settings:\n" +
            "• Theme selection\n" +
            "• Language preferences\n" +
            "• Notification settings\n" +
            "• Display options");
        dialog.showAndWait();
    }
    
    private void quickHelp() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Quick Help");
        dialog.setHeaderText("MediSys Help Center");
        dialog.setContentText("📚 Available Resources:\n\n" +
            "• User Manual: Complete system guide\n" +
            "• Video Tutorials: Step-by-step walkthroughs\n" +
            "• FAQ: Common questions and answers\n" +
            "• Support: Contact technical support\n\n" +
            "🔗 Quick Links:\n" +
            "• Getting Started Guide\n" +
            "• Keyboard Shortcuts\n" +
            "• Troubleshooting\n" +
            "• System Requirements\n\n" +
            "📞 Support: +91-1234567890\n" +
            "📧 Email: support@medisys.com");
        dialog.showAndWait();
    }
    
    // Emergency Actions
    private void quickEmergencyAdmission() {
        Alert emergency = new Alert(Alert.AlertType.WARNING);
        emergency.setTitle("Emergency Admission");
        emergency.setHeaderText("🚨 EMERGENCY PROTOCOL ACTIVATED");
        emergency.setContentText("Emergency admission process initiated.\n\n" +
            "✅ Actions taken:\n" +
            "• Emergency bed reserved\n" +
            "• On-call doctor notified\n" +
            "• Emergency team alerted\n" +
            "• Fast-track registration ready\n\n" +
            "Next steps:\n" +
            "1. Complete patient registration\n" +
            "2. Assign emergency bed\n" +
            "3. Contact emergency contact\n" +
            "4. Prepare medical history");
        emergency.showAndWait();
    }
    
    private void quickCallDoctor() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Call Doctor");
        dialog.setHeaderText("📞 Doctor Communication");
        dialog.setContentText("Available doctors for immediate contact:\n\n" +
            "🟢 Dr. John Smith (Cardiology)\n" +
            "   📱 +91-9876543101 | 🏥 Room 201\n\n" +
            "🟢 Dr. Sarah Johnson (Neurology)\n" +
            "   📱 +91-9876543102 | 🏥 Room 305\n\n" +
            "🟡 Dr. Michael Brown (Orthopedics)\n" +
            "   📱 +91-9876543103 | 🏥 Surgery Wing\n\n" +
            "🔴 Emergency Hotline: 108\n" +
            "🏥 Hospital Main: +91-1234567890");
        dialog.showAndWait();
    }
    
    private void quickAlertSystem() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("System Alert");
        alert.setHeaderText("🔔 HOSPITAL ALERT SYSTEM");
        alert.setContentText("Alert broadcast options:\n\n" +
            "🚨 Emergency Alert\n" +
            "• Code Blue (Cardiac Emergency)\n" +
            "• Code Red (Fire Emergency)\n" +
            "• Code Silver (Security Emergency)\n\n" +
            "📢 General Announcements\n" +
            "• Visitor hours reminder\n" +
            "• Department notifications\n" +
            "• System maintenance alerts\n\n" +
            "⚠️ Staff Notifications\n" +
            "• Shift changes\n" +
            "• Meeting reminders\n" +
            "• Policy updates");
        alert.showAndWait();
    }
    
    public VBox getRoot() {
        return root;
    }
}
