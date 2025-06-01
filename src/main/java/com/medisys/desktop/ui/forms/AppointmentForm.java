package com.medisys.desktop.ui.forms;

import com.medisys.desktop.model.User;
import com.medisys.desktop.ui.modules.AppointmentsModule.Appointment;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Consumer;

/**
 * Complete Appointment Scheduling Form
 */
public class AppointmentForm {
    
    private final Stage stage;
    private final Appointment appointment;
    private final boolean isEdit;
    private final Consumer<Appointment> onSave;
    
    // Form fields
    private ComboBox<String> patientCombo;
    private ComboBox<String> doctorCombo;
    private DatePicker appointmentDate;
    private ComboBox<String> timeSlotCombo;
    private ComboBox<String> appointmentTypeCombo;
    private ComboBox<String> statusCombo;
    private TextArea notesArea;
    private TextField durationField;
    private ComboBox<String> priorityCombo;
    private CheckBox reminderCheckBox;
    private ComboBox<String> reminderTimeCombo;
    
    public AppointmentForm(Appointment appointment, Consumer<Appointment> onSave) {
        this.appointment = appointment;
        this.isEdit = appointment != null;
        this.onSave = onSave;
        this.stage = new Stage();
        
        initializeForm();
    }
    
    private void initializeForm() {
        stage.setTitle(isEdit ? "Edit Appointment" : "Schedule New Appointment");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("form-container");
        
        // Header
        Text header = new Text(isEdit ? "📝 Edit Appointment" : "📅 Schedule New Appointment");
        header.getStyleClass().add("form-header");
        
        // Form content
        ScrollPane scrollPane = new ScrollPane(createFormContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.getStyleClass().add("form-scroll");
        
        // Buttons
        HBox buttonBox = createButtonBox();
        
        root.getChildren().addAll(header, scrollPane, buttonBox);
        
        Scene scene = new Scene(root, 600, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/modern-theme.css").toExternalForm());
        stage.setScene(scene);
        
        // Load existing data if editing
        if (isEdit) {
            loadAppointmentData();
        }
    }
    
    private VBox createFormContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        
        // Patient Selection Section
        VBox patientSection = createSection("👤 Patient Information");
        patientCombo = new ComboBox<>();
        patientCombo.getStyleClass().add("modern-combo-box");
        patientCombo.setPromptText("Select Patient");
        patientCombo.setPrefWidth(400);
        patientCombo.getItems().addAll(
            "Alice Johnson (P001)",
            "Bob Wilson (P002)", 
            "Carol Davis (P003)",
            "David Brown (P004)",
            "Emma Wilson (P005)"
        );
        patientSection.getChildren().add(patientCombo);
        
        // Doctor Selection Section
        VBox doctorSection = createSection("👨‍⚕️ Doctor Information");
        doctorCombo = new ComboBox<>();
        doctorCombo.getStyleClass().add("modern-combo-box");
        doctorCombo.setPromptText("Select Doctor");
        doctorCombo.setPrefWidth(400);
        doctorCombo.getItems().addAll(
            "Dr. John Smith - Cardiology",
            "Dr. Sarah Johnson - Neurology",
            "Dr. Michael Brown - Orthopedics",
            "Dr. Lisa Davis - Pediatrics",
            "Dr. Robert Wilson - Emergency"
        );
        doctorSection.getChildren().add(doctorCombo);
        
        // Date & Time Section
        VBox dateTimeSection = createSection("📅 Date & Time");
        
        HBox dateTimeBox = new HBox(15);
        dateTimeBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox dateBox = new VBox(5);
        Text dateLabel = new Text("Appointment Date:");
        dateLabel.getStyleClass().add("field-label");
        appointmentDate = new DatePicker(LocalDate.now().plusDays(1));
        appointmentDate.getStyleClass().add("modern-date-picker");
        appointmentDate.setPrefWidth(180);
        dateBox.getChildren().addAll(dateLabel, appointmentDate);
        
        VBox timeBox = new VBox(5);
        Text timeLabel = new Text("Time Slot:");
        timeLabel.getStyleClass().add("field-label");
        timeSlotCombo = new ComboBox<>();
        timeSlotCombo.getStyleClass().add("modern-combo-box");
        timeSlotCombo.setPrefWidth(180);
        timeSlotCombo.getItems().addAll(
            "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM",
            "12:00 PM", "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM", "04:00 PM",
            "04:30 PM", "05:00 PM", "05:30 PM", "06:00 PM"
        );
        timeBox.getChildren().addAll(timeLabel, timeSlotCombo);
        
        dateTimeBox.getChildren().addAll(dateBox, timeBox);
        dateTimeSection.getChildren().add(dateTimeBox);
        
        // Appointment Details Section
        VBox detailsSection = createSection("📋 Appointment Details");
        
        HBox detailsBox = new HBox(15);
        detailsBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox typeBox = new VBox(5);
        Text typeLabel = new Text("Appointment Type:");
        typeLabel.getStyleClass().add("field-label");
        appointmentTypeCombo = new ComboBox<>();
        appointmentTypeCombo.getStyleClass().add("modern-combo-box");
        appointmentTypeCombo.setPrefWidth(180);
        appointmentTypeCombo.getItems().addAll(
            "Consultation", "Follow-up", "Surgery", "Emergency", 
            "Routine Checkup", "Vaccination", "Lab Tests", "Therapy"
        );
        typeBox.getChildren().addAll(typeLabel, appointmentTypeCombo);
        
        VBox durationBox = new VBox(5);
        Text durationLabel = new Text("Duration (minutes):");
        durationLabel.getStyleClass().add("field-label");
        durationField = new TextField("30");
        durationField.getStyleClass().add("modern-text-field");
        durationField.setPrefWidth(180);
        durationBox.getChildren().addAll(durationLabel, durationField);
        
        detailsBox.getChildren().addAll(typeBox, durationBox);
        detailsSection.getChildren().add(detailsBox);
        
        // Status & Priority Section
        VBox statusSection = createSection("⚡ Status & Priority");
        
        HBox statusBox = new HBox(15);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox statusVBox = new VBox(5);
        Text statusLabel = new Text("Status:");
        statusLabel.getStyleClass().add("field-label");
        statusCombo = new ComboBox<>();
        statusCombo.getStyleClass().add("modern-combo-box");
        statusCombo.setPrefWidth(180);
        statusCombo.getItems().addAll("Scheduled", "Confirmed", "In Progress", "Completed", "Cancelled");
        statusCombo.setValue("Scheduled");
        statusVBox.getChildren().addAll(statusLabel, statusCombo);
        
        VBox priorityBox = new VBox(5);
        Text priorityLabel = new Text("Priority:");
        priorityLabel.getStyleClass().add("field-label");
        priorityCombo = new ComboBox<>();
        priorityCombo.getStyleClass().add("modern-combo-box");
        priorityCombo.setPrefWidth(180);
        priorityCombo.getItems().addAll("Low", "Normal", "High", "Urgent");
        priorityCombo.setValue("Normal");
        priorityBox.getChildren().addAll(priorityLabel, priorityCombo);
        
        statusBox.getChildren().addAll(statusVBox, priorityBox);
        statusSection.getChildren().add(statusBox);
        
        // Reminder Section
        VBox reminderSection = createSection("🔔 Reminder Settings");
        
        reminderCheckBox = new CheckBox("Send reminder notification");
        reminderCheckBox.getStyleClass().add("modern-checkbox");
        reminderCheckBox.setSelected(true);
        
        HBox reminderBox = new HBox(10);
        reminderBox.setAlignment(Pos.CENTER_LEFT);
        Text reminderLabel = new Text("Remind before:");
        reminderLabel.getStyleClass().add("field-label");
        reminderTimeCombo = new ComboBox<>();
        reminderTimeCombo.getStyleClass().add("modern-combo-box");
        reminderTimeCombo.setPrefWidth(150);
        reminderTimeCombo.getItems().addAll("15 minutes", "30 minutes", "1 hour", "2 hours", "1 day");
        reminderTimeCombo.setValue("30 minutes");
        reminderBox.getChildren().addAll(reminderLabel, reminderTimeCombo);
        
        reminderSection.getChildren().addAll(reminderCheckBox, reminderBox);
        
        // Notes Section
        VBox notesSection = createSection("📝 Additional Notes");
        notesArea = new TextArea();
        notesArea.getStyleClass().add("modern-text-area");
        notesArea.setPromptText("Enter any additional notes or special instructions...");
        notesArea.setPrefRowCount(4);
        notesArea.setWrapText(true);
        notesSection.getChildren().add(notesArea);
        
        content.getChildren().addAll(
            patientSection, doctorSection, dateTimeSection, 
            detailsSection, statusSection, reminderSection, notesSection
        );
        
        return content;
    }
    
    private VBox createSection(String title) {
        VBox section = new VBox(10);
        section.getStyleClass().add("form-section");
        
        Text sectionTitle = new Text(title);
        sectionTitle.getStyleClass().add("section-title");
        
        section.getChildren().add(sectionTitle);
        return section;
    }
    
    private HBox createButtonBox() {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("secondary-button");
        cancelBtn.setOnAction(e -> stage.close());
        
        Button saveBtn = new Button(isEdit ? "Update Appointment" : "Schedule Appointment");
        saveBtn.getStyleClass().add("modern-button");
        saveBtn.setOnAction(e -> saveAppointment());
        
        buttonBox.getChildren().addAll(cancelBtn, saveBtn);
        return buttonBox;
    }
    
    private void loadAppointmentData() {
        if (appointment != null) {
            patientCombo.setValue(appointment.getPatientName());
            doctorCombo.setValue(appointment.getDoctorName());
            appointmentDate.setValue(appointment.getAppointmentDate());
            timeSlotCombo.setValue(appointment.getTimeSlot());
            appointmentTypeCombo.setValue(appointment.getAppointmentType());
            statusCombo.setValue(appointment.getStatus());
            notesArea.setText(appointment.getNotes());
        }
    }
    
    private void saveAppointment() {
        // Validate form
        if (!validateForm()) {
            return;
        }
        
        // Create or update appointment
        Appointment apt = appointment != null ? appointment : new Appointment();
        
        apt.setPatientName(patientCombo.getValue());
        apt.setDoctorName(doctorCombo.getValue());
        apt.setAppointmentDate(appointmentDate.getValue());
        apt.setTimeSlot(timeSlotCombo.getValue());
        apt.setAppointmentType(appointmentTypeCombo.getValue());
        apt.setStatus(statusCombo.getValue());
        apt.setNotes(notesArea.getText());
        
        // Generate ID if new appointment
        if (!isEdit) {
            apt.setId(System.currentTimeMillis());
        }
        
        // Call the save callback
        onSave.accept(apt);
        
        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(isEdit ? "Appointment updated successfully!" : "Appointment scheduled successfully!");
        alert.showAndWait();
        
        stage.close();
    }
    
    private boolean validateForm() {
        if (patientCombo.getValue() == null || patientCombo.getValue().isEmpty()) {
            showError("Please select a patient.");
            return false;
        }
        
        if (doctorCombo.getValue() == null || doctorCombo.getValue().isEmpty()) {
            showError("Please select a doctor.");
            return false;
        }
        
        if (appointmentDate.getValue() == null) {
            showError("Please select an appointment date.");
            return false;
        }
        
        if (appointmentDate.getValue().isBefore(LocalDate.now())) {
            showError("Appointment date cannot be in the past.");
            return false;
        }
        
        if (timeSlotCombo.getValue() == null || timeSlotCombo.getValue().isEmpty()) {
            showError("Please select a time slot.");
            return false;
        }
        
        if (appointmentTypeCombo.getValue() == null || appointmentTypeCombo.getValue().isEmpty()) {
            showError("Please select an appointment type.");
            return false;
        }
        
        return true;
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void show() {
        stage.show();
    }
}
