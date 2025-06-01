package com.medisys.desktop.ui.forms;

import com.medisys.desktop.model.Doctor;
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
 * Complete Doctor Registration Form
 * Professional doctor registration with all required fields
 */
public class DoctorRegistrationForm {
    
    private final Stage stage;
    private final Doctor doctor;
    private final boolean isEdit;
    private final Consumer<Doctor> onSave;
    
    // Form fields
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField phoneField;
    private ComboBox<String> specializationCombo;
    private ComboBox<String> departmentCombo;
    private TextField licenseNumberField;
    private TextField experienceField;
    private TextField qualificationField;
    private TextField consultationFeeField;
    private TextArea addressArea;
    private CheckBox morningShiftBox;
    private CheckBox eveningShiftBox;
    private CheckBox mondayBox, tuesdayBox, wednesdayBox, thursdayBox, fridayBox, saturdayBox, sundayBox;
    private Label photoLabel;
    private String photoPath;
    
    public DoctorRegistrationForm(Doctor doctor, Consumer<Doctor> onSave) {
        this.doctor = doctor;
        this.isEdit = doctor != null;
        this.onSave = onSave;
        this.stage = new Stage();
        
        initializeForm();
    }
    
    private void initializeForm() {
        stage.setTitle(isEdit ? "Edit Doctor" : "Register New Doctor");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("form-container");
        
        // Header
        Text header = new Text(isEdit ? IconLibrary.EDIT + " Edit Doctor" : IconLibrary.DOCTOR + " Register New Doctor");
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
        
        Scene scene = new Scene(root, 750, 700);
        try {
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Could not load stylesheet");
        }
        stage.setScene(scene);
        
        // Load existing data if editing
        if (isEdit) {
            loadDoctorData();
        }
    }
    
    private VBox createFormContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        
        // Personal Information Section
        VBox personalSection = createSection(IconLibrary.DOCTOR + " Personal Information");
        
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
        addressArea.setPrefRowCount(2);
        addressArea.setWrapText(true);
        addressBox.getChildren().addAll(new Label("Address"), addressArea);
        
        personalSection.getChildren().addAll(nameRow, contactRow, addressBox);
        
        // Professional Information Section
        VBox professionalSection = createSection(IconLibrary.STETHOSCOPE + " Professional Information");
        
        HBox profRow1 = new HBox(15);
        profRow1.setAlignment(Pos.CENTER_LEFT);
        
        VBox specializationBox = new VBox(5);
        specializationCombo = new ComboBox<>();
        specializationCombo.getStyleClass().add("modern-combo-box");
        specializationCombo.getItems().addAll(
            "Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Dermatology",
            "Psychiatry", "Radiology", "Anesthesiology", "Emergency Medicine", "General Medicine"
        );
        specializationCombo.setPromptText("Select specialization");
        specializationBox.getChildren().addAll(new Label("Specialization *"), specializationCombo);
        
        VBox departmentBox = new VBox(5);
        departmentCombo = new ComboBox<>();
        departmentCombo.getStyleClass().add("modern-combo-box");
        departmentCombo.getItems().addAll(
            "Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Emergency",
            "Surgery", "Radiology", "Laboratory", "ICU", "General"
        );
        departmentCombo.setPromptText("Select department");
        departmentBox.getChildren().addAll(new Label("Department *"), departmentCombo);
        
        profRow1.getChildren().addAll(specializationBox, departmentBox);
        HBox.setHgrow(specializationBox, Priority.ALWAYS);
        HBox.setHgrow(departmentBox, Priority.ALWAYS);
        
        HBox profRow2 = new HBox(15);
        profRow2.setAlignment(Pos.CENTER_LEFT);
        
        VBox licenseBox = new VBox(5);
        licenseBox.getChildren().addAll(
            new Label("License Number *"),
            licenseNumberField = createTextField("Enter medical license number")
        );
        
        VBox experienceBox = new VBox(5);
        experienceBox.getChildren().addAll(
            new Label("Experience (Years) *"),
            experienceField = createTextField("Enter years of experience")
        );
        
        profRow2.getChildren().addAll(licenseBox, experienceBox);
        HBox.setHgrow(licenseBox, Priority.ALWAYS);
        HBox.setHgrow(experienceBox, Priority.ALWAYS);
        
        HBox profRow3 = new HBox(15);
        profRow3.setAlignment(Pos.CENTER_LEFT);
        
