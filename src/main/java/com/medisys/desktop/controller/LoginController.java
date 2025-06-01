package com.medisys.desktop.controller;

import com.medisys.desktop.model.User;
import com.medisys.desktop.service.AuthService;
import com.medisys.desktop.service.PatientService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private final AuthService authService;
    private final ApplicationContext applicationContext;
    private User loggedInUser;

    public LoginController(AuthService authService, ApplicationContext applicationContext) {
        this.authService = authService;
        this.applicationContext = applicationContext;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password are required");
            return;
        }

        try {
            loggedInUser = authService.authenticate(username, password);
            errorLabel.setText("Login successful");

            // Load the patient dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/patient.fxml"));
            PatientService patientService = applicationContext.getBean(PatientService.class);
            PatientController controller = new PatientController(patientService);
            controller.setLoggedInUser(loggedInUser);
            loader.setController(controller);
            Scene scene = new Scene(loader.load(), 600, 400);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("MediSys - Patient Dashboard");
            stage.show();
        } catch (RuntimeException e) {
            errorLabel.setText("Login failed: " + e.getMessage());
        } catch (IOException e) {
            errorLabel.setText("Error loading dashboard: " + e.getMessage());
        }
    }
}