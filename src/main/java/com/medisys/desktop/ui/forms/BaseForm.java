package com.medisys.desktop.ui.forms;

import com.medisys.desktop.utils.IconLibrary;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Base Form Class for Consistent Styling
 * Provides common form elements and styling across all forms
 */
public abstract class BaseForm {
    
    protected Stage stage;
    
    /**
     * Create a consistent form section with title
     */
    protected VBox createSection(String title) {
        VBox section = new VBox(15);
        section.getStyleClass().add("form-section");
        section.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-background-radius: 8; -fx-border-color: #e9ecef; -fx-border-width: 1; -fx-border-radius: 8;");
        
        Text sectionTitle = new Text(title);
        sectionTitle.getStyleClass().add("section-title");
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #2E86AB;");
        
        section.getChildren().add(sectionTitle);
        return section;
    }
    
    /**
     * Create a consistent text field with modern styling
     */
    protected TextField createTextField(String promptText) {
        TextField field = new TextField();
        field.getStyleClass().add("modern-text-field");
        field.setPromptText(promptText);
        field.setStyle("-fx-padding: 10; -fx-font-size: 14px; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        return field;
    }
    
    /**
     * Create a consistent text area with modern styling
     */
    protected TextArea createTextArea(String promptText, int rows) {
        TextArea area = new TextArea();
        area.getStyleClass().add("modern-text-area");
        area.setPromptText(promptText);
        area.setPrefRowCount(rows);
        area.setWrapText(true);
        area.setStyle("-fx-padding: 10; -fx-font-size: 14px; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        return area;
    }
    
    /**
     * Create a consistent combo box with modern styling
     */
    protected ComboBox<String> createComboBox(String promptText) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getStyleClass().add("modern-combo-box");
        combo.setPromptText(promptText);
        combo.setStyle("-fx-padding: 10; -fx-font-size: 14px; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        return combo;
    }
    
    /**
     * Create a consistent date picker with modern styling
     */
    protected DatePicker createDatePicker() {
        DatePicker picker = new DatePicker();
        picker.getStyleClass().add("modern-date-picker");
        picker.setStyle("-fx-padding: 10; -fx-font-size: 14px; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        return picker;
    }
    
    /**
     * Create a consistent form header
     */
    protected Text createFormHeader(String title, String color) {
        Text header = new Text(title);
        header.getStyleClass().add("form-header");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: " + color + ";");
        return header;
    }
    
    /**
     * Create a consistent primary button
     */
    protected Button createPrimaryButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("primary-button");
        button.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-font-size: 14px; -fx-background-radius: 6; -fx-cursor: hand;");
        button.setOnAction(e -> action.run());
        
        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-background-color: #229954;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-background-color: #229954;", "")));
        
        return button;
    }
    
    /**
     * Create a consistent secondary button
     */
    protected Button createSecondaryButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("secondary-button");
        button.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-font-size: 14px; -fx-background-radius: 6; -fx-cursor: hand;");
        button.setOnAction(e -> action.run());
        
        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-background-color: #7f8c8d;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-background-color: #7f8c8d;", "")));
        
        return button;
    }
    
    /**
     * Create a consistent danger button
     */
    protected Button createDangerButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("danger-button");
        button.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-font-size: 14px; -fx-background-radius: 6; -fx-cursor: hand;");
        button.setOnAction(e -> action.run());
        
        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-background-color: #c0392b;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-background-color: #c0392b;", "")));
        
        return button;
    }
    
    /**
     * Create a consistent info button
     */
    protected Button createInfoButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("info-button");
        button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 24; -fx-font-size: 14px; -fx-background-radius: 6; -fx-cursor: hand;");
        button.setOnAction(e -> action.run());
        
        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-background-color: #2980b9;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-background-color: #2980b9;", "")));
        
        return button;
    }
    
    /**
     * Create a consistent form row with two fields
     */
    protected HBox createFormRow(VBox... fields) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        
        for (VBox field : fields) {
            row.getChildren().add(field);
            HBox.setHgrow(field, Priority.ALWAYS);
        }
        
        return row;
    }
    
    /**
     * Create a consistent field container with label
     */
    protected VBox createFieldContainer(String labelText, Control field) {
        VBox container = new VBox(5);
        
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2c3e50;");
        
        container.getChildren().addAll(label, field);
        return container;
    }
    
    /**
     * Create a consistent photo upload section
     */
    protected VBox createPhotoUploadSection(String title, Label photoLabel, Runnable selectAction) {
        VBox photoSection = createSection(title);
        
        HBox photoRow = new HBox(15);
        photoRow.setAlignment(Pos.CENTER_LEFT);
        
        photoLabel.setText("No photo selected");
        photoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d; -fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: #f8f9fa; -fx-background-radius: 5;");
        
        Button selectPhotoBtn = createInfoButton(IconLibrary.UPLOAD + " Select Photo", selectAction);
        
        photoRow.getChildren().addAll(photoLabel, selectPhotoBtn);
        HBox.setHgrow(photoLabel, Priority.ALWAYS);
        
        photoSection.getChildren().add(photoRow);
        return photoSection;
    }
    
    /**
     * Standard photo selection dialog
     */
    protected String selectPhotoFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
            new FileChooser.ExtensionFilter("PNG Files", "*.png"),
            new FileChooser.ExtensionFilter("JPEG Files", "*.jpg", "*.jpeg"),
            new FileChooser.ExtensionFilter("GIF Files", "*.gif"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        File selectedFile = fileChooser.showOpenDialog(stage);
        return selectedFile != null ? selectedFile.getAbsolutePath() : null;
    }
    
    /**
     * Create a consistent button box for form actions
     */
    protected HBox createButtonBox(Button... buttons) {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        buttonBox.getChildren().addAll(buttons);
        
        return buttonBox;
    }
    
    /**
     * Show a consistent validation error dialog
     */
    protected void showValidationError(String errors) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Please fix the following errors:");
        alert.setContentText(errors);
        
        // Style the alert
        alert.getDialogPane().setStyle("-fx-font-size: 14px;");
        
        alert.showAndWait();
    }
    
    /**
     * Show a consistent success message
     */
    protected void showSuccessMessage(String title, String header, String content) {
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle(title);
        success.setHeaderText(header);
        success.setContentText(content);
        
        // Style the alert
        success.getDialogPane().setStyle("-fx-font-size: 14px;");
        
        success.showAndWait();
    }
    
    /**
     * Show a consistent error message
     */
    protected void showErrorMessage(String title, String header, String content) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(title);
        error.setHeaderText(header);
        error.setContentText(content);
        
        // Style the alert
        error.getDialogPane().setStyle("-fx-font-size: 14px;");
        
        error.showAndWait();
    }
    
    /**
     * Create a consistent form container
     */
    protected VBox createFormContainer() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.getStyleClass().add("form-container");
        container.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        return container;
    }
    
    /**
     * Create a consistent scroll pane for form content
     */
    protected ScrollPane createFormScrollPane(VBox content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.getStyleClass().add("form-scroll");
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scrollPane;
    }
}
