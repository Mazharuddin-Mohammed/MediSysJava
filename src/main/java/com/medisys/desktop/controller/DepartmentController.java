package com.medisys.desktop.controller;

import com.medisys.desktop.model.Department;
import com.medisys.desktop.model.User;
import com.medisys.desktop.service.DepartmentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class DepartmentController {
    @FXML
    private TextField departmentIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField headField;
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
    private ListView<String> departmentListView;

    private final DepartmentService departmentService;
    private User loggedInUser;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        loadDepartments();
    }

    @FXML
    private void initialize() {
        departmentListView.setOnMouseClicked(event -> {
            String selected = departmentListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Long id = Long.parseLong(selected.split(" - ")[0]);
                try {
                    Department department = departmentService.getDepartment(id, loggedInUser.getId());
                    departmentIdField.setText(String.valueOf(department.getId()));
                    nameField.setText(department.getName());
                    headField.setText(department.getHeadOfDepartment());
                } catch (Exception e) {
                    errorLabel.setText("Error fetching department: " + e.getMessage());
                }
            }
        });
    }

    private void loadDepartments() {
        departmentListView.getItems().clear();
        List<Department> departments = departmentService.getAllDepartments(loggedInUser.getId());
        for (Department department : departments) {
            departmentListView.getItems().add(department.getId() + " - " + department.getName());
        }
    }

    @FXML
    private void handleCreateDepartment() {
        try {
            Department department = new Department();
            department.setName(nameField.getText());
            department.setHeadOfDepartment(headField.getText());
            departmentService.createDepartment(department, loggedInUser.getId());
            errorLabel.setText("Department created successfully");
            loadDepartments();
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Error creating department: " + e.getMessage());
        }
    }

    @FXML
    private void handleFetchDepartment() {
        String departmentId = departmentIdField.getText().trim();
        if (departmentId.isEmpty()) {
            errorLabel.setText("Department ID is required");
            return;
        }

        try {
            Department department = departmentService.getDepartment(Long.parseLong(departmentId), loggedInUser.getId());
            nameField.setText(department.getName());
            headField.setText(department.getHeadOfDepartment());
            errorLabel.setText("");
        } catch (Exception e) {
            errorLabel.setText("Failed to fetch department: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateDepartment() {
        try {
            Department department = new Department();
            department.setId(Long.parseLong(departmentIdField.getText()));
            department.setName(nameField.getText());
            department.setHeadOfDepartment(headField.getText());
            departmentService.updateDepartment(department, loggedInUser.getId());
            errorLabel.setText("Department updated successfully");
            loadDepartments();
        } catch (Exception e) {
            errorLabel.setText("Error updating department: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteDepartment() {
        String departmentId = departmentIdField.getText().trim();
        if (departmentId.isEmpty()) {
            errorLabel.setText("Department ID is required");
            return;
        }

        try {
            departmentService.deleteDepartment(Long.parseLong(departmentId), loggedInUser.getId());
            errorLabel.setText("Department deleted successfully");
            loadDepartments();
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Error deleting department: " + e.getMessage());
        }
    }

    private void clearFields() {
        departmentIdField.clear();
        nameField.clear();
        headField.clear();
    }
}