package com.medisys.desktop.ui;

import com.medisys.desktop.MediSysApp;
import com.medisys.desktop.model.User;
import com.medisys.desktop.ui.components.QuickActionsPanel;
import com.medisys.desktop.ui.modules.*;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Modern Main Dashboard with vibrant design and module navigation
 */
public class MainDashboard {
    
    private final MediSysApp app;
    private final User currentUser;
    private final BorderPane root;
    private VBox sidebar;
    private StackPane contentArea;
    private Label userNameLabel;
    private Label userRoleLabel;
    private ImageView profileImageView;
    
    // Module instances
    private DashboardModule dashboardModule;
    private PatientsModule patientsModule;
    private DoctorsModule doctorsModule;
    private AppointmentsModule appointmentsModule;
    private FinanceModule financeModule;
    private ReportsModule reportsModule;
    private HelpModule helpModule;
    private UserManagementModule userManagementModule;
    
    public MainDashboard(MediSysApp app, User currentUser) {
        this.app = app;
        this.currentUser = currentUser;
        this.root = new BorderPane();
        
        initializeModules();
        initializeUI();
        setupEventHandlers();
        showDashboard(); // Show dashboard by default
        animateEntrance();
    }
    
    private void initializeModules() {
        dashboardModule = new DashboardModule(currentUser);
        patientsModule = new PatientsModule(currentUser);
        doctorsModule = new DoctorsModule(currentUser);
        appointmentsModule = new AppointmentsModule(currentUser);
        financeModule = new FinanceModule(currentUser);
        reportsModule = new ReportsModule(currentUser);
        helpModule = new HelpModule(currentUser);

        // User Management only for admin
        if ("admin".equals(currentUser.getRole())) {
            userManagementModule = new UserManagementModule(currentUser);
        }
    }
    
    private void initializeUI() {
        root.getStyleClass().add("root");
        
        // Create sidebar
        sidebar = createSidebar();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(280);
        
        // Create content area
        contentArea = new StackPane();
        contentArea.getStyleClass().add("content-area");
        
        root.setLeft(sidebar);
        root.setCenter(contentArea);
    }
    
    private VBox createSidebar() {
        VBox sidebarContainer = new VBox(0);
        sidebarContainer.setAlignment(Pos.TOP_CENTER);
        sidebarContainer.setPadding(new Insets(20));
        
        // Header with logo and title
        VBox header = createSidebarHeader();
        
        // User profile section
        VBox userProfile = createUserProfile();
        
        // Navigation menu
        VBox navigation = createNavigationMenu();
        
        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        // Footer with logout
        VBox footer = createSidebarFooter();
        
        sidebarContainer.getChildren().addAll(header, userProfile, navigation, spacer, footer);
        
        return sidebarContainer;
    }
    
    private VBox createSidebarHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        // Logo
        Text logo = new Text("🏥");
        logo.setStyle("""
            -fx-font-size: 32px;
            """);
        
        // Title
        Text title = new Text("MediSys");
        title.setStyle("""
            -fx-font-size: 24px;
            -fx-font-weight: bold;
            -fx-fill: #2E86AB;
            """);
        
        header.getChildren().addAll(logo, title);
        
