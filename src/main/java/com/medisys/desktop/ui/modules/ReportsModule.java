package com.medisys.desktop.ui.modules;

import com.medisys.desktop.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Modern Reports Module
 */
public class ReportsModule {
    
    private final User currentUser;
    private final VBox root;
    
    public ReportsModule(User currentUser) {
        this.currentUser = currentUser;
        this.root = new VBox(20);
        
        initializeUI();
    }
    
    private void initializeUI() {
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);
        
        Text title = new Text("📈 Reports & Analytics");
        title.getStyleClass().add("header-title");
        
        Label placeholder = new Label("Reports module will be implemented here with:\n• Patient reports\n• Financial analytics\n• Performance metrics\n• Custom report builder");
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
