package com.medisys.desktop.ui.modules;

import com.medisys.desktop.model.User;
import com.medisys.desktop.ui.forms.AppointmentForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Modern Appointments Module with calendar and scheduling
 */
public class AppointmentsModule {

    private final User currentUser;
    private final VBox root;
    private TableView<Appointment> appointmentsTable;
    private ObservableList<Appointment> appointmentsList;
    private DatePicker datePicker;

    public AppointmentsModule(User currentUser) {
        this.currentUser = currentUser;
        this.root = new VBox(20);
        this.appointmentsList = FXCollections.observableArrayList();

        initializeUI();
        loadSampleAppointments();
    }

    private void initializeUI() {
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        // Header
        HBox header = createHeader();

        // Calendar and filters
        HBox calendarSection = createCalendarSection();

        // Appointments table
        VBox tableContainer = createAppointmentsTable();

        root.getChildren().addAll(header, calendarSection, tableContainer);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);

        Text title = new Text("📅 Appointments Management");
        title.getStyleClass().add("header-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addAppointmentBtn = new Button("+ Schedule Appointment");
        addAppointmentBtn.getStyleClass().add("modern-button");
        addAppointmentBtn.setOnAction(e -> showAddAppointmentDialog());

        header.getChildren().addAll(title, spacer, addAppointmentBtn);

        return header;
    }

    private HBox createCalendarSection() {
        HBox calendarSection = new HBox(20);
        calendarSection.setAlignment(Pos.CENTER_LEFT);
        calendarSection.setPadding(new Insets(10));
        calendarSection.getStyleClass().add("modern-card");

        // Date picker
        VBox dateSection = new VBox(5);
        dateSection.setAlignment(Pos.CENTER_LEFT);

        Text dateLabel = new Text("📅 Select Date:");
        dateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        datePicker = new DatePicker(LocalDate.now());
        datePicker.setPromptText("Select appointment date");

        dateSection.getChildren().addAll(dateLabel, datePicker);

        // Status filters
        VBox statusSection = new VBox(5);
        statusSection.setAlignment(Pos.CENTER_LEFT);

        Text statusLabel = new Text("📊 Filter by Status:");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getStyleClass().add("modern-combo-box");
        statusFilter.getItems().addAll("All Status", "Scheduled", "Confirmed", "In Progress", "Completed", "Cancelled");
        statusFilter.setValue("All Status");

        statusSection.getChildren().addAll(statusLabel, statusFilter);

        // Doctor filter
        VBox doctorSection = new VBox(5);
        doctorSection.setAlignment(Pos.CENTER_LEFT);

        Text doctorLabel = new Text("👨‍⚕️ Filter by Doctor:");
        doctorLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        ComboBox<String> doctorFilter = new ComboBox<>();
        doctorFilter.getStyleClass().add("modern-combo-box");
        doctorFilter.getItems().addAll("All Doctors", "Dr. John Smith", "Dr. Sarah Johnson", "Dr. Michael Brown");
        doctorFilter.setValue("All Doctors");

        doctorSection.getChildren().addAll(doctorLabel, doctorFilter);

        // Action buttons
        VBox actionsSection = new VBox(5);
        actionsSection.setAlignment(Pos.CENTER);

        Button todayBtn = new Button("Today");
        todayBtn.getStyleClass().add("secondary-button");
        todayBtn.setOnAction(e -> datePicker.setValue(LocalDate.now()));

        Button refreshBtn = new Button("Refresh");
        refreshBtn.getStyleClass().add("secondary-button");
        refreshBtn.setOnAction(e -> loadSampleAppointments());

        actionsSection.getChildren().addAll(todayBtn, refreshBtn);

        calendarSection.getChildren().addAll(dateSection, statusSection, doctorSection, actionsSection);

        return calendarSection;
    }

