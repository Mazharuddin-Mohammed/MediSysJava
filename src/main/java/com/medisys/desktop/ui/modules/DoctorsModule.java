package com.medisys.desktop.ui.modules;

import com.medisys.desktop.model.Doctor;
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
import java.time.LocalTime;

/**
 * Modern Doctors Module with CRUD operations and photo upload
 */
public class DoctorsModule {

    private final User currentUser;
    private final VBox root;
    private TableView<Doctor> doctorsTable;
    private ObservableList<Doctor> doctorsList;
    private TextField searchField;

    public DoctorsModule(User currentUser) {
        this.currentUser = currentUser;
        this.root = new VBox(20);
        this.doctorsList = FXCollections.observableArrayList();

        initializeUI();
        loadSampleDoctors();
    }

    private void initializeUI() {
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        // Header
        HBox header = createHeader();

        // Search and filters
        HBox searchBar = createSearchBar();

        // Doctors table
        VBox tableContainer = createDoctorsTable();

        root.getChildren().addAll(header, searchBar, tableContainer);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);

        Text title = new Text("👨‍⚕️ Doctors Management");
        title.getStyleClass().add("header-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addDoctorBtn = new Button("+ Add New Doctor");
        addDoctorBtn.getStyleClass().add("modern-button");
        addDoctorBtn.setOnAction(e -> showAddDoctorDialog());

        header.getChildren().addAll(title, spacer, addDoctorBtn);

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
        searchField.setPromptText("Search doctors by name, specialization, or department...");
        searchField.setPrefWidth(300);

        ComboBox<String> specializationFilter = new ComboBox<>();
        specializationFilter.getStyleClass().add("modern-combo-box");
        specializationFilter.getItems().addAll("All Specializations", "Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Dermatology", "General Medicine");
        specializationFilter.setValue("All Specializations");

        ComboBox<String> departmentFilter = new ComboBox<>();
        departmentFilter.getStyleClass().add("modern-combo-box");
        departmentFilter.getItems().addAll("All Departments", "Emergency", "ICU", "Surgery", "Outpatient", "Radiology");
        departmentFilter.setValue("All Departments");

        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("secondary-button");

        Button clearBtn = new Button("Clear");
        clearBtn.getStyleClass().add("warning-button");

        searchBar.getChildren().addAll(searchLabel, searchField, specializationFilter, departmentFilter, searchBtn, clearBtn);

        return searchBar;
    }

