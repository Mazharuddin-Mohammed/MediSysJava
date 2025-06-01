package com.medisys.desktop.ui.modules;

import com.medisys.desktop.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Modern Patients Module with CRUD operations and photo upload
 */
public class PatientsModule {
    
    private final User currentUser;
    private final VBox root;
    private TableView<Object> patientsTable;
    private TextField searchField;
    
    public PatientsModule(User currentUser) {
        this.currentUser = currentUser;
        this.root = new VBox(20);
        
        initializeUI();
    }
    
    private void initializeUI() {
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);
        
        // Header
        HBox header = createHeader();
        
        // Search and filters
        HBox searchBar = createSearchBar();
        
        // Patients table
        VBox tableContainer = createPatientsTable();
        
        root.getChildren().addAll(header, searchBar, tableContainer);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);
        
        Text title = new Text("👥 Patients Management");
        title.getStyleClass().add("header-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button addPatientBtn = new Button("+ Add New Patient");
        addPatientBtn.getStyleClass().add("modern-button");
        addPatientBtn.setOnAction(e -> showAddPatientDialog());
        
        header.getChildren().addAll(title, spacer, addPatientBtn);
        
        return header;
    }
    
    private HBox createSearchBar() {
        HBox searchBar = new HBox(15);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(10));
        searchBar.getStyleClass().add("modern-card");
        
        Text searchLabel = new Text("🔍");
        searchLabel.setStyle("-fx-font-size: 16px;");
        
        searchField = new TextField();
        searchField.getStyleClass().add("modern-text-field");
        searchField.setPromptText("Search patients by name, ID, or phone...");
        searchField.setPrefWidth(300);
        
        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getStyleClass().add("modern-combo-box");
        filterCombo.getItems().addAll("All Patients", "Active", "Inactive", "Recent");
        filterCombo.setValue("All Patients");
        
        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("secondary-button");
        
        Button clearBtn = new Button("Clear");
        clearBtn.getStyleClass().add("warning-button");
        
        searchBar.getChildren().addAll(searchLabel, searchField, filterCombo, searchBtn, clearBtn);
        
        return searchBar;
    }
    
    private VBox createPatientsTable() {
        VBox container = new VBox(10);
        container.getStyleClass().add("modern-card");
        
        Text tableTitle = new Text("Patient Records");
        tableTitle.getStyleClass().add("section-title");
        
        // Create table (simplified for now)
        patientsTable = new TableView<>();
        patientsTable.getStyleClass().add("modern-table-view");
        patientsTable.setPrefHeight(400);
        
        // Add placeholder
        Label placeholder = new Label("👥 No patients found\nClick 'Add New Patient' to get started");
        placeholder.setStyle("""
            -fx-font-size: 16px;
            -fx-text-fill: #7F8C8D;
            -fx-text-alignment: center;
            """);
        patientsTable.setPlaceholder(placeholder);
        
        container.getChildren().addAll(tableTitle, patientsTable);
        
        return container;
    }
    
    private void showAddPatientDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Add New Patient");
        dialog.setHeaderText("Patient Registration");
        dialog.setContentText("Patient registration form will be implemented here with photo upload capability.");
        dialog.showAndWait();
    }
    
    public VBox getRoot() {
        return root;
    }
}