        VBox qualificationBox = new VBox(5);
        qualificationBox.getChildren().addAll(
            new Label("Qualification *"),
            qualificationField = createTextField("Enter medical qualification (MBBS, MD, etc.)")
        );
        
        VBox feeBox = new VBox(5);
        feeBox.getChildren().addAll(
            new Label("Consultation Fee (₹) *"),
            consultationFeeField = createTextField("Enter consultation fee")
        );
        
        profRow3.getChildren().addAll(qualificationBox, feeBox);
        HBox.setHgrow(qualificationBox, Priority.ALWAYS);
        HBox.setHgrow(feeBox, Priority.ALWAYS);
        
        professionalSection.getChildren().addAll(profRow1, profRow2, profRow3);
        
        // Schedule Information Section
        VBox scheduleSection = createSection(IconLibrary.CALENDAR + " Schedule Information");
        
        // Shift timings
        VBox shiftBox = new VBox(10);
        Text shiftLabel = new Text("Available Shifts:");
        shiftLabel.setStyle("-fx-font-weight: bold;");
        
        HBox shiftsRow = new HBox(20);
        shiftsRow.setAlignment(Pos.CENTER_LEFT);
        
        morningShiftBox = new CheckBox("Morning Shift (9:00 AM - 1:00 PM)");
        eveningShiftBox = new CheckBox("Evening Shift (5:00 PM - 9:00 PM)");
        
        shiftsRow.getChildren().addAll(morningShiftBox, eveningShiftBox);
        shiftBox.getChildren().addAll(shiftLabel, shiftsRow);
        
        // Working days
        VBox daysBox = new VBox(10);
        Text daysLabel = new Text("Working Days:");
        daysLabel.setStyle("-fx-font-weight: bold;");
        
        HBox daysRow1 = new HBox(15);
        daysRow1.setAlignment(Pos.CENTER_LEFT);
        
        mondayBox = new CheckBox("Monday");
        tuesdayBox = new CheckBox("Tuesday");
        wednesdayBox = new CheckBox("Wednesday");
        thursdayBox = new CheckBox("Thursday");
        
        daysRow1.getChildren().addAll(mondayBox, tuesdayBox, wednesdayBox, thursdayBox);
        
        HBox daysRow2 = new HBox(15);
        daysRow2.setAlignment(Pos.CENTER_LEFT);
        
        fridayBox = new CheckBox("Friday");
        saturdayBox = new CheckBox("Saturday");
        sundayBox = new CheckBox("Sunday");
        
        daysRow2.getChildren().addAll(fridayBox, saturdayBox, sundayBox);
        
        daysBox.getChildren().addAll(daysLabel, daysRow1, daysRow2);
        
        scheduleSection.getChildren().addAll(shiftBox, daysBox);
        
        // Photo Section
        VBox photoSection = createSection(IconLibrary.DOCTOR + " Doctor Photo");
        
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
            personalSection, professionalSection, scheduleSection, photoSection
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
        
        Button saveBtn = new Button(IconLibrary.SAVE + " " + (isEdit ? "Update" : "Register") + " Doctor");
        saveBtn.getStyleClass().add("primary-button");
        saveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        saveBtn.setOnAction(e -> saveDoctor());
        
        buttonBox.getChildren().addAll(cancelBtn, saveBtn);
        