    private VBox createDoctorsTable() {
        VBox container = new VBox(10);
        container.getStyleClass().add("modern-card");

        Text tableTitle = new Text("Medical Staff Directory");
        tableTitle.getStyleClass().add("section-title");

        // Create table
        doctorsTable = new TableView<>();
        doctorsTable.getStyleClass().add("modern-table-view");
        doctorsTable.setPrefHeight(400);
        doctorsTable.setItems(doctorsList);

        // Profile Photo Column
        TableColumn<Doctor, String> photoCol = new TableColumn<>("Photo");
        photoCol.setPrefWidth(80);
        photoCol.setCellFactory(col -> new TableCell<Doctor, String>() {
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
                        Text profileIcon = new Text("👨‍⚕️");
                        profileIcon.setStyle("-fx-font-size: 20px;");
                        setGraphic(profileIcon);
                        return;
                    }
                    setGraphic(imageView);
                }
            }
        });

        // Doctor ID Column
        TableColumn<Doctor, String> idCol = new TableColumn<>("Doctor ID");
        idCol.setPrefWidth(100);
        idCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));

        // Full Name Column
        TableColumn<Doctor, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setPrefWidth(150);
        nameCol.setCellValueFactory(cellData -> {
            Doctor doctor = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(doctor.getFullName());
        });

        // Specialization Column
        TableColumn<Doctor, String> specializationCol = new TableColumn<>("Specialization");
        specializationCol.setPrefWidth(150);
        specializationCol.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        // Department Column
        TableColumn<Doctor, String> departmentCol = new TableColumn<>("Department");
        departmentCol.setPrefWidth(120);
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));

        // Phone Column
        TableColumn<Doctor, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setPrefWidth(120);
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Email Column
        TableColumn<Doctor, String> emailCol = new TableColumn<>("Email");
        emailCol.setPrefWidth(180);
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Status Column
        TableColumn<Doctor, Boolean> statusCol = new TableColumn<>("Status");
        statusCol.setPrefWidth(80);
        statusCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        statusCol.setCellFactory(col -> new TableCell<Doctor, Boolean>() {
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
        TableColumn<Doctor, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(col -> new TableCell<Doctor, Void>() {
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
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    editBtn.setOnAction(e -> showEditDoctorDialog(doctor));
                    deleteBtn.setOnAction(e -> showDeleteDoctorDialog(doctor));
                    setGraphic(actionBox);
                }
            }
        });

        doctorsTable.getColumns().addAll(photoCol, idCol, nameCol, specializationCol, departmentCol, phoneCol, emailCol, statusCol, actionsCol);

        container.getChildren().addAll(tableTitle, doctorsTable);

        return container;
    }

    private void showAddDoctorDialog() {
        showDoctorDialog(null, "Add New Doctor");
    }

    private void showEditDoctorDialog(Doctor doctor) {
        showDoctorDialog(doctor, "Edit Doctor");
    }

    private void showDoctorDialog(Doctor doctor, String title) {
        Dialog<Doctor> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("Enter doctor information");

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
            // Fallback to medical cross symbol
            Text fallbackIcon = new Text("👨‍⚕️");
            fallbackIcon.setStyle("-fx-font-size: 40px;");
            VBox fallbackBox = new VBox();
            fallbackBox.setAlignment(Pos.CENTER);
            fallbackBox.getChildren().add(fallbackIcon);
            fallbackBox.setPrefSize(80, 80);
            fallbackBox.setStyle("-fx-border: 2px solid #E2E8F0; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        }

        Button uploadPhotoBtn = new Button("Upload Photo");
        uploadPhotoBtn.getStyleClass().add("secondary-button");
        uploadPhotoBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Doctor Photo");
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

        // Personal Information Fields
        TextField firstNameField = new TextField();
        firstNameField.getStyleClass().add("modern-text-field");
        firstNameField.setPromptText("First Name");
        if (doctor != null) firstNameField.setText(doctor.getFirstName());

        TextField lastNameField = new TextField();
        lastNameField.getStyleClass().add("modern-text-field");
        lastNameField.setPromptText("Last Name");
        if (doctor != null) lastNameField.setText(doctor.getLastName());

        TextField doctorIdField = new TextField();
        doctorIdField.getStyleClass().add("modern-text-field");
        doctorIdField.setPromptText("Doctor ID (e.g., DOC001)");
        if (doctor != null) doctorIdField.setText(doctor.getDoctorId());

        TextField emailField = new TextField();
        emailField.getStyleClass().add("modern-text-field");
        emailField.setPromptText("Email Address");
        if (doctor != null) emailField.setText(doctor.getEmail());

        TextField phoneField = new TextField();
        phoneField.getStyleClass().add("modern-text-field");
        phoneField.setPromptText("Phone Number");
        if (doctor != null) phoneField.setText(doctor.getPhone());

        // Professional Information Fields
        ComboBox<String> specializationField = new ComboBox<>();
        specializationField.getStyleClass().add("modern-combo-box");
        specializationField.getItems().addAll(
            "Cardiology", "Neurology", "Orthopedics", "Pediatrics",
            "Dermatology", "General Medicine", "Surgery", "Radiology",
            "Psychiatry", "Oncology", "Gastroenterology", "Pulmonology"
        );
        specializationField.setPromptText("Select Specialization");
        if (doctor != null) specializationField.setValue(doctor.getSpecialization());

        ComboBox<String> departmentField = new ComboBox<>();
        departmentField.getStyleClass().add("modern-combo-box");
        departmentField.getItems().addAll(
            "Emergency", "ICU", "Surgery", "Outpatient", "Radiology",
            "Cardiology", "Neurology", "Pediatrics", "Orthopedics"
        );
        departmentField.setPromptText("Select Department");
        if (doctor != null) departmentField.setValue(doctor.getDepartment());

        TextField qualificationField = new TextField();
        qualificationField.getStyleClass().add("modern-text-field");
        qualificationField.setPromptText("Qualifications (e.g., MBBS, MD)");
        if (doctor != null) qualificationField.setText(doctor.getQualification());

        TextField experienceField = new TextField();
        experienceField.getStyleClass().add("modern-text-field");
        experienceField.setPromptText("Years of Experience");

        DatePicker joiningDateField = new DatePicker();
        joiningDateField.getStyleClass().add("modern-date-picker");
        joiningDateField.setPromptText("Joining Date");
        if (doctor != null) joiningDateField.setValue(doctor.getJoiningDate());
        else joiningDateField.setValue(LocalDate.now());

        ComboBox<String> statusField = new ComboBox<>();
        statusField.getStyleClass().add("modern-combo-box");
        statusField.getItems().addAll("Active", "Inactive", "On Leave");
        statusField.setValue("Active");

        // Add form fields to grid
        grid.add(new Label("First Name:"), 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(new Label("Last Name:"), 0, 2);
        grid.add(lastNameField, 1, 2);
        grid.add(new Label("Doctor ID:"), 0, 3);
        grid.add(doctorIdField, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(new Label("Phone:"), 0, 5);
        grid.add(phoneField, 1, 5);
        grid.add(new Label("Specialization:"), 0, 6);
        grid.add(specializationField, 1, 6);
        grid.add(new Label("Department:"), 0, 7);
        grid.add(departmentField, 1, 7);
        grid.add(new Label("Qualification:"), 0, 8);
        grid.add(qualificationField, 1, 8);
        grid.add(new Label("Experience:"), 0, 9);
        grid.add(experienceField, 1, 9);
        grid.add(new Label("Joining Date:"), 0, 10);
        grid.add(joiningDateField, 1, 10);
        grid.add(new Label("Status:"), 0, 11);
        grid.add(statusField, 1, 11);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Style buttons
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        okButton.getStyleClass().add("modern-button");
        cancelButton.getStyleClass().add("secondary-button");

        // Convert result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    if (doctor == null) {
                        // Create new doctor
                        Doctor newDoctor = new Doctor(
                            firstNameField.getText(),
                            lastNameField.getText(),
                            specializationField.getValue(),
                            emailField.getText(),
                            phoneField.getText()
                        );
                        newDoctor.setDoctorId(doctorIdField.getText());
                        newDoctor.setDepartment(departmentField.getValue());
                        newDoctor.setQualification(qualificationField.getText());
                        newDoctor.setJoiningDate(joiningDateField.getValue());
                        newDoctor.setActive("Active".equals(statusField.getValue()));

                        doctorsList.add(newDoctor);
                        showAlert("Success", "Doctor added successfully!");
                        return newDoctor;
                    } else {
                        // Update existing doctor
                        doctor.setFirstName(firstNameField.getText());
                        doctor.setLastName(lastNameField.getText());
                        doctor.setEmail(emailField.getText());
                        doctor.setPhone(phoneField.getText());
                        doctor.setDoctorId(doctorIdField.getText());
                        doctor.setSpecialization(specializationField.getValue());
                        doctor.setDepartment(departmentField.getValue());
                        doctor.setQualification(qualificationField.getText());
                        doctor.setJoiningDate(joiningDateField.getValue());
                        doctor.setActive("Active".equals(statusField.getValue()));

                        doctorsTable.refresh();
                        showAlert("Success", "Doctor updated successfully!");
                        return doctor;
                    }
                } catch (Exception e) {
                    showAlert("Error", "Failed to save doctor: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showDeleteDoctorDialog(Doctor doctor) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Doctor");
        alert.setHeaderText("Are you sure you want to delete this doctor?");
        alert.setContentText("Doctor: " + doctor.getFullName() + " (" + doctor.getDoctorId() + ")");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                doctorsList.remove(doctor);
                showAlert("Success", "Doctor deleted successfully!");
            }
        });
    }

    private void loadSampleDoctors() {
        try {
            // Prevent multiple loading
            if (!doctorsList.isEmpty()) {
                return;
            }

            // Add sample doctors
            Doctor doctor1 = new Doctor("John", "Smith", "Cardiology", "john.smith@medisys.com", "+91-9876543101");
            doctor1.setId(1L);
            doctor1.setDoctorId("DOC001");
            doctor1.setQualification("MD, MBBS");
            doctor1.setDepartment("Cardiology");
            doctor1.setDesignation("Senior Consultant");
            doctor1.setJoiningDate(LocalDate.of(2020, 1, 15));
            doctor1.setConsultationFee(1500.0);
            doctor1.setMorningStartTime(LocalTime.of(9, 0));
            doctor1.setMorningEndTime(LocalTime.of(13, 0));
            doctor1.setEveningStartTime(LocalTime.of(17, 0));
            doctor1.setEveningEndTime(LocalTime.of(20, 0));
            doctor1.setWorkingDays("Monday to Friday");

            Doctor doctor2 = new Doctor("Sarah", "Johnson", "Neurology", "sarah.johnson@medisys.com", "+91-9876543102");
            doctor2.setId(2L);
            doctor2.setDoctorId("DOC002");
            doctor2.setQualification("MD, DM Neurology");
            doctor2.setDepartment("Neurology");
            doctor2.setDesignation("Consultant");
            doctor2.setJoiningDate(LocalDate.of(2019, 6, 10));
            doctor2.setConsultationFee(1800.0);
            doctor2.setMorningStartTime(LocalTime.of(10, 0));
            doctor2.setMorningEndTime(LocalTime.of(14, 0));
            doctor2.setWorkingDays("Monday to Saturday");

            Doctor doctor3 = new Doctor("Michael", "Brown", "Orthopedics", "michael.brown@medisys.com", "+91-9876543103");
            doctor3.setId(3L);
            doctor3.setDoctorId("DOC003");
            doctor3.setQualification("MS Orthopedics");
            doctor3.setDepartment("Orthopedics");
            doctor3.setDesignation("Senior Consultant");
            doctor3.setJoiningDate(LocalDate.of(2018, 3, 20));
            doctor3.setConsultationFee(1200.0);
            doctor3.setMorningStartTime(LocalTime.of(8, 30));
            doctor3.setMorningEndTime(LocalTime.of(12, 30));
            doctor3.setEveningStartTime(LocalTime.of(16, 0));
            doctor3.setEveningEndTime(LocalTime.of(19, 0));
            doctor3.setWorkingDays("Monday to Friday");

            doctorsList.addAll(doctor1, doctor2, doctor3);
            System.out.println("✅ Sample doctors loaded successfully");
        } catch (Exception e) {
            System.err.println("⚠️ Error loading sample doctors: " + e.getMessage());
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
