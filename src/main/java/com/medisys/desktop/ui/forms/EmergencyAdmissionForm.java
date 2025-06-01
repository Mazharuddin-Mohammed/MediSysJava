package com.medisys.desktop.ui.forms;

import com.medisys.desktop.utils.IconLibrary;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Emergency Admission Form
 * Fast-track patient admission for emergency cases
 */
public class EmergencyAdmissionForm {
    
    private final Stage stage;
    private final Runnable onSave;
    
    // Form fields
    private TextField patientNameField;
    private TextField ageField;
    private ComboBox<String> genderCombo;
    private TextField phoneField;
    private TextField emergencyContactField;
    private TextField emergencyPhoneField;
    private ComboBox<String> emergencyTypeCombo;
    private ComboBox<String> priorityCombo;
    private ComboBox<String> assignedDoctorCombo;
    private ComboBox<String> bedNumberCombo;
    private TextArea symptomsArea;
    private TextArea initialAssessmentArea;
    private CheckBox consentCheckBox;
    
    public EmergencyAdmissionForm(Runnable onSave) {
        this.onSave = onSave;
        this.stage = new Stage();
        
        initializeForm();
    }
    
    private void initializeForm() {
        stage.setTitle("🚨 Emergency Admission");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("form-container");
        root.setStyle("-fx-background-color: #FFE5E5;");
        
        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        
        Text header = new Text(IconLibrary.EMERGENCY + " EMERGENCY ADMISSION");
        header.getStyleClass().add("form-header");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #C0392B;");
        
        Text timestamp = new Text("Admission Time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        timestamp.setStyle("-fx-font-size: 14px; -fx-fill: #E74C3C; -fx-font-weight: bold;");
        
        headerBox.getChildren().addAll(header, timestamp);
        
        // Form content
        ScrollPane scrollPane = new ScrollPane(createFormContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.getStyleClass().add("form-scroll");
        
        // Buttons
        HBox buttonBox = createButtonBox();
        
        root.getChildren().addAll(headerBox, scrollPane, buttonBox);
        
        Scene scene = new Scene(root, 700, 700);
        try {
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Could not load stylesheet");
        }
        stage.setScene(scene);
    }
    
    private VBox createFormContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        
        // Patient Information Section
        VBox patientSection = createSection(IconLibrary.PATIENT + " Patient Information");
        
        HBox nameAgeRow = new HBox(15);
        nameAgeRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox nameBox = new VBox(5);
        nameBox.getChildren().addAll(
            new Label("Patient Name *"),
            patientNameField = createTextField("Enter patient full name")
        );
        
        VBox ageBox = new VBox(5);
        ageBox.getChildren().addAll(
            new Label("Age *"),
            ageField = createTextField("Age")
        );
        ageField.setPrefWidth(100);
        
        VBox genderBox = new VBox(5);
        genderCombo = new ComboBox<>();
        genderCombo.getStyleClass().add("modern-combo-box");
        genderCombo.getItems().addAll("Male", "Female", "Other");
        genderCombo.setPromptText("Gender");
        genderBox.getChildren().addAll(new Label("Gender *"), genderCombo);
        
        nameAgeRow.getChildren().addAll(nameBox, ageBox, genderBox);
        HBox.setHgrow(nameBox, Priority.ALWAYS);
        
        HBox contactRow = new HBox(15);
        contactRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox phoneBox = new VBox(5);
        phoneBox.getChildren().addAll(
            new Label("Phone Number"),
            phoneField = createTextField("Enter phone number")
        );
        
        VBox emergencyContactBox = new VBox(5);
        emergencyContactBox.getChildren().addAll(
            new Label("Emergency Contact Name *"),
            emergencyContactField = createTextField("Enter emergency contact name")
        );
        
        contactRow.getChildren().addAll(phoneBox, emergencyContactBox);
        HBox.setHgrow(phoneBox, Priority.ALWAYS);
        HBox.setHgrow(emergencyContactBox, Priority.ALWAYS);
        
        VBox emergencyPhoneBox = new VBox(5);
        emergencyPhoneBox.getChildren().addAll(
            new Label("Emergency Contact Phone *"),
            emergencyPhoneField = createTextField("Enter emergency contact phone")
        );
        
        patientSection.getChildren().addAll(nameAgeRow, contactRow, emergencyPhoneBox);
        
        // Emergency Details Section
        VBox emergencySection = createSection(IconLibrary.EMERGENCY + " Emergency Details");
        
        HBox emergencyRow = new HBox(15);
        emergencyRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox typeBox = new VBox(5);
        emergencyTypeCombo = new ComboBox<>();
        emergencyTypeCombo.getStyleClass().add("modern-combo-box");
        emergencyTypeCombo.getItems().addAll(
            "Cardiac Emergency", "Respiratory Emergency", "Trauma/Accident", 
            "Neurological Emergency", "Poisoning", "Burns", "Stroke", 
            "Severe Pain", "Unconscious", "Other"
        );
        emergencyTypeCombo.setPromptText("Select emergency type");
        typeBox.getChildren().addAll(new Label("Emergency Type *"), emergencyTypeCombo);
        
        VBox priorityBox = new VBox(5);
        priorityCombo = new ComboBox<>();
        priorityCombo.getStyleClass().add("modern-combo-box");
        priorityCombo.getItems().addAll("Critical", "High", "Medium", "Low");
        priorityCombo.setPromptText("Select priority");
        priorityCombo.setValue("High"); // Default to High for emergency
        priorityBox.getChildren().addAll(new Label("Priority *"), priorityCombo);
        
        emergencyRow.getChildren().addAll(typeBox, priorityBox);
        HBox.setHgrow(typeBox, Priority.ALWAYS);
        HBox.setHgrow(priorityBox, Priority.ALWAYS);
        
        VBox symptomsBox = new VBox(5);
        symptomsArea = new TextArea();
        symptomsArea.getStyleClass().add("modern-text-area");
        symptomsArea.setPromptText("Describe current symptoms and condition...");
        symptomsArea.setPrefRowCount(3);
        symptomsArea.setWrapText(true);
        symptomsBox.getChildren().addAll(new Label("Symptoms & Condition *"), symptomsArea);
        
        emergencySection.getChildren().addAll(emergencyRow, symptomsBox);
        
        // Medical Assignment Section
        VBox assignmentSection = createSection(IconLibrary.DOCTOR + " Medical Assignment");
        
        HBox assignmentRow = new HBox(15);
        assignmentRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox doctorBox = new VBox(5);
        assignedDoctorCombo = new ComboBox<>();
        assignedDoctorCombo.getStyleClass().add("modern-combo-box");
        assignedDoctorCombo.getItems().addAll(
            "Dr. John Smith - Emergency Medicine",
            "Dr. Sarah Johnson - Cardiology", 
            "Dr. Michael Brown - Trauma Surgery",
            "Dr. Lisa Davis - Emergency Medicine",
            "Dr. Robert Wilson - Critical Care"
        );
        assignedDoctorCombo.setPromptText("Assign doctor");
        doctorBox.getChildren().addAll(new Label("Assigned Doctor *"), assignedDoctorCombo);
        
        VBox bedBox = new VBox(5);
        bedNumberCombo = new ComboBox<>();
        bedNumberCombo.getStyleClass().add("modern-combo-box");
        bedNumberCombo.getItems().addAll(
            "Emergency Bed 1", "Emergency Bed 2", "Emergency Bed 3",
            "ICU Bed 1", "ICU Bed 2", "Trauma Room 1", "Trauma Room 2"
        );
        bedNumberCombo.setPromptText("Assign bed");
        bedBox.getChildren().addAll(new Label("Bed Assignment *"), bedNumberCombo);
        
        assignmentRow.getChildren().addAll(doctorBox, bedBox);
        HBox.setHgrow(doctorBox, Priority.ALWAYS);
        HBox.setHgrow(bedBox, Priority.ALWAYS);
        
        assignmentSection.getChildren().add(assignmentRow);
        
        // Initial Assessment Section
        VBox assessmentSection = createSection(IconLibrary.STETHOSCOPE + " Initial Assessment");
        
        VBox assessmentBox = new VBox(5);
        initialAssessmentArea = new TextArea();
        initialAssessmentArea.getStyleClass().add("modern-text-area");
        initialAssessmentArea.setPromptText("Initial medical assessment, vital signs, immediate actions taken...");
        initialAssessmentArea.setPrefRowCount(4);
        initialAssessmentArea.setWrapText(true);
        assessmentBox.getChildren().addAll(new Label("Initial Assessment"), initialAssessmentArea);
        
        assessmentSection.getChildren().add(assessmentBox);
        
        // Consent Section
        VBox consentSection = createSection(IconLibrary.DOCUMENT + " Consent & Authorization");
        
        consentCheckBox = new CheckBox("I authorize emergency medical treatment and understand that this is an emergency admission. " +
                                     "I consent to necessary medical procedures and treatment.");
        consentCheckBox.setWrapText(true);
        consentCheckBox.setStyle("-fx-font-weight: bold; -fx-text-fill: #C0392B;");
        
        consentSection.getChildren().add(consentCheckBox);
        
        content.getChildren().addAll(
            patientSection, emergencySection, assignmentSection, assessmentSection, consentSection
        );
        
        return content;
    }
    
    private VBox createSection(String title) {
        VBox section = new VBox(15);
        section.getStyleClass().add("form-section");
        section.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 8; -fx-border-color: #E74C3C; -fx-border-width: 1; -fx-border-radius: 8;");
        
        Text sectionTitle = new Text(title);
        sectionTitle.getStyleClass().add("section-title");
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #C0392B;");
        
        section.getChildren().add(sectionTitle);
        return section;
    }
    
    private TextField createTextField(String promptText) {
        TextField field = new TextField();
        field.getStyleClass().add("modern-text-field");
        field.setPromptText(promptText);
        field.setStyle("-fx-padding: 10; -fx-font-size: 14px;");
        return field;
    }
    
    private HBox createButtonBox() {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button cancelBtn = new Button(IconLibrary.ERROR + " Cancel");
        cancelBtn.getStyleClass().add("secondary-button");
        cancelBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        cancelBtn.setOnAction(e -> stage.close());
        
        Button admitBtn = new Button(IconLibrary.EMERGENCY + " ADMIT PATIENT");
        admitBtn.getStyleClass().add("primary-button");
        admitBtn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 15 25; -fx-font-size: 14px;");
        admitBtn.setOnAction(e -> admitPatient());
        
        buttonBox.getChildren().addAll(cancelBtn, admitBtn);
        
        return buttonBox;
    }
    
    private void admitPatient() {
        // Validate form
        if (!validateForm()) {
            return;
        }
        
        // Show confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Emergency Admission");
        confirmation.setHeaderText("🚨 EMERGENCY ADMISSION CONFIRMATION");
        confirmation.setContentText("Are you sure you want to admit this patient for emergency treatment?\n\n" +
                                   "Patient: " + patientNameField.getText() + "\n" +
                                   "Emergency Type: " + emergencyTypeCombo.getValue() + "\n" +
                                   "Priority: " + priorityCombo.getValue() + "\n" +
                                   "Assigned Doctor: " + assignedDoctorCombo.getValue());
        
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Process admission
                processEmergencyAdmission();
            }
        });
    }
    
    private void processEmergencyAdmission() {
        // Simulate admission process
        Alert progress = new Alert(Alert.AlertType.INFORMATION);
        progress.setTitle("Emergency Admission Processed");
        progress.setHeaderText("✅ PATIENT ADMITTED SUCCESSFULLY");
        progress.setContentText("Emergency admission completed!\n\n" +
                               "Admission ID: EMR" + System.currentTimeMillis() + "\n" +
                               "Patient: " + patientNameField.getText() + "\n" +
                               "Bed: " + bedNumberCombo.getValue() + "\n" +
                               "Doctor: " + assignedDoctorCombo.getValue() + "\n\n" +
                               "Actions taken:\n" +
                               "• Patient registered in emergency system\n" +
                               "• Bed reserved and prepared\n" +
                               "• Doctor notified\n" +
                               "• Emergency team alerted\n" +
                               "• Medical records created");
        progress.showAndWait();
        
        // Call save callback
        if (onSave != null) {
            onSave.run();
        }
        
        stage.close();
    }
    
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        
        if (patientNameField.getText().trim().isEmpty()) {
            errors.append("• Patient name is required\n");
        }
        if (ageField.getText().trim().isEmpty()) {
            errors.append("• Age is required\n");
        } else {
            try {
                Integer.parseInt(ageField.getText().trim());
            } catch (NumberFormatException e) {
                errors.append("• Age must be a valid number\n");
            }
        }
        if (genderCombo.getValue() == null) {
            errors.append("• Gender is required\n");
        }
        if (emergencyContactField.getText().trim().isEmpty()) {
            errors.append("• Emergency contact name is required\n");
        }
        if (emergencyPhoneField.getText().trim().isEmpty()) {
            errors.append("• Emergency contact phone is required\n");
        }
        if (emergencyTypeCombo.getValue() == null) {
            errors.append("• Emergency type is required\n");
        }
        if (priorityCombo.getValue() == null) {
            errors.append("• Priority is required\n");
        }
        if (assignedDoctorCombo.getValue() == null) {
            errors.append("• Assigned doctor is required\n");
        }
        if (bedNumberCombo.getValue() == null) {
            errors.append("• Bed assignment is required\n");
        }
        if (symptomsArea.getText().trim().isEmpty()) {
            errors.append("• Symptoms description is required\n");
        }
        if (!consentCheckBox.isSelected()) {
            errors.append("• Consent for emergency treatment is required\n");
        }
        
        if (errors.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please fix the following errors:");
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
    public void show() {
        stage.show();
    }
}