        return buttonBox;
    }
    
    private void selectPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Doctor Photo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            photoPath = selectedFile.getAbsolutePath();
            photoLabel.setText("Photo: " + selectedFile.getName());
        }
    }
    
    private void loadDoctorData() {
        if (doctor != null) {
            firstNameField.setText(doctor.getFirstName());
            lastNameField.setText(doctor.getLastName());
            emailField.setText(doctor.getEmail());
            phoneField.setText(doctor.getPhone());
            specializationCombo.setValue(doctor.getSpecialization());
            departmentCombo.setValue(doctor.getDepartment());
            licenseNumberField.setText(doctor.getLicenseNumber());
            experienceField.setText(String.valueOf(doctor.getExperience()));
            qualificationField.setText(doctor.getQualification());
            consultationFeeField.setText(String.valueOf(doctor.getConsultationFee()));
            addressArea.setText(doctor.getAddress());
            
            // Load schedule data
            if (doctor.getAvailableShifts() != null) {
                morningShiftBox.setSelected(doctor.getAvailableShifts().contains("Morning"));
                eveningShiftBox.setSelected(doctor.getAvailableShifts().contains("Evening"));
            }
            
            if (doctor.getWorkingDays() != null) {
                mondayBox.setSelected(doctor.getWorkingDays().contains("Monday"));
                tuesdayBox.setSelected(doctor.getWorkingDays().contains("Tuesday"));
                wednesdayBox.setSelected(doctor.getWorkingDays().contains("Wednesday"));
                thursdayBox.setSelected(doctor.getWorkingDays().contains("Thursday"));
                fridayBox.setSelected(doctor.getWorkingDays().contains("Friday"));
                saturdayBox.setSelected(doctor.getWorkingDays().contains("Saturday"));
                sundayBox.setSelected(doctor.getWorkingDays().contains("Sunday"));
            }
        }
    }
    
    private void saveDoctor() {
        // Validate form
        if (!validateForm()) {
            return;
        }
        
        // Create or update doctor
        Doctor doc = doctor != null ? doctor : new Doctor();
        
        doc.setFirstName(firstNameField.getText().trim());
        doc.setLastName(lastNameField.getText().trim());
        doc.setEmail(emailField.getText().trim());
        doc.setPhone(phoneField.getText().trim());
        doc.setSpecialization(specializationCombo.getValue());
        doc.setDepartment(departmentCombo.getValue());
        doc.setLicenseNumber(licenseNumberField.getText().trim());
        doc.setExperience(Integer.parseInt(experienceField.getText().trim()));
        doc.setQualification(qualificationField.getText().trim());
        doc.setConsultationFee(Double.parseDouble(consultationFeeField.getText().trim()));
        doc.setAddress(addressArea.getText().trim());
        
        // Set schedule
        StringBuilder shifts = new StringBuilder();
        if (morningShiftBox.isSelected()) shifts.append("Morning,");
        if (eveningShiftBox.isSelected()) shifts.append("Evening,");
        if (shifts.length() > 0) {
            doc.setAvailableShifts(shifts.toString().replaceAll(",$", ""));
        }
        
        StringBuilder days = new StringBuilder();
        if (mondayBox.isSelected()) days.append("Monday,");
        if (tuesdayBox.isSelected()) days.append("Tuesday,");
        if (wednesdayBox.isSelected()) days.append("Wednesday,");
        if (thursdayBox.isSelected()) days.append("Thursday,");
        if (fridayBox.isSelected()) days.append("Friday,");
        if (saturdayBox.isSelected()) days.append("Saturday,");
        if (sundayBox.isSelected()) days.append("Sunday,");
        if (days.length() > 0) {
            doc.setWorkingDays(days.toString().replaceAll(",$", ""));
        }
        
        // Generate ID if new
        if (!isEdit) {
            doc.setId(System.currentTimeMillis());
            doc.setDoctorId("DOC" + String.format("%03d", (int)(Math.random() * 1000)));
            doc.setJoiningDate(LocalDate.now());
            doc.setActive(true);
        }
        
        // Set photo path if selected
        if (photoPath != null) {
            doc.setPhotoPath(photoPath);
        }
        
        // Call save callback
        if (onSave != null) {
            onSave.accept(doc);
        }
        
        // Show success message
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Success");
        success.setHeaderText("Doctor " + (isEdit ? "Updated" : "Registered"));
        success.setContentText("Dr. " + doc.getFirstName() + " " + doc.getLastName() + 
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
        if (emailField.getText().trim().isEmpty()) {
            errors.append("• Email is required\n");
        }
        if (phoneField.getText().trim().isEmpty()) {
            errors.append("• Phone number is required\n");
        }
        if (specializationCombo.getValue() == null) {
            errors.append("• Specialization is required\n");
        }
        if (departmentCombo.getValue() == null) {
            errors.append("• Department is required\n");
        }
        if (licenseNumberField.getText().trim().isEmpty()) {
            errors.append("• License number is required\n");
        }
        if (experienceField.getText().trim().isEmpty()) {
            errors.append("• Experience is required\n");
        } else {
            try {
                Integer.parseInt(experienceField.getText().trim());
            } catch (NumberFormatException e) {
                errors.append("• Experience must be a valid number\n");
            }
        }
        if (qualificationField.getText().trim().isEmpty()) {
            errors.append("• Qualification is required\n");
        }
        if (consultationFeeField.getText().trim().isEmpty()) {
            errors.append("• Consultation fee is required\n");
        } else {
            try {
                Double.parseDouble(consultationFeeField.getText().trim());
            } catch (NumberFormatException e) {
                errors.append("• Consultation fee must be a valid number\n");
            }
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
