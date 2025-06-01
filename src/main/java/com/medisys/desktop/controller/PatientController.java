package com.medisys.desktop.controller;

import com.medisys.desktop.model.Patient;
import com.medisys.desktop.model.User;
import com.medisys.desktop.service.PatientService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class PatientController {
    @FXML
    private TextField patientIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField dobField;
    @FXML
    private TextField contactField;
    @FXML
    private Button createButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button fetchButton;
    @FXML
    private Label errorLabel;
    @FXML
    private ListView<String> patientListView;

    private final PatientService patientService;
    private User loggedInUser;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        loadPatients();
    }

    @FXML
    private void initialize() {
        patientListView.setOnMouseClicked(event -> {
            String selected = patientListView.getSelectionModel().getSelectedItem();
            if (selected != null && loggedInUser != null) {
                try {
                    Long id = Long.parseLong(selected.split(" - ")[0]);
                    Patient patient = patientService.getPatient(id, loggedInUser.getId());
                    if (patient != null) {
                        patientIdField.setText(String.valueOf(patient.getId()));
                        nameField.setText(patient.getName() != null ? patient.getName() : "");
                        dobField.setText(patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : "");
                        contactField.setText(patient.getContactInfo() != null ? patient.getContactInfo() : "");
                        errorLabel.setText("");
                    }
                } catch (NumberFormatException e) {
                    errorLabel.setText("Invalid patient ID format");
                } catch (Exception e) {
                    errorLabel.setText("Error fetching patient: " + e.getMessage());
                }
            } else if (loggedInUser == null) {
                errorLabel.setText("User not logged in");
            }
        });
    }

    private void loadPatients() {
        patientListView.getItems().clear();
        List<Patient> patients = patientService.getAllPatients(loggedInUser.getId());
        for (Patient patient : patients) {
            patientListView.getItems().add(patient.getId() + " - " + patient.getName());
        }
    }

    @FXML
    private void handleCreatePatient() {
        try {
            Patient patient = new Patient();
            patient.setName(nameField.getText());
            patient.setDateOfBirth(dobField.getText());
            patient.setContactInfo(contactField.getText());
            patientService.createPatient(patient, loggedInUser.getId());
            errorLabel.setText("Patient created successfully");
            loadPatients();
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Error creating patient: " + e.getMessage());
        }
    }

    @FXML
    private void handleFetchPatient() {
        String patientId = patientIdField.getText().trim();
        if (patientId.isEmpty()) {
            errorLabel.setText("Patient ID is required");
            return;
        }

        try {
            Patient patient = patientService.getPatient(Long.parseLong(patientId), loggedInUser.getId());
            nameField.setText(patient.getName());
            dobField.setText(patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : "");
            contactField.setText(patient.getContactInfo());
            errorLabel.setText("");
        } catch (Exception e) {
            errorLabel.setText("Failed to fetch patient: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdatePatient() {
        try {
            Patient patient = new Patient();
            patient.setId(Long.parseLong(patientIdField.getText()));
            patient.setName(nameField.getText());
            patient.setDateOfBirth(dobField.getText());
            patient.setContactInfo(contactField.getText());
            patientService.updatePatient(patient, loggedInUser.getId());
            errorLabel.setText("Patient updated successfully");
            loadPatients();
        } catch (Exception e) {
            errorLabel.setText("Error updating patient: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeletePatient() {
        String patientId = patientIdField.getText().trim();
        if (patientId.isEmpty()) {
            errorLabel.setText("Patient ID is required");
            return;
        }

        try {
            patientService.deletePatient(Long.parseLong(patientId), loggedInUser.getId());
            errorLabel.setText("Patient deleted successfully");
            loadPatients();
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Error deleting patient: " + e.getMessage());
        }
    }

    private void clearFields() {
        patientIdField.clear();
        nameField.clear();
        dobField.clear();
        contactField.clear();
    }
}