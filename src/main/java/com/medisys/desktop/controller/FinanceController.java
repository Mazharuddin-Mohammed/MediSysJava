package com.medisys.desktop.controller;

import com.medisys.desktop.model.Finance;
import com.medisys.desktop.model.User;
import com.medisys.desktop.service.FinanceService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class FinanceController {
    @FXML
    private TextField financeIdField;
    @FXML
    private TextField patientIdField;
    @FXML
    private TextField amountField;
    @FXML
    private TextField statusField;
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
    private ListView<String> financeListView;

    private final FinanceService financeService;
    private User loggedInUser;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        loadFinanceRecords();
    }

    @FXML
    private void initialize() {
        financeListView.setOnMouseClicked(event -> {
            String selected = financeListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Long id = Long.parseLong(selected.split(" - ")[0]);
                try {
                    Finance finance = financeService.getFinance(id, loggedInUser.getId());
                    financeIdField.setText(String.valueOf(finance.getId()));
                    patientIdField.setText(String.valueOf(finance.getPatientId()));
                    amountField.setText(String.valueOf(finance.getAmount()));
                    statusField.setText(finance.getStatus());
                } catch (Exception e) {
                    errorLabel.setText("Error fetching finance record: " + e.getMessage());
                }
            }
        });
    }

    private void loadFinanceRecords() {
        financeListView.getItems().clear();
        List<Finance> finances = financeService.getAllFinance(loggedInUser.getId());
        for (Finance finance : finances) {
            financeListView.getItems().add(finance.getId() + " - Patient ID: " + finance.getPatientId());
        }
    }

    @FXML
    private void handleCreateFinance() {
        try {
            Finance finance = new Finance();
            finance.setPatientId(Long.parseLong(patientIdField.getText()));
            finance.setAmount(Double.parseDouble(amountField.getText()));
            finance.setStatus(statusField.getText());
            financeService.createFinance(finance, loggedInUser.getId());
            errorLabel.setText("Finance record created successfully");
            loadFinanceRecords();
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Error creating finance record: " + e.getMessage());
        }
    }

    @FXML
    private void handleFetchFinance() {
        String financeId = financeIdField.getText().trim();
        if (financeId.isEmpty()) {
            errorLabel.setText("Finance ID is required");
            return;
        }

        try {
            Finance finance = financeService.getFinance(Long.parseLong(financeId), loggedInUser.getId());
            patientIdField.setText(String.valueOf(finance.getPatientId()));
            amountField.setText(String.valueOf(finance.getAmount()));
            statusField.setText(finance.getStatus());
            errorLabel.setText("");
        } catch (Exception e) {
            errorLabel.setText("Failed to fetch finance record: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateFinance() {
        try {
            Finance finance = new Finance();
            finance.setId(Long.parseLong(financeIdField.getText()));
            finance.setPatientId(Long.parseLong(patientIdField.getText()));
            finance.setAmount(Double.parseDouble(amountField.getText()));
            finance.setStatus(statusField.getText());
            financeService.updateFinance(finance, loggedInUser.getId());
            errorLabel.setText("Finance record updated successfully");
            loadFinanceRecords();
        } catch (Exception e) {
            errorLabel.setText("Error updating finance record: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteFinance() {
        String financeId = financeIdField.getText().trim();
        if (financeId.isEmpty()) {
            errorLabel.setText("Finance ID is required");
            return;
        }

        try {
            financeService.deleteFinance(Long.parseLong(financeId), loggedInUser.getId());
            errorLabel.setText("Finance record deleted successfully");
            loadFinanceRecords();
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Error deleting finance record: " + e.getMessage());
        }
    }

    private void clearFields() {
        financeIdField.clear();
        patientIdField.clear();
        amountField.clear();
        statusField.clear();
    }
}