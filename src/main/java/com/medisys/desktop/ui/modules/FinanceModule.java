package com.medisys.desktop.ui.modules;

import com.medisys.desktop.model.User;
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

/**
 * Modern Finance Module with billing and payment management
 */
public class FinanceModule {

    private final User currentUser;
    private final VBox root;
    private TableView<Bill> billsTable;
    private ObservableList<Bill> billsList;
    private TextField searchField;

    public FinanceModule(User currentUser) {
        this.currentUser = currentUser;
        this.root = new VBox(20);
        this.billsList = FXCollections.observableArrayList();

        initializeUI();
        loadSampleBills();
    }

    private void initializeUI() {
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        // Header
        HBox header = createHeader();

        // Financial summary cards
        HBox summaryCards = createSummaryCards();

        // Search and filters
        HBox searchBar = createSearchBar();

        // Bills table
        VBox tableContainer = createBillsTable();

        root.getChildren().addAll(header, summaryCards, searchBar, tableContainer);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);

        Text title = new Text("💰 Finance Management");
        title.getStyleClass().add("header-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBillBtn = new Button("+ Create Bill");
        addBillBtn.getStyleClass().add("modern-button");
        addBillBtn.setOnAction(e -> showAddBillDialog());

        Button reportsBtn = new Button("📊 Reports");
        reportsBtn.getStyleClass().add("secondary-button");
        reportsBtn.setOnAction(e -> showReportsDialog());

        header.getChildren().addAll(title, spacer, addBillBtn, reportsBtn);

        return header;
    }

    private HBox createSummaryCards() {
        HBox summaryCards = new HBox(20);
        summaryCards.setAlignment(Pos.CENTER);
        summaryCards.setPadding(new Insets(10));

        // Total Revenue Card
        VBox revenueCard = createSummaryCard("💰 Total Revenue", "₹2,45,000", "#4ECDC4", "This Month");

        // Pending Payments Card
        VBox pendingCard = createSummaryCard("⏳ Pending Payments", "₹45,000", "#F18F01", "Outstanding");

        // Insurance Claims Card
        VBox insuranceCard = createSummaryCard("🏥 Insurance Claims", "₹1,20,000", "#A23B72", "Processing");

        // Today's Collection Card
        VBox todayCard = createSummaryCard("📅 Today's Collection", "₹15,000", "#2E86AB", "Today");

        summaryCards.getChildren().addAll(revenueCard, pendingCard, insuranceCard, todayCard);

        return summaryCards;
    }

    private VBox createSummaryCard(String title, String amount, String color, String subtitle) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("modern-card");
        card.setStyle("-fx-border-left: 5px solid " + color + ";");

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #2C3E50;");

        Text amountText = new Text(amount);
        amountText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text subtitleText = new Text(subtitle);
        subtitleText.setStyle("-fx-font-size: 12px; -fx-fill: #7F8C8D;");

        card.getChildren().addAll(titleText, amountText, subtitleText);

        return card;
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
        searchField.setPromptText("Search bills by patient name, bill number, or amount...");
        searchField.setPrefWidth(300);

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getStyleClass().add("modern-combo-box");
        statusFilter.getItems().addAll("All Status", "Paid", "Pending", "Overdue", "Cancelled");
        statusFilter.setValue("All Status");

        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.getStyleClass().add("modern-combo-box");
        typeFilter.getItems().addAll("All Types", "Consultation", "Surgery", "Lab Tests", "Pharmacy", "Room Charges");
        typeFilter.setValue("All Types");

        DatePicker fromDate = new DatePicker();
        fromDate.setPromptText("From Date");

        DatePicker toDate = new DatePicker();
        toDate.setPromptText("To Date");

        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("secondary-button");

        Button clearBtn = new Button("Clear");
        clearBtn.getStyleClass().add("warning-button");

        searchBar.getChildren().addAll(searchLabel, searchField, statusFilter, typeFilter, fromDate, toDate, searchBtn, clearBtn);

