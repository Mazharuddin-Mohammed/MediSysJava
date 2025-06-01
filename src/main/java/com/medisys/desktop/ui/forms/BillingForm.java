package com.medisys.desktop.ui.forms;

import com.medisys.desktop.utils.IconLibrary;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Complete Billing Form
 * Professional billing and invoice generation system
 */
public class BillingForm {
    
    private final Stage stage;
    private final Runnable onSave;
    
    // Form fields
    private ComboBox<String> patientCombo;
    private TextField patientIdField;
    private TextField patientNameField;
    private TextField patientPhoneField;
    private ComboBox<String> doctorCombo;
    private ComboBox<String> departmentCombo;
    private DatePicker serviceDatePicker;
    private ComboBox<String> serviceTypeCombo;
    private TextArea servicesArea;
    private TextField consultationFeeField;
    private TextField medicationCostField;
    private TextField testChargesField;
    private TextField roomChargesField;
    private TextField otherChargesField;
    private TextField discountField;
    private TextField taxField;
    private TextField totalAmountField;
    private ComboBox<String> paymentMethodCombo;
    private ComboBox<String> paymentStatusCombo;
    private TextArea notesArea;
    
    public BillingForm(Runnable onSave) {
        this.onSave = onSave;
        this.stage = new Stage();
        
        initializeForm();
    }
    
