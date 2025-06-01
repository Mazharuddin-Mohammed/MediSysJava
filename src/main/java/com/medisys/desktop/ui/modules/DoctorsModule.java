package com.medisys.desktop.ui.modules;

import com.medisys.desktop.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Modern Doctors Module with CRUD operations and photo upload
 */
public class DoctorsModule {
    
    private final User currentUser;
    private final VBox root;
    
    public DoctorsModule(User currentUser) {
        this.currentUser = currentUser;
        this.root = new VBox(20);
        
        initializeUI();
    }
    
    private void initializeUI() {
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);
        
        Text title = new Text("👨‍⚕️ Doctors Management");
        title.getStyleClass().add("header-title");
        
        Label placeholder = new Label("Doctors module will be implemented here with:\n• Doctor profiles with photos\n• Specialization management\n• Schedule management\n• Performance tracking");
        placeholder.setStyle("""
            -fx-font-size: 16px;
            -fx-text-fill: #7F8C8D;
            -fx-padding: 40;
            """);
        
        root.getChildren().addAll(title, placeholder);
    }
    
    public VBox getRoot() {
        return root;
    }
}