        return searchBar;
    }

    private VBox createBillsTable() {
        VBox container = new VBox(10);
        container.getStyleClass().add("modern-card");

        Text tableTitle = new Text("Recent Bills & Payments");
        tableTitle.getStyleClass().add("section-title");

        // Create table
        billsTable = new TableView<>();
        billsTable.getStyleClass().add("modern-table-view");
        billsTable.setPrefHeight(400);
        billsTable.setItems(billsList);

        // Bill Number Column
        TableColumn<Bill, String> billNoCol = new TableColumn<>("Bill No.");
        billNoCol.setPrefWidth(100);
        billNoCol.setCellValueFactory(new PropertyValueFactory<>("billNumber"));

        // Patient Column
        TableColumn<Bill, String> patientCol = new TableColumn<>("Patient");
        patientCol.setPrefWidth(150);
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        // Date Column
        TableColumn<Bill, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setPrefWidth(100);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("billDate"));

        // Service Type Column
        TableColumn<Bill, String> serviceCol = new TableColumn<>("Service");
        serviceCol.setPrefWidth(120);
        serviceCol.setCellValueFactory(new PropertyValueFactory<>("serviceType"));

        // Amount Column
        TableColumn<Bill, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setPrefWidth(100);
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setCellFactory(col -> new TableCell<Bill, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("₹" + String.format("%.2f", item));
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #2E86AB;");
                }
            }
        });

        // Status Column
        TableColumn<Bill, String> statusCol = new TableColumn<>("Status");
        statusCol.setPrefWidth(100);
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<Bill, String>() {
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

        // Actions Column
        TableColumn<Bill, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(col -> new TableCell<Bill, Void>() {
            private final Button viewBtn = new Button("View");
            private final Button payBtn = new Button("Pay");
            private final HBox actionBox = new HBox(5);

            {
                viewBtn.getStyleClass().addAll("action-button", "edit");
                payBtn.getStyleClass().addAll("action-button", "edit");
                actionBox.getChildren().addAll(viewBtn, payBtn);
                actionBox.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Bill bill = getTableView().getItems().get(getIndex());
                    viewBtn.setOnAction(e -> showBillDetails(bill));
                    payBtn.setOnAction(e -> showPaymentDialog(bill));
                    payBtn.setDisable("Paid".equals(bill.getStatus()));
                    setGraphic(actionBox);
                }
            }
        });

        billsTable.getColumns().addAll(billNoCol, patientCol, dateCol, serviceCol, amountCol, statusCol, actionsCol);

        container.getChildren().addAll(tableTitle, billsTable);

        return container;
    }

    private void showAddBillDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Create New Bill");
        dialog.setHeaderText("Billing Management");
        dialog.setContentText("Bill creation form will be implemented here with:\n• Patient selection\n• Service/treatment details\n• Insurance information\n• Payment terms\n• Automatic calculations");
        dialog.showAndWait();
    }

    private void showReportsDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Financial Reports");
        dialog.setHeaderText("Reports & Analytics");
        dialog.setContentText("Financial reports will be implemented here with:\n• Revenue analysis\n• Payment trends\n• Outstanding amounts\n• Insurance claims status\n• Monthly/yearly summaries");
        dialog.showAndWait();
    }

    private void showBillDetails(Bill bill) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Bill Details");
        dialog.setHeaderText("Bill #" + bill.getBillNumber());
        dialog.setContentText("Patient: " + bill.getPatientName() + "\nService: " + bill.getServiceType() + "\nAmount: ₹" + bill.getAmount() + "\nStatus: " + bill.getStatus());
        dialog.showAndWait();
    }

    private void showPaymentDialog(Bill bill) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Process Payment");
        alert.setHeaderText("Confirm Payment");
        alert.setContentText("Process payment for Bill #" + bill.getBillNumber() + "\nAmount: ₹" + bill.getAmount());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                bill.setStatus("Paid");
                billsTable.refresh();
                showAlert("Success", "Payment processed successfully!");
            }
        });
    }

    private void loadSampleBills() {
        try {
            if (!billsList.isEmpty()) {
                return;
            }

            // Add sample bills
            Bill bill1 = new Bill();
            bill1.setId(1L);
            bill1.setBillNumber("BILL001");
            bill1.setPatientName("Alice Johnson");
            bill1.setBillDate(LocalDate.now().minusDays(2));
            bill1.setServiceType("Consultation");
            bill1.setAmount(1500.0);
            bill1.setStatus("Paid");

            Bill bill2 = new Bill();
            bill2.setId(2L);
            bill2.setBillNumber("BILL002");
            bill2.setPatientName("Bob Wilson");
            bill2.setBillDate(LocalDate.now().minusDays(1));
            bill2.setServiceType("Lab Tests");
            bill2.setAmount(2500.0);
            bill2.setStatus("Pending");

            Bill bill3 = new Bill();
            bill3.setId(3L);
            bill3.setBillNumber("BILL003");
            bill3.setPatientName("Carol Davis");
            bill3.setBillDate(LocalDate.now());
            bill3.setServiceType("Surgery");
            bill3.setAmount(45000.0);
            bill3.setStatus("Pending");

            billsList.addAll(bill1, bill2, bill3);
            System.out.println("✅ Sample bills loaded successfully");
        } catch (Exception e) {
            System.err.println("⚠️ Error loading sample bills: " + e.getMessage());
        }
    }

    private String getStatusStyle(String status) {
        return switch (status.toLowerCase()) {
            case "paid" -> "status-active";
            case "pending" -> "status-warning";
            case "overdue" -> "status-inactive";
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

    // Simple Bill class for demo
    public static class Bill {
        private Long id;
        private String billNumber;
        private String patientName;
        private LocalDate billDate;
        private String serviceType;
        private Double amount;
        private String status;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getBillNumber() { return billNumber; }
        public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

        public String getPatientName() { return patientName; }
        public void setPatientName(String patientName) { this.patientName = patientName; }

        public LocalDate getBillDate() { return billDate; }
        public void setBillDate(LocalDate billDate) { this.billDate = billDate; }

        public String getServiceType() { return serviceType; }
        public void setServiceType(String serviceType) { this.serviceType = serviceType; }

        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
