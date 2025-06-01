package com.medisys.desktop;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Simple test application to verify JavaFX functionality
 */
public class TestApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MediSys - Test Application");

        // Create the login form
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Title
        Label titleLabel = new Label("MediSys Healthcare Management System");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        grid.add(titleLabel, 0, 0, 2, 1);

        // Username field
        Label userLabel = new Label("Username:");
        grid.add(userLabel, 0, 1);
        TextField userTextField = new TextField();
        userTextField.setText("admin"); // Pre-fill for testing
        grid.add(userTextField, 1, 1);

        // Password field
        Label pwLabel = new Label("Password:");
        grid.add(pwLabel, 0, 2);
        PasswordField pwBox = new PasswordField();
        pwBox.setText("admin123"); // Pre-fill for testing
        grid.add(pwBox, 1, 2);

        // Login button
        Button loginBtn = new Button("Login");
        loginBtn.setDefaultButton(true);
        grid.add(loginBtn, 1, 3);

        // Status label
        Label statusLabel = new Label();
        grid.add(statusLabel, 0, 4, 2, 1);

        // Login button action
        loginBtn.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            
            if ("admin".equals(username) && "admin123".equals(password)) {
                statusLabel.setText("✅ Login successful! Welcome to MediSys.");
                statusLabel.setStyle("-fx-text-fill: green;");
                
                // Show main application window
                showMainWindow();
            } else {
                statusLabel.setText("❌ Invalid credentials. Try admin/admin123");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("✅ MediSys Test Application started successfully!");
        System.out.println("Default credentials: admin / admin123");
    }

    private void showMainWindow() {
        Stage mainStage = new Stage();
        mainStage.setTitle("MediSys - Main Dashboard");

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome to MediSys Healthcare Management System");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label statusLabel = new Label("🎉 Application is running successfully!");
        statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");

        // Module buttons
        Button patientsBtn = new Button("👥 Patients Module");
        patientsBtn.setPrefWidth(200);
        patientsBtn.setOnAction(e -> showAlert("Patients Module", "Patients management functionality would be here."));

        Button doctorsBtn = new Button("👨‍⚕️ Doctors Module");
        doctorsBtn.setPrefWidth(200);
        doctorsBtn.setOnAction(e -> showAlert("Doctors Module", "Doctors management functionality would be here."));

        Button appointmentsBtn = new Button("📅 Appointments Module");
        appointmentsBtn.setPrefWidth(200);
        appointmentsBtn.setOnAction(e -> showAlert("Appointments Module", "Appointments scheduling functionality would be here."));

        Button financeBtn = new Button("💰 Finance Module");
        financeBtn.setPrefWidth(200);
        financeBtn.setOnAction(e -> showAlert("Finance Module", "Financial management functionality would be here."));

        Button reportsBtn = new Button("📊 Reports Module");
        reportsBtn.setPrefWidth(200);
        reportsBtn.setOnAction(e -> showAlert("Reports Module", "Reports and analytics functionality would be here."));

        // System info
        Label systemInfo = new Label(
            "System Status: ✅ Running\n" +
            "Java Version: " + System.getProperty("java.version") + "\n" +
            "JavaFX Version: " + System.getProperty("javafx.version", "Unknown") + "\n" +
            "OS: " + System.getProperty("os.name")
        );
        systemInfo.setStyle("-fx-font-family: monospace; -fx-background-color: #f0f0f0; -fx-padding: 10;");

        mainLayout.getChildren().addAll(
            welcomeLabel,
            statusLabel,
            new Separator(),
            patientsBtn,
            doctorsBtn,
            appointmentsBtn,
            financeBtn,
            reportsBtn,
            new Separator(),
            systemInfo
        );

        Scene mainScene = new Scene(mainLayout, 500, 600);
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        System.out.println("Starting MediSys Test Application...");
        launch(args);
    }
}