    private VBox createAppointmentsTable() {
        VBox container = new VBox(10);
        container.getStyleClass().add("modern-card");

        Text tableTitle = new Text("Today's Appointments");
        tableTitle.getStyleClass().add("section-title");

        // Create table
        appointmentsTable = new TableView<>();
        appointmentsTable.getStyleClass().add("modern-table-view");
        appointmentsTable.setPrefHeight(400);
        appointmentsTable.setItems(appointmentsList);

        // Time Column
        TableColumn<Appointment, String> timeCol = new TableColumn<>("Time");
        timeCol.setPrefWidth(80);
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));

        // Patient Column
        TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient");
        patientCol.setPrefWidth(150);
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        // Doctor Column
        TableColumn<Appointment, String> doctorCol = new TableColumn<>("Doctor");
        doctorCol.setPrefWidth(150);
        doctorCol.setCellValueFactory(new PropertyValueFactory<>("doctorName"));

        // Type Column
        TableColumn<Appointment, String> typeCol = new TableColumn<>("Type");
        typeCol.setPrefWidth(120);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));

        // Status Column
        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setPrefWidth(100);
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<Appointment, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Label statusLabel = new Label(item);
                    statusLabel.getStyleClass().add(getStatusStyle(item));
                    setGraphic(statusLabel);
                }
            }
        });

        // Notes Column
        TableColumn<Appointment, String> notesCol = new TableColumn<>("Notes");
        notesCol.setPrefWidth(200);
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        // Actions Column
        TableColumn<Appointment, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(col -> new TableCell<Appointment, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button cancelBtn = new Button("Cancel");
            private final HBox actionBox = new HBox(5);

            {
                editBtn.getStyleClass().add("secondary-button");
                editBtn.setStyle("-fx-font-size: 10px; -fx-padding: 4 8 4 8;");
                cancelBtn.getStyleClass().add("error-button");
                cancelBtn.setStyle("-fx-font-size: 10px; -fx-padding: 4 8 4 8;");
                actionBox.getChildren().addAll(editBtn, cancelBtn);
                actionBox.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Appointment appointment = getTableView().getItems().get(getIndex());
                    editBtn.setOnAction(e -> showEditAppointmentDialog(appointment));
                    cancelBtn.setOnAction(e -> showCancelAppointmentDialog(appointment));
                    setGraphic(actionBox);
                }
            }
        });

        appointmentsTable.getColumns().addAll(timeCol, patientCol, doctorCol, typeCol, statusCol, notesCol, actionsCol);

        container.getChildren().addAll(tableTitle, appointmentsTable);

        return container;
    }

    private void showAddAppointmentDialog() {
        AppointmentForm form = new AppointmentForm(null, this::addAppointment);
        form.show();
    }

    private void showEditAppointmentDialog(Appointment appointment) {
        AppointmentForm form = new AppointmentForm(appointment, this::updateAppointment);
        form.show();
    }

    private void addAppointment(Appointment appointment) {
        appointmentsList.add(appointment);
        appointmentsTable.refresh();
    }

    private void updateAppointment(Appointment appointment) {
        appointmentsTable.refresh();
    }

    private void showCancelAppointmentDialog(Appointment appointment) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Appointment");
        alert.setHeaderText("Are you sure you want to cancel this appointment?");
        alert.setContentText("Patient: " + appointment.getPatientName() + "\nDoctor: " + appointment.getDoctorName() + "\nTime: " + appointment.getTimeSlot());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                appointment.setStatus("Cancelled");
                appointmentsTable.refresh();
                showAlert("Success", "Appointment cancelled successfully!");
            }
        });
    }

    private void loadSampleAppointments() {
        try {
            if (!appointmentsList.isEmpty()) {
                return;
            }

            // Add sample appointments
            Appointment apt1 = new Appointment();
            apt1.setId(1L);
            apt1.setPatientName("Alice Johnson");
            apt1.setDoctorName("Dr. John Smith");
            apt1.setAppointmentDate(LocalDate.now());
            apt1.setTimeSlot("09:00 AM");
            apt1.setAppointmentType("Consultation");
            apt1.setStatus("Confirmed");
            apt1.setNotes("Regular checkup");

            Appointment apt2 = new Appointment();
            apt2.setId(2L);
            apt2.setPatientName("Bob Wilson");
            apt2.setDoctorName("Dr. Sarah Johnson");
            apt2.setAppointmentDate(LocalDate.now());
            apt2.setTimeSlot("10:30 AM");
            apt2.setAppointmentType("Follow-up");
            apt2.setStatus("Scheduled");
            apt2.setNotes("Neurology follow-up");

            Appointment apt3 = new Appointment();
            apt3.setId(3L);
            apt3.setPatientName("Carol Davis");
            apt3.setDoctorName("Dr. Michael Brown");
            apt3.setAppointmentDate(LocalDate.now());
            apt3.setTimeSlot("02:00 PM");
            apt3.setAppointmentType("Surgery");
            apt3.setStatus("In Progress");
            apt3.setNotes("Orthopedic surgery");

            appointmentsList.addAll(apt1, apt2, apt3);
            System.out.println("✅ Sample appointments loaded successfully");
        } catch (Exception e) {
            System.err.println("⚠️ Error loading sample appointments: " + e.getMessage());
        }
    }

    private String getStatusStyle(String status) {
        return switch (status.toLowerCase()) {
            case "confirmed" -> "status-active";
            case "scheduled" -> "status-active";
            case "in progress" -> "status-warning";
            case "completed" -> "status-success";
            case "cancelled" -> "status-inactive";
            default -> "status-inactive";
        };
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

    // Simple Appointment class for demo
    public static class Appointment {
        private Long id;
        private String patientName;
        private String doctorName;
        private LocalDate appointmentDate;
        private String timeSlot;
        private String appointmentType;
        private String status;
        private String notes;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getPatientName() { return patientName; }
        public void setPatientName(String patientName) { this.patientName = patientName; }

        public String getDoctorName() { return doctorName; }
        public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

        public LocalDate getAppointmentDate() { return appointmentDate; }
        public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

        public String getTimeSlot() { return timeSlot; }
        public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

        public String getAppointmentType() { return appointmentType; }
        public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}
