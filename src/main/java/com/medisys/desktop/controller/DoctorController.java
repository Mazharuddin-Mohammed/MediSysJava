package com.medisys.desktop.controller;

import com.medisys.desktop.model.Doctor;
import com.medisys.desktop.model.User;
import com.medisys.desktop.service.DoctorService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class DoctorController {
    @FXML
    private TextField doctorIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField specialtyField;
    @FXML
    private TextField contactField;
    @FXML
    private TextField departmentIdField;
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
    private ListView<String> doctorListView;

    private final DoctorService doctorService;
    private User loggedInUser;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        loadDoctors();
    }

    @FXML
    private void initialize() {
        doctorListView.setOnMouseClicked(event -> {
            String selected = doctorListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Long id = Long.parseLong(selected.split(" - ")[0]);
                try {
                    Doctor doctor = doctorService.getDoctor(id, loggedInUser.getId());
                    doctorIdField.setText(String.valueOf(doctor.getId()));
                    nameField.setText(doctor.getName());
                    specialtyField.setText(doctor.getSpecialty());
                    contactField.setText(doctor.getContactInfo());
                    departmentIdField.setText(String.valueOf(doctor.getDepartmentId()));
                } catch (Exception e) {
                    errorLabel.setText("Error fetching doctor: " + e.getMessage());
                }
            }
        });
    }

    private void loadDoctors() {
        doctorListView.getItems().clear();
        List<Doctor> doctors = doctorService.getAllDoctors(loggedInUser.getId());
        for (Doctor doctor : doctors) {
            doctorListView.getItems().add(doctor.getId() + " - " + doctor.getName());
        }
    }

    @FXML
    private void handleCreateDoctor() {
        try {
            Doctor doctor = new Doctor();
            doctor.setName(nameField.getText());
            doctor.setSpecialty(specialtyField.getText());
            doctor.setContactInfo(contactField.getText());
            doctor.setDepartmentId(Long.parseLong(departmentIdField.getText()));
            doctorService.createDoctor(doctor, loggedInUser.getId());
            errorLabel.setText("Doctor created successfully");
            loadDoctors();
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Error creating doctor: " + e.getMessage());
        }
    }

    @FXML
    private void handleFetchDoctor() {
        String doctorId = doctorIdField.getText().trim();
        if (doctorId.isEmpty()) {
            errorLabel.setText("Doctor ID is required");
            return;
        }

        try {
            Doctor doctor = doctorService.getDoctor(Long.parseLong(doctorId), loggedInUser.getId());
            nameField.setText(doctor.getName());
            specialtyField.setText(doctor.getSpecialty());
            contactField.setText(doctor.getContactInfo());
            departmentIdField.setText(String.valueOf(doctor.getDepartmentId()));
            errorLabel.setText("");
        } catch (Exception e) {
            errorLabel.setText("Failed to fetch doctor: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateDoctor() {
        try {
            Doctor doctor = new Doctor();
            doctor.setId(Long.parseLong(doctorIdField.getText()));
            doctor.setName(nameField.getText());
            doctor.setSpecialty(specialtyField.getText());
            doctor.setContactInfo(contactField.getText());
            doctor.setDepartmentId(Long.parseLong(departmentIdField.getText()));
            doctorService.updateDoctor(doctor, loggedInUser.getId());
            errorLabel.setText("Doctor updated successfully");
            loadDoctors();
        } catch (Exception e) {
            errorLabel.setText("Error updating doctor: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteDoctor() {
        String doctorId = doctorIdField.getText().trim();
        if (doctorId.isEmpty()) {
            errorLabel.setText("Doctor ID is required");
            return;
        }

        try {
            doctorService.deleteDoctor(Long.parseLong(doctorId), loggedInUser.getId());
            errorLabel.setText("Doctor deleted successfully");
            loadDoctors();
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Error deleting doctor: " + e.getMessage());
        }
    }

    private void clearFields() {
        doctorIdField.clear();
        nameField.clear();
        specialtyField.clear();
        contactField.clear();
        departmentIdField.clear();
    }
}