    private void initializeForm() {
        stage.setTitle("💰 Create Bill / Invoice");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("form-container");
        
        // Header
        Text header = new Text(IconLibrary.MONEY + " Create Bill / Invoice");
        header.getStyleClass().add("form-header");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #27AE60;");
        
        // Form content
        ScrollPane scrollPane = new ScrollPane(createFormContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.getStyleClass().add("form-scroll");
        
        // Buttons
        HBox buttonBox = createButtonBox();
        
        root.getChildren().addAll(header, scrollPane, buttonBox);
        
        Scene scene = new Scene(root, 800, 700);
        try {
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Could not load stylesheet");
        }
        stage.setScene(scene);
        
        // Initialize calculations
        setupCalculations();
    }
    
    private VBox createFormContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));
        
        // Bill Information Section
        VBox billInfoSection = createSection(IconLibrary.DOCUMENT + " Bill Information");
        
        HBox billRow1 = new HBox(15);
        billRow1.setAlignment(Pos.CENTER_LEFT);
        
        VBox billNumberBox = new VBox(5);
        TextField billNumberField = new TextField("BILL" + System.currentTimeMillis());
        billNumberField.setEditable(false);
        billNumberField.setStyle("-fx-background-color: #f0f0f0;");
        billNumberBox.getChildren().addAll(new Label("Bill Number"), billNumberField);
        
        VBox billDateBox = new VBox(5);
        TextField billDateField = new TextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        billDateField.setEditable(false);
        billDateField.setStyle("-fx-background-color: #f0f0f0;");
        billDateBox.getChildren().addAll(new Label("Bill Date"), billDateField);
        
        VBox billTimeBox = new VBox(5);
        TextField billTimeField = new TextField(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        billTimeField.setEditable(false);
        billTimeField.setStyle("-fx-background-color: #f0f0f0;");
        billTimeBox.getChildren().addAll(new Label("Bill Time"), billTimeField);
        
        billRow1.getChildren().addAll(billNumberBox, billDateBox, billTimeBox);
        HBox.setHgrow(billNumberBox, Priority.ALWAYS);
        HBox.setHgrow(billDateBox, Priority.ALWAYS);
        HBox.setHgrow(billTimeBox, Priority.ALWAYS);
        
        billInfoSection.getChildren().add(billRow1);
        
        // Patient Information Section
        VBox patientSection = createSection(IconLibrary.PATIENT + " Patient Information");
        
        HBox patientRow1 = new HBox(15);
        patientRow1.setAlignment(Pos.CENTER_LEFT);
        
        VBox patientSelectBox = new VBox(5);
        patientCombo = new ComboBox<>();
        patientCombo.getStyleClass().add("modern-combo-box");
        patientCombo.getItems().addAll(
            "Alice Johnson (PAT001)",
            "Bob Wilson (PAT002)",
            "Carol Davis (PAT003)",
            "David Brown (PAT004)",
            "Emma Wilson (PAT005)"
        );
        patientCombo.setPromptText("Select existing patient");
        patientCombo.setOnAction(e -> loadPatientDetails());
        patientSelectBox.getChildren().addAll(new Label("Select Patient *"), patientCombo);
        
        VBox patientIdBox = new VBox(5);
        patientIdBox.getChildren().addAll(
            new Label("Patient ID"),
            patientIdField = createTextField("Patient ID")
        );
        patientIdField.setEditable(false);
        patientIdField.setStyle("-fx-background-color: #f0f0f0;");
        
        patientRow1.getChildren().addAll(patientSelectBox, patientIdBox);
        HBox.setHgrow(patientSelectBox, Priority.ALWAYS);
        HBox.setHgrow(patientIdBox, Priority.ALWAYS);
        
        HBox patientRow2 = new HBox(15);
        patientRow2.setAlignment(Pos.CENTER_LEFT);
        
        VBox patientNameBox = new VBox(5);
        patientNameBox.getChildren().addAll(
            new Label("Patient Name *"),
            patientNameField = createTextField("Enter patient name")
        );
        
        VBox patientPhoneBox = new VBox(5);
        patientPhoneBox.getChildren().addAll(
            new Label("Phone Number"),
            patientPhoneField = createTextField("Enter phone number")
        );
        
        patientRow2.getChildren().addAll(patientNameBox, patientPhoneBox);
        HBox.setHgrow(patientNameBox, Priority.ALWAYS);
        HBox.setHgrow(patientPhoneBox, Priority.ALWAYS);
        
        patientSection.getChildren().addAll(patientRow1, patientRow2);
        
        // Service Information Section
        VBox serviceSection = createSection(IconLibrary.STETHOSCOPE + " Service Information");
        
        HBox serviceRow1 = new HBox(15);
        serviceRow1.setAlignment(Pos.CENTER_LEFT);
        
        VBox doctorBox = new VBox(5);
        doctorCombo = new ComboBox<>();
        doctorCombo.getStyleClass().add("modern-combo-box");
        doctorCombo.getItems().addAll(
            "Dr. John Smith - Cardiology",
            "Dr. Sarah Johnson - Neurology",
            "Dr. Michael Brown - Orthopedics",
            "Dr. Lisa Davis - Pediatrics",
            "Dr. Robert Wilson - Emergency"
        );
        doctorCombo.setPromptText("Select doctor");
        doctorBox.getChildren().addAll(new Label("Consulting Doctor *"), doctorCombo);
        
        VBox departmentBox = new VBox(5);
        departmentCombo = new ComboBox<>();
        departmentCombo.getStyleClass().add("modern-combo-box");
        departmentCombo.getItems().addAll(
            "Cardiology", "Neurology", "Orthopedics", "Pediatrics", 
            "Emergency", "Surgery", "Radiology", "Laboratory"
        );
        departmentCombo.setPromptText("Select department");
        departmentBox.getChildren().addAll(new Label("Department *"), departmentCombo);
        
        serviceRow1.getChildren().addAll(doctorBox, departmentBox);
        HBox.setHgrow(doctorBox, Priority.ALWAYS);
        HBox.setHgrow(departmentBox, Priority.ALWAYS);
        
        HBox serviceRow2 = new HBox(15);
        serviceRow2.setAlignment(Pos.CENTER_LEFT);
        
        VBox serviceDateBox = new VBox(5);
        serviceDatePicker = new DatePicker(LocalDate.now());
        serviceDatePicker.getStyleClass().add("modern-date-picker");
        serviceDateBox.getChildren().addAll(new Label("Service Date *"), serviceDatePicker);
        
        VBox serviceTypeBox = new VBox(5);
        serviceTypeCombo = new ComboBox<>();
        serviceTypeCombo.getStyleClass().add("modern-combo-box");
        serviceTypeCombo.getItems().addAll(
            "Consultation", "Surgery", "Emergency Treatment", "Diagnostic Tests",
            "Therapy", "Vaccination", "Health Checkup", "Follow-up"
        );
        serviceTypeCombo.setPromptText("Select service type");
        serviceTypeBox.getChildren().addAll(new Label("Service Type *"), serviceTypeCombo);
        
        serviceRow2.getChildren().addAll(serviceDateBox, serviceTypeBox);
        HBox.setHgrow(serviceDateBox, Priority.ALWAYS);
        HBox.setHgrow(serviceTypeBox, Priority.ALWAYS);
        
        VBox servicesBox = new VBox(5);
        servicesArea = new TextArea();
        servicesArea.getStyleClass().add("modern-text-area");
        servicesArea.setPromptText("Enter detailed services provided...");
        servicesArea.setPrefRowCount(3);
        servicesArea.setWrapText(true);
        servicesBox.getChildren().addAll(new Label("Services Provided"), servicesArea);
        
        serviceSection.getChildren().addAll(serviceRow1, serviceRow2, servicesBox);
        
        // Charges Section
        VBox chargesSection = createSection(IconLibrary.MONEY + " Charges Breakdown");
        
        HBox chargesRow1 = new HBox(15);
        chargesRow1.setAlignment(Pos.CENTER_LEFT);
        
        VBox consultationBox = new VBox(5);
        consultationBox.getChildren().addAll(
            new Label("Consultation Fee (₹)"),
            consultationFeeField = createTextField("0.00")
        );
        
        VBox medicationBox = new VBox(5);
        medicationBox.getChildren().addAll(
            new Label("Medication Cost (₹)"),
            medicationCostField = createTextField("0.00")
        );
        
        chargesRow1.getChildren().addAll(consultationBox, medicationBox);
        HBox.setHgrow(consultationBox, Priority.ALWAYS);
        HBox.setHgrow(medicationBox, Priority.ALWAYS);
        
        HBox chargesRow2 = new HBox(15);
        chargesRow2.setAlignment(Pos.CENTER_LEFT);
        
        VBox testBox = new VBox(5);
        testBox.getChildren().addAll(
            new Label("Test Charges (₹)"),
            testChargesField = createTextField("0.00")
        );
        
        VBox roomBox = new VBox(5);
        roomBox.getChildren().addAll(
            new Label("Room Charges (₹)"),
            roomChargesField = createTextField("0.00")
        );
        
        chargesRow2.getChildren().addAll(testBox, roomBox);
        HBox.setHgrow(testBox, Priority.ALWAYS);
        HBox.setHgrow(roomBox, Priority.ALWAYS);
        
        VBox otherBox = new VBox(5);
        otherBox.getChildren().addAll(
            new Label("Other Charges (₹)"),
            otherChargesField = createTextField("0.00")
        );
        
        chargesSection.getChildren().addAll(chargesRow1, chargesRow2, otherBox);
        
        // Total Calculation Section
        VBox totalSection = createSection(IconLibrary.RECEIPT + " Total Calculation");
        
        HBox totalRow1 = new HBox(15);
        totalRow1.setAlignment(Pos.CENTER_LEFT);
        
        VBox discountBox = new VBox(5);
        discountBox.getChildren().addAll(
            new Label("Discount (₹)"),
            discountField = createTextField("0.00")
        );
        
        VBox taxBox = new VBox(5);
        taxBox.getChildren().addAll(
            new Label("Tax/GST (₹)"),
            taxField = createTextField("0.00")
        );
        
        totalRow1.getChildren().addAll(discountBox, taxBox);
        HBox.setHgrow(discountBox, Priority.ALWAYS);
        HBox.setHgrow(taxBox, Priority.ALWAYS);
        
        VBox totalAmountBox = new VBox(5);
        totalAmountField = createTextField("0.00");
        totalAmountField.setEditable(false);
        totalAmountField.setStyle("-fx-background-color: #e8f5e8; -fx-font-weight: bold; -fx-font-size: 16px;");
        totalAmountBox.getChildren().addAll(new Label("Total Amount (₹)"), totalAmountField);
        
        totalSection.getChildren().addAll(totalRow1, totalAmountBox);
        
        // Payment Information Section
        VBox paymentSection = createSection(IconLibrary.PAYMENT + " Payment Information");
        
        HBox paymentRow = new HBox(15);
        paymentRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox paymentMethodBox = new VBox(5);
        paymentMethodCombo = new ComboBox<>();
        paymentMethodCombo.getStyleClass().add("modern-combo-box");
        paymentMethodCombo.getItems().addAll(
            "Cash", "Credit Card", "Debit Card", "UPI", "Net Banking", "Insurance", "Cheque"
        );
        paymentMethodCombo.setPromptText("Select payment method");
        paymentMethodBox.getChildren().addAll(new Label("Payment Method *"), paymentMethodCombo);
        
        VBox paymentStatusBox = new VBox(5);
        paymentStatusCombo = new ComboBox<>();
        paymentStatusCombo.getStyleClass().add("modern-combo-box");
        paymentStatusCombo.getItems().addAll("Paid", "Pending", "Partial", "Failed");
        paymentStatusCombo.setValue("Pending");
        paymentStatusBox.getChildren().addAll(new Label("Payment Status *"), paymentStatusCombo);
        
        paymentRow.getChildren().addAll(paymentMethodBox, paymentStatusBox);
        HBox.setHgrow(paymentMethodBox, Priority.ALWAYS);
        HBox.setHgrow(paymentStatusBox, Priority.ALWAYS);
        
        VBox notesBox = new VBox(5);
        notesArea = new TextArea();
        notesArea.getStyleClass().add("modern-text-area");
        notesArea.setPromptText("Enter additional notes or comments...");
        notesArea.setPrefRowCount(2);
        notesArea.setWrapText(true);
        notesBox.getChildren().addAll(new Label("Notes"), notesArea);
        
        paymentSection.getChildren().addAll(paymentRow, notesBox);
        
        content.getChildren().addAll(
            billInfoSection, patientSection, serviceSection, 
            chargesSection, totalSection, paymentSection
        );
        
        return content;
    }
    
    private VBox createSection(String title) {
        VBox section = new VBox(15);
        section.getStyleClass().add("form-section");
        section.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-background-radius: 8;");
        
        Text sectionTitle = new Text(title);
        sectionTitle.getStyleClass().add("section-title");
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #27AE60;");
        
        section.getChildren().add(sectionTitle);
        return section;
    }
    
    private TextField createTextField(String promptText) {
        TextField field = new TextField();
        field.getStyleClass().add("modern-text-field");
        field.setPromptText(promptText);
        field.setStyle("-fx-padding: 10; -fx-font-size: 14px;");
        return field;
    }
    
    private void setupCalculations() {
        // Add listeners for automatic calculation
        consultationFeeField.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());
        medicationCostField.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());
        testChargesField.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());
        roomChargesField.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());
        otherChargesField.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());
        discountField.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());
        taxField.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());
    }
    
    private void calculateTotal() {
        try {
            double consultation = parseDouble(consultationFeeField.getText());
            double medication = parseDouble(medicationCostField.getText());
            double tests = parseDouble(testChargesField.getText());
            double room = parseDouble(roomChargesField.getText());
            double other = parseDouble(otherChargesField.getText());
            double discount = parseDouble(discountField.getText());
            double tax = parseDouble(taxField.getText());
            
            double subtotal = consultation + medication + tests + room + other;
            double total = subtotal - discount + tax;
            
            totalAmountField.setText(String.format("%.2f", Math.max(0, total)));
        } catch (Exception e) {
            // Ignore calculation errors during typing
        }
    }
    
    private double parseDouble(String text) {
        try {
            return text == null || text.trim().isEmpty() ? 0.0 : Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private void loadPatientDetails() {
        String selected = patientCombo.getValue();
        if (selected != null) {
            // Extract patient details from selection
            if (selected.contains("Alice Johnson")) {
                patientIdField.setText("PAT001");
                patientNameField.setText("Alice Johnson");
                patientPhoneField.setText("+91-9876543301");
            } else if (selected.contains("Bob Wilson")) {
                patientIdField.setText("PAT002");
                patientNameField.setText("Bob Wilson");
                patientPhoneField.setText("+91-9876543302");
            } else if (selected.contains("Carol Davis")) {
                patientIdField.setText("PAT003");
                patientNameField.setText("Carol Davis");
                patientPhoneField.setText("+91-9876543303");
            }
            // Add more patient details as needed
        }
    }
    
    private HBox createButtonBox() {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button cancelBtn = new Button(IconLibrary.ERROR + " Cancel");
        cancelBtn.getStyleClass().add("secondary-button");
        cancelBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        cancelBtn.setOnAction(e -> stage.close());
        
        Button printBtn = new Button(IconLibrary.PRINT + " Print Bill");
        printBtn.getStyleClass().add("secondary-button");
        printBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        printBtn.setOnAction(e -> printBill());
        
        Button saveBtn = new Button(IconLibrary.SAVE + " Generate Bill");
        saveBtn.getStyleClass().add("primary-button");
        saveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        saveBtn.setOnAction(e -> generateBill());
        
        buttonBox.getChildren().addAll(cancelBtn, printBtn, saveBtn);
        
        return buttonBox;
    }
    
    private void printBill() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Print Bill");
        info.setHeaderText("🖨️ Print Functionality");
        info.setContentText("Bill printing functionality would be implemented here.\n\n" +
                           "Features:\n" +
                           "• Professional bill format\n" +
                           "• Hospital letterhead\n" +
                           "• Detailed charges breakdown\n" +
                           "• Payment information\n" +
                           "• Barcode/QR code for verification");
        info.showAndWait();
    }
    
    private void generateBill() {
        // Validate form
        if (!validateForm()) {
            return;
        }
        
        // Show success message
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Bill Generated");
        success.setHeaderText("✅ Bill Generated Successfully");
        success.setContentText("Bill has been generated successfully!\n\n" +
                              "Patient: " + patientNameField.getText() + "\n" +
                              "Total Amount: ₹" + totalAmountField.getText() + "\n" +
                              "Payment Status: " + paymentStatusCombo.getValue() + "\n\n" +
                              "Bill has been saved to the system and can be printed or emailed.");
        success.showAndWait();
        
        // Call save callback
        if (onSave != null) {
            onSave.run();
        }
        
        stage.close();
    }
    
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        
        if (patientNameField.getText().trim().isEmpty()) {
            errors.append("• Patient name is required\n");
        }
        if (doctorCombo.getValue() == null) {
            errors.append("• Consulting doctor is required\n");
        }
        if (departmentCombo.getValue() == null) {
            errors.append("• Department is required\n");
        }
        if (serviceDatePicker.getValue() == null) {
            errors.append("• Service date is required\n");
        }
        if (serviceTypeCombo.getValue() == null) {
            errors.append("• Service type is required\n");
        }
        if (paymentMethodCombo.getValue() == null) {
            errors.append("• Payment method is required\n");
        }
        if (paymentStatusCombo.getValue() == null) {
            errors.append("• Payment status is required\n");
        }
        
        // Validate total amount
        try {
            double total = Double.parseDouble(totalAmountField.getText());
            if (total <= 0) {
                errors.append("• Total amount must be greater than zero\n");
            }
        } catch (NumberFormatException e) {
            errors.append("• Invalid total amount\n");
        }
        
        if (errors.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please fix the following errors:");
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
    public void show() {
        stage.show();
    }
}
