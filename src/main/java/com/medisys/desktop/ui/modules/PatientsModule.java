package com.medisys.desktop.ui.modules;

import com.medisys.desktop.model.Patient;
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

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modern Patients Module with CRUD operations and photo upload
 */
public class PatientsModule {

    private final User currentUser;
    private final VBox root;
    private TableView<Patient> patientsTable;
    private ObservableList<Patient> patientsList;
    private TextField searchField;
    
    public PatientsModule(User currentUser) {
        this.currentUser = currentUser;
        this.root = new VBox(20);
        this.patientsList = FXCollections.observableArrayList();

        initializeUI();
        loadSamplePatients();
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

        // Create table
        patientsTable = new TableView<>();
        patientsTable.getStyleClass().add("modern-table-view");
        patientsTable.setPrefHeight(400);
        patientsTable.setItems(patientsList);

        // Profile Photo Column
        TableColumn<Patient, String> photoCol = new TableColumn<>("Photo");
        photoCol.setPrefWidth(80);
        photoCol.setCellFactory(col -> new TableCell<Patient, String>() {
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
                        Image defaultImage = new Image(getClass().getResourceAsStream("/icons/default-profile.png"));
                        imageView.setImage(defaultImage);
                    } catch (Exception e) {
                        Text profileIcon = new Text("👤");
                        profileIcon.setStyle("-fx-font-size: 20px;");
                        setGraphic(profileIcon);
                        return;
                    }
                    setGraphic(imageView);
                }
            }
        });

        // Patient ID Column
        TableColumn<Patient, String> idCol = new TableColumn<>("Patient ID");
        idCol.setPrefWidth(100);
        idCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));

        // Full Name Column
        TableColumn<Patient, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setPrefWidth(150);
        nameCol.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(patient.getFullName());
        });

        // Age Column
        TableColumn<Patient, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setPrefWidth(60);
        ageCol.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            return new javafx.beans.property.SimpleIntegerProperty(patient.getAge()).asObject();
        });

        // Gender Column
        TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
        genderCol.setPrefWidth(80);
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Phone Column
        TableColumn<Patient, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setPrefWidth(120);
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Email Column
        TableColumn<Patient, String> emailCol = new TableColumn<>("Email");
        emailCol.setPrefWidth(180);
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Status Column
        TableColumn<Patient, Boolean> statusCol = new TableColumn<>("Status");
        statusCol.setPrefWidth(80);
        statusCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        statusCol.setCellFactory(col -> new TableCell<Patient, Boolean>() {
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
        TableColumn<Patient, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(col -> new TableCell<Patient, Void>() {
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
                    Patient patient = getTableView().getItems().get(getIndex());
                    editBtn.setOnAction(e -> showEditPatientDialog(patient));
                    deleteBtn.setOnAction(e -> showDeletePatientDialog(patient));
                    setGraphic(actionBox);
                }
            }
        });

        patientsTable.getColumns().addAll(photoCol, idCol, nameCol, ageCol, genderCol, phoneCol, emailCol, statusCol, actionsCol);

        container.getChildren().addAll(tableTitle, patientsTable);

        return container;
    }
    
    private void showAddPatientDialog() {
        showPatientDialog(null, "Add New Patient");
    }

    private void showEditPatientDialog(Patient patient) {
        showPatientDialog(patient, "Edit Patient");
    }

    private void showPatientDialog(Patient patient, String title) {
        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("Enter patient information");

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
            fileChooser.setTitle("Select Patient Photo");
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
        TextField firstNameField = new TextField();
        firstNameField.getStyleClass().add("modern-text-field");
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.getStyleClass().add("modern-text-field");
        lastNameField.setPromptText("Last Name");

        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("Date of Birth");

        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getStyleClass().add("modern-combo-box");
        genderCombo.getItems().addAll("Male", "Female", "Other");
        genderCombo.setPromptText("Gender");

        ComboBox<String> bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getStyleClass().add("modern-combo-box");
        bloodGroupCombo.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        bloodGroupCombo.setPromptText("Blood Group");

        TextField emailField = new TextField();
        emailField.getStyleClass().add("modern-text-field");
        emailField.setPromptText("Email");

        TextField phoneField = new TextField();
        phoneField.getStyleClass().add("modern-text-field");
        phoneField.setPromptText("Phone");

        TextArea addressArea = new TextArea();
        addressArea.getStyleClass().add("modern-text-field");
        addressArea.setPromptText("Address");
        addressArea.setPrefRowCount(2);

        TextField emergencyContactField = new TextField();
        emergencyContactField.getStyleClass().add("modern-text-field");
        emergencyContactField.setPromptText("Emergency Contact Name");

        TextField emergencyPhoneField = new TextField();
        emergencyPhoneField.getStyleClass().add("modern-text-field");
        emergencyPhoneField.setPromptText("Emergency Contact Phone");

        TextField insuranceField = new TextField();
        insuranceField.getStyleClass().add("modern-text-field");
        insuranceField.setPromptText("Insurance Provider");

        CheckBox activeCheckBox = new CheckBox("Active");
        activeCheckBox.setSelected(true);

        // Fill fields if editing
        if (patient != null) {
            firstNameField.setText(patient.getFirstName());
            lastNameField.setText(patient.getLastName());
            if (patient.getDateOfBirth() != null) {
                dobPicker.setValue(patient.getDateOfBirth());
            }
            genderCombo.setValue(patient.getGender());
            bloodGroupCombo.setValue(patient.getBloodGroup());
            emailField.setText(patient.getEmail());
            phoneField.setText(patient.getPhone());
            addressArea.setText(patient.getAddress());
            emergencyContactField.setText(patient.getEmergencyContactName());
            emergencyPhoneField.setText(patient.getEmergencyContactPhone());
            insuranceField.setText(patient.getInsuranceProvider());
            activeCheckBox.setSelected(patient.isActive());
        }

        // Add fields to grid
        grid.add(new Label("First Name:"), 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(new Label("Last Name:"), 0, 2);
        grid.add(lastNameField, 1, 2);
        grid.add(new Label("Date of Birth:"), 0, 3);
        grid.add(dobPicker, 1, 3);
        grid.add(new Label("Gender:"), 0, 4);
        grid.add(genderCombo, 1, 4);
        grid.add(new Label("Blood Group:"), 0, 5);
        grid.add(bloodGroupCombo, 1, 5);
        grid.add(new Label("Email:"), 0, 6);
        grid.add(emailField, 1, 6);
        grid.add(new Label("Phone:"), 0, 7);
        grid.add(phoneField, 1, 7);
        grid.add(new Label("Address:"), 0, 8);
        grid.add(addressArea, 1, 8);
        grid.add(new Label("Emergency Contact:"), 0, 9);
        grid.add(emergencyContactField, 1, 9);
        grid.add(new Label("Emergency Phone:"), 0, 10);
        grid.add(emergencyPhoneField, 1, 10);
        grid.add(new Label("Insurance Provider:"), 0, 11);
        grid.add(insuranceField, 1, 11);
        grid.add(activeCheckBox, 1, 12);

        dialog.getDialogPane().setContent(grid);

        // Add buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Convert result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Patient newPatient = patient != null ? patient : new Patient();
                newPatient.setFirstName(firstNameField.getText());
                newPatient.setLastName(lastNameField.getText());
                newPatient.setDateOfBirth(dobPicker.getValue());
                newPatient.setGender(genderCombo.getValue());
                newPatient.setBloodGroup(bloodGroupCombo.getValue());
                newPatient.setEmail(emailField.getText());
                newPatient.setPhone(phoneField.getText());
                newPatient.setAddress(addressArea.getText());
                newPatient.setEmergencyContactName(emergencyContactField.getText());
                newPatient.setEmergencyContactPhone(emergencyPhoneField.getText());
                newPatient.setInsuranceProvider(insuranceField.getText());
                newPatient.setActive(activeCheckBox.isSelected());

                if (patient == null) {
                    newPatient.setId((long) (patientsList.size() + 1));
                    newPatient.setPatientId("PAT" + String.format("%03d", patientsList.size() + 1));
                    newPatient.setCreatedDate(LocalDateTime.now());
                }
                newPatient.setUpdatedDate(LocalDateTime.now());

                return newPatient;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (patient == null) {
                patientsList.add(result);
                showAlert("Success", "Patient created successfully!");
            } else {
                patientsTable.refresh();
                showAlert("Success", "Patient updated successfully!");
            }
        });
    }

    private void showDeletePatientDialog(Patient patient) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Patient");
        alert.setHeaderText("Are you sure you want to delete this patient?");
        alert.setContentText("Patient: " + patient.getFullName() + " (" + patient.getPatientId() + ")");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                patientsList.remove(patient);
                showAlert("Success", "Patient deleted successfully!");
            }
        });
    }

    private void loadSamplePatients() {
        try {
            // Prevent multiple loading
            if (!patientsList.isEmpty()) {
                return;
            }

            // Add sample patients with safe initialization
            Patient patient1 = new Patient("Alice", "Johnson", LocalDate.of(1985, 3, 15), "alice.johnson@email.com", "+91-9876543301");
            patient1.setId(1L);
            patient1.setPatientId("PAT001");
            patient1.setGender("Female");
            patient1.setBloodGroup("A+");
            patient1.setAddress("123 Main St, Sector 15, New Delhi");
            patient1.setEmergencyContactName("Robert Johnson");
            patient1.setEmergencyContactPhone("+91-9876543401");
            patient1.setInsuranceProvider("Star Health Insurance");

            Patient patient2 = new Patient("Bob", "Wilson", LocalDate.of(1978, 7, 22), "bob.wilson@email.com", "+91-9876543302");
            patient2.setId(2L);
            patient2.setPatientId("PAT002");
            patient2.setGender("Male");
            patient2.setBloodGroup("B+");
            patient2.setAddress("456 Oak Ave, Block A, Mumbai");
            patient2.setEmergencyContactName("Mary Wilson");
            patient2.setEmergencyContactPhone("+91-9876543402");
            patient2.setInsuranceProvider("HDFC ERGO");

            Patient patient3 = new Patient("Carol", "Davis", LocalDate.of(1992, 11, 8), "carol.davis@email.com", "+91-9876543303");
            patient3.setId(3L);
            patient3.setPatientId("PAT003");
            patient3.setGender("Female");
            patient3.setBloodGroup("O-");
            patient3.setAddress("789 Pine Rd, Phase 2, Bangalore");
            patient3.setEmergencyContactName("James Davis");
            patient3.setEmergencyContactPhone("+91-9876543403");
            patient3.setInsuranceProvider("ICICI Lombard");

            patientsList.addAll(patient1, patient2, patient3);
            System.out.println("✅ Sample patients loaded successfully");
        } catch (Exception e) {
            System.err.println("⚠️ Error loading sample patients: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getRoot() {
        return root;
    }
}
