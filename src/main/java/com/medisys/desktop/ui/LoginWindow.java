package com.medisys.desktop.ui;

import com.medisys.desktop.MediSysApp;
import com.medisys.desktop.model.User;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Modern Login Window with vibrant design
 */
public class LoginWindow {
    
    private final MediSysApp app;
    private final BorderPane root;
    // private final AuthService authService;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label statusLabel;
    private Button loginButton;
    
    public LoginWindow(MediSysApp app) {
        this.app = app;
        // this.authService = new AuthService();
        this.root = new BorderPane();
        
        initializeUI();
        setupEventHandlers();
        animateEntrance();
    }
    
    private void initializeUI() {
        root.getStyleClass().add("root");
        
        // Create main container
        HBox mainContainer = new HBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setSpacing(0);
        
        // Left side - Branding
        VBox leftSide = createBrandingSide();
        leftSide.setPrefWidth(500);
        
        // Right side - Login form
        VBox rightSide = createLoginForm();
        rightSide.setPrefWidth(500);
        
        mainContainer.getChildren().addAll(leftSide, rightSide);
        root.setCenter(mainContainer);
    }
    
    private VBox createBrandingSide() {
        VBox brandingSide = new VBox(30);
        brandingSide.setAlignment(Pos.CENTER);
        brandingSide.setPadding(new Insets(60));
        brandingSide.setStyle("""
            -fx-background-color: #1A202C;
            """);
        
        // Logo/Icon with actual logo image
        VBox logoContainer = new VBox(10);
        logoContainer.setAlignment(Pos.CENTER);

        try {
            // Try to load the actual logo image
            javafx.scene.image.Image logoImage = new javafx.scene.image.Image(
                getClass().getResourceAsStream("/images/logo.jpg")
            );

            // Create styled container for logo
            VBox logoBox = new VBox(5);
            logoBox.setAlignment(Pos.CENTER);
            logoBox.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 60;
                -fx-border-radius: 60;
                -fx-padding: 20;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);
                """);

            // Logo image
            javafx.scene.image.ImageView logoImageView = new javafx.scene.image.ImageView(logoImage);
            logoImageView.setFitWidth(80);
            logoImageView.setFitHeight(80);
            logoImageView.setPreserveRatio(true);
            logoImageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");

            // MediSys text
            Text logoText = new Text("MediSys");
            logoText.setStyle("""
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-fill: #3182CE;
                """);

            logoBox.getChildren().addAll(logoImageView, logoText);
            logoContainer.getChildren().add(logoBox);

        } catch (Exception e) {
            // Fallback to styled logo using CSS if image not found
            VBox logoBox = new VBox(5);
            logoBox.setAlignment(Pos.CENTER);
            logoBox.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 60;
                -fx-border-radius: 60;
                -fx-padding: 20;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);
                """);

            // Medical cross symbol
            Text crossSymbol = new Text("✚");
            crossSymbol.setStyle("""
                -fx-font-size: 48px;
                -fx-fill: #3182CE;
                """);

            // MediSys text
            Text logoText = new Text("MediSys");
            logoText.setStyle("""
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-fill: #3182CE;
                """);

