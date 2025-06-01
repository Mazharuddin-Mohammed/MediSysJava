package com.medisys.desktop.ui.forms;

import com.medisys.desktop.model.Patient;
import com.medisys.desktop.utils.IconLibrary;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * Complete Patient Registration Form
 * Professional patient registration with all required fields
 */
public class PatientRegistrationForm {
    
    private final Stage stage;
    private final Patient patient;
    private final boolean isEdit;
    private final Consumer<Patient> onSave;
    
    // Form fields
    private TextField firstNameField;
    private TextField lastNameField;
    private DatePicker dobPicker;
    private ComboBox<String> genderCombo;
    private TextField emailField;
    private TextField phoneField;
    private TextArea addressArea;
    private ComboBox<String> bloodGroupCombo;
    private TextField emergencyContactField;
    private TextField emergencyPhoneField;
    private TextField insuranceField;
    private TextField allergiesField;
    private TextArea medicalHistoryArea;
    private Label photoLabel;
    private String photoPath;
    
    public PatientRegistrationForm(Patient patient, Consumer<Patient> onSave) {
        this.patient = patient;
        this.isEdit = patient != null;
        this.onSave = onSave;
        this.stage = new Stage();
        
        initializeForm();
    }
    
    private void initializeForm() {
        stage.setTitle(isEdit ? "Edit Patient" : "Register New Patient");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("form-container");
        
        // Header
        Text header = new Text(isEdit ? IconLibrary.EDIT + " Edit Patient" : IconLibrary.PATIENTS + " Register New Patient");
        header.getStyleClass().add("form-header");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #2E86AB;");
        
        // Form content
        ScrollPane scrollPane = new ScrollPane(createFormContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.getStyleClass().add("form-scroll");
        
        // Buttons
        HBox buttonBox = createButtonBox();
        
        root.getChildren().addAll(header, scrollPane, buttonBox);
        
        Scene scene = new Scene(root, 700, 700);
        try {
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Could not load stylesheet");
        }
        stage.setScene(scene);
        
        // Load existing data if editing
        if (isEdit) {
            loadPatientData();
        }
    }
    
    private VBox createFormContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        
        // Personal Information Section
        VBox personalSection = createSection(IconLibrary.PATIENT + " Personal Information");
        
        HBox nameRow = new HBox(15);
        nameRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox firstNameBox = new VBox(5);
        firstNameBox.getChildren().addAll(
            new Label("First Name *"),
            firstNameField = createTextField("Enter first name")
        );
        
        VBox lastNameBox = new VBox(5);
        lastNameBox.getChildren().addAll(
            new Label("Last Name *"),
            lastNameField = createTextField("Enter last name")
        );
        
        nameRow.getChildren().addAll(firstNameBox, lastNameBox);
        HBox.setHgrow(firstNameBox, Priority.ALWAYS);
        HBox.setHgrow(lastNameBox, Priority.ALWAYS);
        
        HBox dobGenderRow = new HBox(15);
        dobGenderRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox dobBox = new VBox(5);
        dobPicker = new DatePicker();
        dobPicker.getStyleClass().add("modern-date-picker");
        dobPicker.setPromptText("Select date of birth");
        dobBox.getChildren().addAll(new Label("Date of Birth *"), dobPicker);
        
        VBox genderBox = new VBox(5);
        genderCombo = new ComboBox<>();
        genderCombo.getStyleClass().add("modern-combo-box");
        genderCombo.getItems().addAll("Male", "Female", "Other");
        genderCombo.setPromptText("Select gender");
        genderBox.getChildren().addAll(new Label("Gender *"), genderCombo);
        
        dobGenderRow.getChildren().addAll(dobBox, genderBox);
        HBox.setHgrow(dobBox, Priority.ALWAYS);
        HBox.setHgrow(genderBox, Priority.ALWAYS);
        
        personalSection.getChildren().addAll(nameRow, dobGenderRow);
        
        // Contact Information Section
        VBox contactSection = createSection(IconLibrary.PHONE + " Contact Information");
        
        HBox contactRow = new HBox(15);
        contactRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox emailBox = new VBox(5);
        emailBox.getChildren().addAll(
            new Label("Email *"),
            emailField = createTextField("Enter email address")
        );
        
        VBox phoneBox = new VBox(5);
        phoneBox.getChildren().addAll(
            new Label("Phone *"),
            phoneField = createTextField("Enter phone number")
        );
        
        contactRow.getChildren().addAll(emailBox, phoneBox);
        HBox.setHgrow(emailBox, Priority.ALWAYS);
        HBox.setHgrow(phoneBox, Priority.ALWAYS);
        
        VBox addressBox = new VBox(5);
        addressArea = new TextArea();
        addressArea.getStyleClass().add("modern-text-area");
        addressArea.setPromptText("Enter complete address");
        addressArea.setPrefRowCount(3);
        addressArea.setWrapText(true);
        addressBox.getChildren().addAll(new Label("Address"), addressArea);
        
        contactSection.getChildren().addAll(contactRow, addressBox);
        
        // Medical Information Section
        VBox medicalSection = createSection(IconLibrary.STETHOSCOPE + " Medical Information");
        
        HBox medicalRow = new HBox(15);
        medicalRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox bloodGroupBox = new VBox(5);
        bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getStyleClass().add("modern-combo-box");
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        bloodGroupCombo.setPromptText("Select blood group");
        bloodGroupBox.getChildren().addAll(new Label("Blood Group"), bloodGroupCombo);
        
        VBox allergiesBox = new VBox(5);
        allergiesBox.getChildren().addAll(
            new Label("Allergies"),
            allergiesField = createTextField("Enter known allergies")
        );
        
        medicalRow.getChildren().addAll(bloodGroupBox, allergiesBox);
        HBox.setHgrow(bloodGroupBox, Priority.ALWAYS);
        HBox.setHgrow(allergiesBox, Priority.ALWAYS);
        
        VBox historyBox = new VBox(5);
        medicalHistoryArea = new TextArea();
        medicalHistoryArea.getStyleClass().add("modern-text-area");
        medicalHistoryArea.setPromptText("Enter medical history");
        medicalHistoryArea.setPrefRowCount(3);
        medicalHistoryArea.setWrapText(true);
        historyBox.getChildren().addAll(new Label("Medical History"), medicalHistoryArea);
        
        medicalSection.getChildren().addAll(medicalRow, historyBox);
        
        // Emergency Contact Section
        VBox emergencySection = createSection(IconLibrary.EMERGENCY + " Emergency Contact");
        
        HBox emergencyRow = new HBox(15);
        emergencyRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox emergencyContactBox = new VBox(5);
        emergencyContactBox.getChildren().addAll(
            new Label("Emergency Contact Name"),
            emergencyContactField = createTextField("Enter emergency contact name")
        );
        
        VBox emergencyPhoneBox = new VBox(5);
        emergencyPhoneBox.getChildren().addAll(
            new Label("Emergency Contact Phone"),
            emergencyPhoneField = createTextField("Enter emergency contact phone")
        );
        
        emergencyRow.getChildren().addAll(emergencyContactBox, emergencyPhoneBox);
        HBox.setHgrow(emergencyContactBox, Priority.ALWAYS);
        HBox.setHgrow(emergencyPhoneBox, Priority.ALWAYS);
        
        emergencySection.getChildren().add(emergencyRow);
        
        // Insurance Section
        VBox insuranceSection = createSection(IconLibrary.DOCUMENT + " Insurance Information");
        
        VBox insuranceBox = new VBox(5);
        insuranceBox.getChildren().addAll(
            new Label("Insurance Provider"),
            insuranceField = createTextField("Enter insurance provider")
        );
        
        insuranceSection.getChildren().add(insuranceBox);
        
        // Photo Section
        VBox photoSection = createSection(IconLibrary.PATIENT + " Patient Photo");
        
        HBox photoRow = new HBox(15);
        photoRow.setAlignment(Pos.CENTER_LEFT);
        
        photoLabel = new Label("No photo selected");
        photoLabel.getStyleClass().add("photo-label");
        
        Button selectPhotoBtn = new Button(IconLibrary.UPLOAD + " Select Photo");
        selectPhotoBtn.getStyleClass().add("modern-button");
        selectPhotoBtn.setOnAction(e -> selectPhoto());
        
        photoRow.getChildren().addAll(photoLabel, selectPhotoBtn);
        photoSection.getChildren().add(photoRow);
        
        content.getChildren().addAll(
            personalSection, contactSection, medicalSection, 
            emergencySection, insuranceSection, photoSection
        );
        
        return content;
    }
    
