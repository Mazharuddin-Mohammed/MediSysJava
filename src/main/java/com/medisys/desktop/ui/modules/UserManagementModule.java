package com.medisys.desktop.ui.modules;

import com.medisys.desktop.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modern User Management Module with photo upload and role-based access
 */
public class UserManagementModule {
    
    private final User currentUser;
    private final VBox root;
    private TableView<User> usersTable;
    private ObservableList<User> usersList;
    private TextField searchField;
    
    public UserManagementModule(User currentUser) {
        this.currentUser = currentUser;
        this.root = new VBox(20);
        this.usersList = FXCollections.observableArrayList();
        
        initializeUI();
        loadSampleUsers();
    }
    
    private void initializeUI() {
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);
        
        // Header
        HBox header = createHeader();
        
        // Search and filters
        HBox searchBar = createSearchBar();
        
        // Users table
        VBox tableContainer = createUsersTable();
        
        root.getChildren().addAll(header, searchBar, tableContainer);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);
        
        Text title = new Text("👤 User Management");
        title.getStyleClass().add("header-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button addUserBtn = new Button("+ Add New User");
        addUserBtn.getStyleClass().add("modern-button");
        addUserBtn.setOnAction(e -> showAddUserDialog());
        
        header.getChildren().addAll(title, spacer, addUserBtn);
        
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
        searchField.setPromptText("Search users by name, username, or email...");
        searchField.setPrefWidth(300);
        
        ComboBox<String> roleFilter = new ComboBox<>();
        roleFilter.getStyleClass().add("modern-combo-box");
        roleFilter.getItems().addAll("All Roles", "Admin", "Doctor", "Finance", "Department Head", "Nurse", "Receptionist");
        roleFilter.setValue("All Roles");
        
        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getStyleClass().add("modern-combo-box");
        statusFilter.getItems().addAll("All Status", "Active", "Inactive");
        statusFilter.setValue("All Status");
        
        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("secondary-button");
        
        Button clearBtn = new Button("Clear");
        clearBtn.getStyleClass().add("warning-button");
        
        searchBar.getChildren().addAll(searchLabel, searchField, roleFilter, statusFilter, searchBtn, clearBtn);
        
        return searchBar;
    }
    
    private VBox createUsersTable() {
        VBox container = new VBox(10);
        container.getStyleClass().add("modern-card");
        
        Text tableTitle = new Text("System Users");
        tableTitle.getStyleClass().add("section-title");
        
        // Create table
        usersTable = new TableView<>();
        usersTable.getStyleClass().add("modern-table-view");
        usersTable.setPrefHeight(400);
        usersTable.setItems(usersList);
        
        // Profile Photo Column
        TableColumn<User, String> photoCol = new TableColumn<>("Photo");
        photoCol.setPrefWidth(80);
        photoCol.setCellFactory(col -> new TableCell<User, String>() {
            private final ImageView imageView = new ImageView();
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    imageView.setFitWidth(40);
                    imageView.setFitHeight(40);
                    imageView.getStyleClass().add("profile-photo");
                    
                    try {
                        // Try to load user's photo or default
                        Image defaultImage = new Image(getClass().getResourceAsStream("/icons/default-profile.png"));
                        imageView.setImage(defaultImage);
                    } catch (Exception e) {
                        // Fallback to text
                        Text profileIcon = new Text("👤");
                        profileIcon.setStyle("-fx-font-size: 20px;");
                        setGraphic(profileIcon);
                        return;
                    }
                    setGraphic(imageView);
                }
            }
        });
        
        // Username Column
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setPrefWidth(120);
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        
        // Full Name Column
        TableColumn<User, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setPrefWidth(150);
        nameCol.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(user.getFullName());
        });
        
        // Email Column
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setPrefWidth(180);
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        // Role Column
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setPrefWidth(120);
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setCellFactory(col -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(formatRole(item));
                    setStyle("-fx-text-fill: " + getRoleColor(item) + "; -fx-font-weight: bold;");
                }
            }
        });
        
        // Status Column
        TableColumn<User, Boolean> statusCol = new TableColumn<>("Status");
        statusCol.setPrefWidth(100);
        statusCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        statusCol.setCellFactory(col -> new TableCell<User, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Label statusLabel = new Label(item ? "Active" : "Inactive");
                    statusLabel.getStyleClass().add(item ? "status-active" : "status-inactive");
                    setGraphic(statusLabel);
                }
            }
        });
        
        // Actions Column
        TableColumn<User, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(col -> new TableCell<User, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox actionBox = new HBox(5);
            
            {
                editBtn.getStyleClass().addAll("action-button", "edit");
                deleteBtn.getStyleClass().addAll("action-button", "delete");
                actionBox.getChildren().addAll(editBtn, deleteBtn);
                actionBox.setAlignment(Pos.CENTER);
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());
                    editBtn.setOnAction(e -> showEditUserDialog(user));
                    deleteBtn.setOnAction(e -> showDeleteUserDialog(user));
                    setGraphic(actionBox);
                }
            }
        });
        
        usersTable.getColumns().addAll(photoCol, usernameCol, nameCol, emailCol, roleCol, statusCol, actionsCol);
        
        container.getChildren().addAll(tableTitle, usersTable);
        
        return container;
    }
    
    private void loadSampleUsers() {
        // Add sample users
        User admin = new User("admin", "System", "Administrator", "admin@medisys.com", "admin");
        admin.setId(1L);
        admin.setPhone("+91-9876543210");
        admin.setActive(true);
        admin.setCreatedDate(LocalDateTime.now().minusDays(30));
        
        User doctor = new User("doctor", "Dr. John", "Smith", "john.smith@medisys.com", "doctor");
        doctor.setId(2L);
        doctor.setPhone("+91-9876543211");
        doctor.setActive(true);
        doctor.setCreatedDate(LocalDateTime.now().minusDays(15));
        
        User finance = new User("finance", "Sarah", "Johnson", "sarah.johnson@medisys.com", "finance");
        finance.setId(3L);
        finance.setPhone("+91-9876543212");
        finance.setActive(true);
        finance.setCreatedDate(LocalDateTime.now().minusDays(10));
        
        User nurse = new User("nurse1", "Emily", "Davis", "emily.davis@medisys.com", "nurse");
        nurse.setId(4L);
        nurse.setPhone("+91-9876543213");
        nurse.setActive(true);
        nurse.setCreatedDate(LocalDateTime.now().minusDays(5));
        
        usersList.addAll(admin, doctor, finance, nurse);
    }
    
    private void showAddUserDialog() {
        showUserDialog(null, "Add New User");
    }
    
    private void showEditUserDialog(User user) {
        showUserDialog(user, "Edit User");
    }
    
    private void showUserDialog(User user, String title) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("Enter user information");
        
        // Create form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Photo upload section
        VBox photoSection = new VBox(10);
        photoSection.setAlignment(Pos.CENTER);
        
        ImageView photoPreview = new ImageView();
        photoPreview.setFitWidth(80);
        photoPreview.setFitHeight(80);
        photoPreview.getStyleClass().add("profile-photo");
        
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/icons/default-profile.png"));
            photoPreview.setImage(defaultImage);
        } catch (Exception e) {
            // Fallback
        }
        
        Button uploadPhotoBtn = new Button("Upload Photo");
        uploadPhotoBtn.getStyleClass().add("secondary-button");
        uploadPhotoBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Profile Photo");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
            if (selectedFile != null) {
                try {
                    Image image = new Image(selectedFile.toURI().toString());
                    photoPreview.setImage(image);
                } catch (Exception ex) {
                    showAlert("Error", "Could not load the selected image.");
                }
            }
        });
        
        photoSection.getChildren().addAll(photoPreview, uploadPhotoBtn);
        grid.add(photoSection, 0, 0, 2, 1);
        
        // Form fields
        TextField usernameField = new TextField();
        usernameField.getStyleClass().add("modern-text-field");
        usernameField.setPromptText("Username");
        
        TextField firstNameField = new TextField();
        firstNameField.getStyleClass().add("modern-text-field");
        firstNameField.setPromptText("First Name");
        
        TextField lastNameField = new TextField();
        lastNameField.getStyleClass().add("modern-text-field");
        lastNameField.setPromptText("Last Name");
        
        TextField emailField = new TextField();
        emailField.getStyleClass().add("modern-text-field");
        emailField.setPromptText("Email");
        
        TextField phoneField = new TextField();
        phoneField.getStyleClass().add("modern-text-field");
        phoneField.setPromptText("Phone");
        
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getStyleClass().add("modern-combo-box");
        roleCombo.getItems().addAll("admin", "doctor", "finance", "department_head", "nurse", "receptionist");
        roleCombo.setPromptText("Select Role");
        
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getStyleClass().add("modern-combo-box");
        genderCombo.getItems().addAll("Male", "Female", "Other");
        genderCombo.setPromptText("Gender");
        
        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("Date of Birth");
        
        TextArea addressArea = new TextArea();
        addressArea.getStyleClass().add("modern-text-field");
        addressArea.setPromptText("Address");
        addressArea.setPrefRowCount(2);
        
        CheckBox activeCheckBox = new CheckBox("Active");
        activeCheckBox.setSelected(true);
        
        // Fill fields if editing
        if (user != null) {
            usernameField.setText(user.getUsername());
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhone());
            roleCombo.setValue(user.getRole());
            genderCombo.setValue(user.getGender());
            if (user.getDateOfBirth() != null) {
                dobPicker.setValue(user.getDateOfBirth());
            }
            addressArea.setText(user.getAddress());
            activeCheckBox.setSelected(user.isActive());
        }
        
        // Add fields to grid
        grid.add(new Label("Username:"), 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(new Label("First Name:"), 0, 2);
        grid.add(firstNameField, 1, 2);
        grid.add(new Label("Last Name:"), 0, 3);
        grid.add(lastNameField, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(new Label("Phone:"), 0, 5);
        grid.add(phoneField, 1, 5);
        grid.add(new Label("Role:"), 0, 6);
        grid.add(roleCombo, 1, 6);
        grid.add(new Label("Gender:"), 0, 7);
        grid.add(genderCombo, 1, 7);
        grid.add(new Label("Date of Birth:"), 0, 8);
        grid.add(dobPicker, 1, 8);
        grid.add(new Label("Address:"), 0, 9);
        grid.add(addressArea, 1, 9);
        grid.add(activeCheckBox, 1, 10);
        
        dialog.getDialogPane().setContent(grid);
        
        // Add buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Convert result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                User newUser = user != null ? user : new User();
                newUser.setUsername(usernameField.getText());
                newUser.setFirstName(firstNameField.getText());
                newUser.setLastName(lastNameField.getText());
                newUser.setEmail(emailField.getText());
                newUser.setPhone(phoneField.getText());
                newUser.setRole(roleCombo.getValue());
                newUser.setGender(genderCombo.getValue());
                newUser.setDateOfBirth(dobPicker.getValue());
                newUser.setAddress(addressArea.getText());
                newUser.setActive(activeCheckBox.isSelected());
                
                if (user == null) {
                    newUser.setId((long) (usersList.size() + 1));
                    newUser.setCreatedDate(LocalDateTime.now());
                }
                newUser.setUpdatedDate(LocalDateTime.now());
                
                return newUser;
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(result -> {
            if (user == null) {
                usersList.add(result);
                showAlert("Success", "User created successfully!");
            } else {
                usersTable.refresh();
                showAlert("Success", "User updated successfully!");
            }
        });
    }
    
    private void showDeleteUserDialog(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Are you sure you want to delete this user?");
        alert.setContentText("User: " + user.getFullName() + " (" + user.getUsername() + ")");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                usersList.remove(user);
                showAlert("Success", "User deleted successfully!");
            }
        });
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    
    private String getRoleColor(String role) {
        return switch (role.toLowerCase()) {
            case "admin" -> "#2E86AB";
            case "doctor" -> "#A23B72";
            case "finance" -> "#4ECDC4";
            case "department_head" -> "#F18F01";
            case "nurse" -> "#FF6B6B";
            case "receptionist" -> "#7F8C8D";
            default -> "#2C3E50";
        };
    }
    
    public VBox getRoot() {
        return root;
    }
}
