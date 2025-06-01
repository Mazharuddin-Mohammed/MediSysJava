package com.medisys.desktop;

import com.medisys.desktop.model.User;
import com.medisys.desktop.ui.LoginWindow;
import com.medisys.desktop.ui.MainDashboard;
import com.medisys.desktop.util.StyleManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main MediSys Application with Modern UI
 */
public class MediSysApp extends Application {
    
    private static MediSysApp instance;
    private Stage primaryStage;
    private User currentUser;
    
    public static MediSysApp getInstance() {
        return instance;
    }
    
    @Override
    public void start(Stage primaryStage) {
        instance = this;
        this.primaryStage = primaryStage;
        
        // Set application icon
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/medisys-icon.png")));
        } catch (Exception e) {
            System.out.println("Could not load application icon");
        }
        
        // Configure primary stage
        primaryStage.setTitle("MediSys - Healthcare Management System");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);
        primaryStage.setMaximized(true);
        
        // Show login window
        showLoginWindow();
        
        // Handle application close
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        
        System.out.println("🚀 MediSys Application started successfully!");
    }
    
    public void showLoginWindow() {
        LoginWindow loginWindow = new LoginWindow(this);
        Scene loginScene = new Scene(loginWindow.getRoot(), 1000, 700);
        
        // Apply modern styling
        StyleManager.applyGlobalStyles(loginScene);
        
        primaryStage.setScene(loginScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    public void showMainDashboard(User user) {
        this.currentUser = user;
        
        MainDashboard dashboard = new MainDashboard(this, user);
        Scene dashboardScene = new Scene(dashboard.getRoot(), 1400, 900);
        
        // Apply modern styling
        StyleManager.applyGlobalStyles(dashboardScene);
        
        primaryStage.setScene(dashboardScene);
        primaryStage.setMaximized(true);
        primaryStage.centerOnScreen();
        
        System.out.println("✅ Welcome " + user.getFullName() + " (" + user.getRole() + ")");
    }
    
    public void logout() {
        this.currentUser = null;
        showLoginWindow();
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public static void main(String[] args) {
        // Set system properties for better rendering
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        
        launch(args);
    }
}