    private VBox createSection(String title) {
        VBox section = new VBox(15);
        section.getStyleClass().add("form-section");
        section.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-background-radius: 8;");
        
        Text sectionTitle = new Text(title);
        sectionTitle.getStyleClass().add("section-title");
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #2E86AB;");
        
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
        
        Button saveBtn = new Button(IconLibrary.SAVE + " " + (isEdit ? "Update" : "Register") + " Patient");
        saveBtn.getStyleClass().add("primary-button");
        saveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        saveBtn.setOnAction(e -> savePatient());
        
        buttonBox.getChildren().addAll(cancelBtn, saveBtn);
        
        return buttonBox;
    }
    
    private void selectPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Patient Photo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            photoPath = selectedFile.getAbsolutePath();
            photoLabel.setText("Photo: " + selectedFile.getName());
        }
    }
    
    private void loadPatientData() {
        if (patient != null) {
            firstNameField.setText(patient.getFirstName());
            lastNameField.setText(patient.getLastName());
            dobPicker.setValue(patient.getDateOfBirth());
            genderCombo.setValue(patient.getGender());
            emailField.setText(patient.getEmail());
            phoneField.setText(patient.getPhone());
            addressArea.setText(patient.getAddress());
            bloodGroupCombo.setValue(patient.getBloodGroup());
            emergencyContactField.setText(patient.getEmergencyContactName());
            emergencyPhoneField.setText(patient.getEmergencyContactPhone());
            insuranceField.setText(patient.getInsuranceProvider());
            allergiesField.setText(patient.getAllergies());
            medicalHistoryArea.setText(patient.getMedicalHistory());
        }
    }
    
    private void savePatient() {
        // Validate form
        if (!validateForm()) {
            return;
        }
        
        // Create or update patient
        Patient pat = patient != null ? patient : new Patient();
        
        pat.setFirstName(firstNameField.getText().trim());
        pat.setLastName(lastNameField.getText().trim());
        pat.setDateOfBirth(dobPicker.getValue());
        pat.setGender(genderCombo.getValue());
        pat.setEmail(emailField.getText().trim());
        pat.setPhone(phoneField.getText().trim());
        pat.setAddress(addressArea.getText().trim());
        pat.setBloodGroup(bloodGroupCombo.getValue());
        pat.setEmergencyContactName(emergencyContactField.getText().trim());
        pat.setEmergencyContactPhone(emergencyPhoneField.getText().trim());
        pat.setInsuranceProvider(insuranceField.getText().trim());
        pat.setAllergies(allergiesField.getText().trim());
        pat.setMedicalHistory(medicalHistoryArea.getText().trim());
        
        // Generate ID and patient ID if new
        if (!isEdit) {
            pat.setId(System.currentTimeMillis());
            pat.setPatientId("PAT" + String.format("%03d", (int)(Math.random() * 1000)));
            pat.setRegistrationDate(LocalDate.now());
            pat.setActive(true);
        }
        
        // Set photo path if selected
        if (photoPath != null) {
            pat.setPhotoPath(photoPath);
        }
        
        // Call save callback
        if (onSave != null) {
            onSave.accept(pat);
        }
        
        // Show success message
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Success");
        success.setHeaderText("Patient " + (isEdit ? "Updated" : "Registered"));
        success.setContentText("Patient " + pat.getFirstName() + " " + pat.getLastName() + 
                              " has been " + (isEdit ? "updated" : "registered") + " successfully!");
        success.showAndWait();
        
        stage.close();
    }
    
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        
        if (firstNameField.getText().trim().isEmpty()) {
            errors.append("• First name is required\n");
        }
        if (lastNameField.getText().trim().isEmpty()) {
            errors.append("• Last name is required\n");
        }
        if (dobPicker.getValue() == null) {
            errors.append("• Date of birth is required\n");
        }
        if (genderCombo.getValue() == null) {
            errors.append("• Gender is required\n");
        }
        if (emailField.getText().trim().isEmpty()) {
            errors.append("• Email is required\n");
        }
        if (phoneField.getText().trim().isEmpty()) {
            errors.append("• Phone number is required\n");
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