        return header;
    }
    
    private VBox createUserProfile() {
        VBox profile = new VBox(10);
        profile.setAlignment(Pos.CENTER);
        profile.setPadding(new Insets(20));
        profile.setStyle("""
            -fx-background-color: #F8F9FA;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            """);
        
        // Profile image
        profileImageView = new ImageView();
        profileImageView.setFitWidth(60);
        profileImageView.setFitHeight(60);
        profileImageView.getStyleClass().add("profile-photo");
        
        // Load default profile image or user's photo
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/icons/default-profile.png"));
            profileImageView.setImage(defaultImage);
        } catch (Exception e) {
            // Fallback to text
            Text profileIcon = new Text("👤");
            profileIcon.setStyle("-fx-font-size: 40px;");
            profile.getChildren().add(profileIcon);
        }
        
        // User name
        userNameLabel = new Label(currentUser.getFullName());
        userNameLabel.setStyle("""
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-text-fill: #2C3E50;
            """);
        
        // User role
        userRoleLabel = new Label(formatRole(currentUser.getRole()));
        userRoleLabel.setStyle("""
            -fx-font-size: 12px;
            -fx-text-fill: #7F8C8D;
            """);
        
        if (profileImageView.getImage() != null) {
            profile.getChildren().addAll(profileImageView, userNameLabel, userRoleLabel);
        } else {
            profile.getChildren().addAll(userNameLabel, userRoleLabel);
        }
        
        return profile;
    }
    
    private VBox createNavigationMenu() {
        VBox navigation = new VBox(5);
        navigation.setPadding(new Insets(20, 0, 0, 0));
        
        // Navigation buttons based on user role
        Button dashboardBtn = createNavButton("📊", "Dashboard");
        Button patientsBtn = createNavButton("👥", "Patients");
        Button doctorsBtn = createNavButton("👨‍⚕️", "Doctors");
        Button appointmentsBtn = createNavButton("📅", "Appointments");
        
        navigation.getChildren().addAll(dashboardBtn, patientsBtn, doctorsBtn, appointmentsBtn);
        
        // Role-specific buttons
        if ("admin".equals(currentUser.getRole()) || "finance".equals(currentUser.getRole())) {
            Button financeBtn = createNavButton("💰", "Finance");
            navigation.getChildren().add(financeBtn);
            financeBtn.setOnAction(e -> showFinance());
        }
        
        Button reportsBtn = createNavButton("📈", "Reports");
        navigation.getChildren().add(reportsBtn);

        Button helpBtn = createNavButton("❓", "Help");
        navigation.getChildren().add(helpBtn);

        // Admin-only features
        if ("admin".equals(currentUser.getRole())) {
            Separator separator = new Separator();
            separator.setPadding(new Insets(10, 0, 10, 0));
            
            Button userMgmtBtn = createNavButton("👤", "User Management");
            navigation.getChildren().addAll(separator, userMgmtBtn);
            userMgmtBtn.setOnAction(e -> showUserManagement());
        }
        
        // Set up event handlers
        dashboardBtn.setOnAction(e -> showDashboard());
        patientsBtn.setOnAction(e -> showPatients());
        doctorsBtn.setOnAction(e -> showDoctors());
        appointmentsBtn.setOnAction(e -> showAppointments());
        reportsBtn.setOnAction(e -> showReports());
        helpBtn.setOnAction(e -> showHelp());

        return navigation;
    }
    
    private Button createNavButton(String icon, String text) {
        Button button = new Button(icon + "  " + text);
        button.getStyleClass().add("nav-button");
        button.setPrefWidth(240);
        button.setAlignment(Pos.CENTER_LEFT);
        return button;
    }
    
    private VBox createSidebarFooter() {
        VBox footer = new VBox(10);
        footer.setAlignment(Pos.CENTER);
        
        Button logoutBtn = new Button("🚪  Logout");
        logoutBtn.getStyleClass().add("error-button");
        logoutBtn.setPrefWidth(240);
        logoutBtn.setOnAction(e -> handleLogout());
        
        Text version = new Text("v2.0.0");
        version.setStyle("""
            -fx-font-size: 10px;
            -fx-fill: #7F8C8D;
            """);
        
        footer.getChildren().addAll(logoutBtn, version);
        
        return footer;
    }
    
    private void setupEventHandlers() {
        // Add any global event handlers here
    }
    
    // Navigation methods
    private void showDashboard() {
        switchContent(dashboardModule.getRoot(), "Dashboard");
    }
    
    private void showPatients() {
        switchContent(patientsModule.getRoot(), "Patients");
    }
    
    private void showDoctors() {
        switchContent(doctorsModule.getRoot(), "Doctors");
    }
    
    private void showAppointments() {
        switchContent(appointmentsModule.getRoot(), "Appointments");
    }
    
    private void showFinance() {
        switchContent(financeModule.getRoot(), "Finance");
    }
    
    private void showReports() {
        switchContent(reportsModule.getRoot(), "Reports");
    }

    private void showHelp() {
        switchContent(helpModule.getRoot(), "Help & Documentation");
    }

    private void showUserManagement() {
        if (userManagementModule != null) {
            switchContent(userManagementModule.getRoot(), "User Management");
        }
    }
    
    private void switchContent(Region newContent, String title) {
        // Fade out current content
        if (!contentArea.getChildren().isEmpty()) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), contentArea);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                contentArea.getChildren().clear();
                contentArea.getChildren().add(newContent);
                
                // Fade in new content
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), contentArea);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        } else {
            contentArea.getChildren().add(newContent);
        }
        
        // Update window title
        app.getPrimaryStage().setTitle("MediSys - " + title);
        
        // Update navigation button states
        updateNavigationStates(title);
    }
    
    private void updateNavigationStates(String activeModule) {
        // Remove active class from all nav buttons and add to current
        sidebar.getChildren().forEach(node -> {
            if (node instanceof VBox) {
                ((VBox) node).getChildren().forEach(child -> {
                    if (child instanceof Button) {
                        Button btn = (Button) child;
                        btn.getStyleClass().remove("active");
                        if (btn.getText().contains(activeModule)) {
                            btn.getStyleClass().add("active");
                        }
                    }
                });
            }
        });
    }
    
    private void handleLogout() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Logout Confirmation");
        confirmDialog.setHeaderText("Are you sure you want to logout?");
        confirmDialog.setContentText("You will be redirected to the login screen.");
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Animate logout
                FadeTransition fadeOut = new FadeTransition(Duration.millis(500), root);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(e -> app.logout());
                fadeOut.play();
            }
        });
    }
    
    private String formatRole(String role) {
        return switch (role.toLowerCase()) {
            case "admin" -> "Administrator";
            case "doctor" -> "Doctor";
            case "finance" -> "Finance Manager";
            case "department_head" -> "Department Head";
            case "nurse" -> "Nurse";
            case "receptionist" -> "Receptionist";
            default -> role;
        };
    }
    
    private void animateEntrance() {
        // Fade in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    
    public BorderPane getRoot() {
        return root;
    }
}