            logoBox.getChildren().addAll(crossSymbol, logoText);
            logoContainer.getChildren().add(logoBox);
            System.out.println("⚠️ Logo image not found, using fallback design");
        }
        
        // Title
        Text title = new Text("MediSys");
        title.setStyle("""
            -fx-font-size: 36px;
            -fx-font-weight: 600;
            -fx-fill: white;
            """);

        // Subtitle
        Text subtitle = new Text("Enterprise Healthcare Management");
        subtitle.setStyle("""
            -fx-font-size: 16px;
            -fx-fill: rgba(255,255,255,0.85);
            """);
        
        // Features
        VBox features = new VBox(15);
        features.setAlignment(Pos.CENTER_LEFT);
        features.setPadding(new Insets(30, 0, 0, 0));
        
        String[] featureList = {
            "• Comprehensive Patient Records",
            "• Clinical Workflow Management",
            "• Financial & Billing Integration",
            "• Advanced Reporting & Analytics",
            "• HIPAA Compliant & Secure"
        };
        
        for (String feature : featureList) {
            Text featureText = new Text(feature);
            featureText.setStyle("""
                -fx-font-size: 14px;
                -fx-fill: rgba(255,255,255,0.9);
                -fx-font-weight: 400;
                """);
            features.getChildren().add(featureText);
        }
        
        brandingSide.getChildren().addAll(logoContainer, title, subtitle, features);
        
        return brandingSide;
    }
    
    private VBox createLoginForm() {
        VBox loginForm = new VBox(25);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setPadding(new Insets(60));
        loginForm.setStyle("-fx-background-color: #FFFFFF;");
        
        // Welcome text
        Text welcomeText = new Text("Welcome Back!");
        welcomeText.getStyleClass().add("header-title");
        
        Text subtitleText = new Text("Please sign in to your account");
        subtitleText.getStyleClass().add("header-subtitle");
        
        // Form container
        VBox formContainer = new VBox(20);
        formContainer.setMaxWidth(350);
        formContainer.setAlignment(Pos.CENTER);
        
        // Username field
        VBox usernameContainer = new VBox(8);
        Label usernameLabel = new Label("Username");
        usernameLabel.getStyleClass().add("form-label");
        
        usernameField = new TextField();
        usernameField.getStyleClass().add("modern-text-field");
        usernameField.setPromptText("Enter your username");
        usernameField.setText("admin"); // Pre-fill for demo
        
        usernameContainer.getChildren().addAll(usernameLabel, usernameField);
        
        // Password field
        VBox passwordContainer = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("form-label");
        
        passwordField = new PasswordField();
        passwordField.getStyleClass().add("modern-password-field");
        passwordField.setPromptText("Enter your password");
        passwordField.setText("admin123"); // Pre-fill for demo
        
        passwordContainer.getChildren().addAll(passwordLabel, passwordField);
        
        // Login button
        loginButton = new Button("Sign In");
        loginButton.getStyleClass().add("modern-button");
        loginButton.setPrefWidth(350);
        loginButton.setPrefHeight(50);
        loginButton.setDefaultButton(true);
        
        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("""
            -fx-font-size: 14px;
            -fx-text-fill: #FF6B6B;
            """);
        statusLabel.setVisible(false);
        
        // Demo credentials info
        VBox demoInfo = new VBox(5);
        demoInfo.setAlignment(Pos.CENTER);
        demoInfo.setStyle("""
            -fx-background-color: #E3F2FD;
            -fx-background-radius: 8;
            -fx-padding: 15;
            """);
        
        Text demoTitle = new Text("Demo Credentials");
        demoTitle.setStyle("""
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-fill: #2E86AB;
            """);
        
        Text demoText = new Text("Username: admin | Password: admin123");
        demoText.setStyle("""
            -fx-font-size: 12px;
            -fx-fill: #7F8C8D;
            """);
        
        demoInfo.getChildren().addAll(demoTitle, demoText);
        
        formContainer.getChildren().addAll(
            usernameContainer,
            passwordContainer,
            loginButton,
            statusLabel,
            demoInfo
        );
        
        loginForm.getChildren().addAll(welcomeText, subtitleText, formContainer);
        
        return loginForm;
    }
    
    private void setupEventHandlers() {
        loginButton.setOnAction(e -> handleLogin());
        
        // Enter key handling
        passwordField.setOnAction(e -> handleLogin());
        usernameField.setOnAction(e -> passwordField.requestFocus());
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both username and password", false);
            return;
        }
        
        // Disable login button during authentication
        loginButton.setDisable(true);
        loginButton.setText("Signing In...");
        
        // Simulate authentication (replace with actual authentication)
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate network delay
                
                // Mock authentication
                User user = authenticateUser(username, password);
                
                javafx.application.Platform.runLater(() -> {
                    if (user != null) {
                        showStatus("Login successful! Welcome " + user.getFullName(), true);
                        
                        // Animate transition to main dashboard
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), root);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.setOnFinished(event -> app.showMainDashboard(user));
                        fadeOut.play();
                    } else {
                        showStatus("Invalid username or password", false);
                        loginButton.setDisable(false);
                        loginButton.setText("Sign In");
                    }
                });
            } catch (InterruptedException ex) {
                javafx.application.Platform.runLater(() -> {
                    showStatus("Login failed. Please try again.", false);
                    loginButton.setDisable(false);
                    loginButton.setText("Sign In");
                });
            }
        }).start();
    }
    
    private User authenticateUser(String username, String password) {
        // Mock authentication - replace with actual database authentication
        if ("admin".equals(username) && "admin123".equals(password)) {
            User user = new User(1L, "admin", "hashed_password", "admin");
            user.setFirstName("System");
            user.setLastName("Administrator");
            user.setEmail("admin@medisys.com");
            return user;
        } else if ("doctor".equals(username) && "admin123".equals(password)) {
            User user = new User(2L, "doctor", "hashed_password", "doctor");
            user.setFirstName("Dr. John");
            user.setLastName("Smith");
            user.setEmail("john.smith@medisys.com");
            return user;
        } else if ("finance".equals(username) && "admin123".equals(password)) {
            User user = new User(3L, "finance", "hashed_password", "finance");
            user.setFirstName("Sarah");
            user.setLastName("Johnson");
            user.setEmail("sarah.johnson@medisys.com");
            return user;
        }
        return null;
    }
    
    private void showStatus(String message, boolean isSuccess) {
        statusLabel.setText(message);
        statusLabel.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-text-fill: " + (isSuccess ? "#4ECDC4" : "#FF6B6B") + ";"
        );
        statusLabel.setVisible(true);
        
        // Auto-hide after 3 seconds
        if (!isSuccess) {
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    javafx.application.Platform.runLater(() -> statusLabel.setVisible(false));
                } catch (InterruptedException e) {
                    // Ignore
                }
            }).start();
        }
    }
    
    private void animateEntrance() {
        // Fade in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        // Slide in animation for form
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(600), root.getCenter());
        slideIn.setFromY(50);
        slideIn.setToY(0);
        
        fadeIn.play();
        slideIn.play();
    }
    
    public BorderPane getRoot() {
        return root;
    }
}
