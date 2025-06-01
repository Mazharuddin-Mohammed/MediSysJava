package com.medisys.desktop.ui.modules;

import com.medisys.desktop.model.User;
import com.medisys.desktop.utils.ReportExporter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.chart.*;

import java.time.LocalDate;

/**
 * Modern Reports & Analytics Module with charts and statistics
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

        // Header
        HBox header = createHeader();

        // Report filters
        HBox filtersSection = createFiltersSection();

        // Statistics cards
        HBox statsCards = createStatsCards();

        // Charts section
        HBox chartsSection = createChartsSection();

        // Quick reports
        VBox quickReports = createQuickReports();

        root.getChildren().addAll(header, filtersSection, statsCards, chartsSection, quickReports);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);

        Text title = new Text("📊 Reports & Analytics");
        title.getStyleClass().add("header-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button exportBtn = new Button("📤 Export Report");
        exportBtn.getStyleClass().add("modern-button");
        exportBtn.setOnAction(e -> showExportDialog());

        Button printBtn = new Button("🖨️ Print");
        printBtn.getStyleClass().add("secondary-button");
        printBtn.setOnAction(e -> showPrintDialog());

        header.getChildren().addAll(title, spacer, exportBtn, printBtn);

        return header;
    }

    private HBox createFiltersSection() {
        HBox filtersSection = new HBox(20);
        filtersSection.setAlignment(Pos.CENTER_LEFT);
        filtersSection.setPadding(new Insets(10));
        filtersSection.getStyleClass().add("modern-card");

        // Date range
        VBox dateSection = new VBox(5);
        dateSection.setAlignment(Pos.CENTER_LEFT);

        Text dateLabel = new Text("📅 Date Range:");
        dateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        HBox dateRange = new HBox(10);
        DatePicker fromDate = new DatePicker(LocalDate.now().minusMonths(1));
        fromDate.setPromptText("From Date");

        Text toText = new Text("to");
        toText.setStyle("-fx-font-size: 12px;");

        DatePicker toDate = new DatePicker(LocalDate.now());
        toDate.setPromptText("To Date");

        dateRange.getChildren().addAll(fromDate, toText, toDate);
        dateSection.getChildren().addAll(dateLabel, dateRange);

        // Report type
        VBox typeSection = new VBox(5);
        typeSection.setAlignment(Pos.CENTER_LEFT);

        Text typeLabel = new Text("📋 Report Type:");
        typeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        ComboBox<String> reportType = new ComboBox<>();
        reportType.getStyleClass().add("modern-combo-box");
        reportType.getItems().addAll("All Reports", "Patient Statistics", "Financial Summary", "Doctor Performance", "Appointment Analytics", "Revenue Analysis");
        reportType.setValue("All Reports");

        typeSection.getChildren().addAll(typeLabel, reportType);

        // Department filter
        VBox deptSection = new VBox(5);
        deptSection.setAlignment(Pos.CENTER_LEFT);

        Text deptLabel = new Text("🏥 Department:");
        deptLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        ComboBox<String> department = new ComboBox<>();
        department.getStyleClass().add("modern-combo-box");
        department.getItems().addAll("All Departments", "Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Emergency");
        department.setValue("All Departments");

        deptSection.getChildren().addAll(deptLabel, department);

        // Generate button
        VBox actionSection = new VBox(5);
        actionSection.setAlignment(Pos.CENTER);

        Button generateBtn = new Button("Generate Report");
        generateBtn.getStyleClass().add("modern-button");
        generateBtn.setOnAction(e -> generateReport());

        actionSection.getChildren().add(generateBtn);

        filtersSection.getChildren().addAll(dateSection, typeSection, deptSection, actionSection);

        return filtersSection;
    }

    private HBox createStatsCards() {
        HBox statsCards = new HBox(20);
        statsCards.setAlignment(Pos.CENTER);
        statsCards.setPadding(new Insets(10));

        // Total Patients Card
        VBox patientsCard = createStatsCard("👥 Total Patients", "1,247", "#4ECDC4", "+12% this month");

        // Total Revenue Card
        VBox revenueCard = createStatsCard("💰 Total Revenue", "₹12,45,000", "#2E86AB", "+8% this month");

        // Appointments Card
        VBox appointmentsCard = createStatsCard("📅 Appointments", "856", "#F18F01", "+15% this month");

        // Doctors Card
        VBox doctorsCard = createStatsCard("👨‍⚕️ Active Doctors", "24", "#A23B72", "3 new this month");

        statsCards.getChildren().addAll(patientsCard, revenueCard, appointmentsCard, doctorsCard);

        return statsCards;
    }

    private VBox createStatsCard(String title, String value, String color, String trend) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.getStyleClass().add("modern-card");
        card.setStyle("-fx-border-left: 5px solid " + color + ";");

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #2C3E50;");

        Text valueText = new Text(value);
        valueText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: " + color + ";");

        Text trendText = new Text(trend);
        trendText.setStyle("-fx-font-size: 12px; -fx-fill: #27AE60;");

        card.getChildren().addAll(titleText, valueText, trendText);

        return card;
    }

    private HBox createChartsSection() {
        HBox chartsSection = new HBox(20);
        chartsSection.setAlignment(Pos.CENTER);
        chartsSection.setPadding(new Insets(10));

        // Patient Visits Chart
        VBox visitChartContainer = new VBox(10);
        visitChartContainer.getStyleClass().add("modern-card");
        visitChartContainer.setPadding(new Insets(20));

        Text visitChartTitle = new Text("📈 Patient Visits Trend");
        visitChartTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        LineChart<String, Number> visitChart = createVisitChart();
        visitChart.setPrefHeight(300);

        visitChartContainer.getChildren().addAll(visitChartTitle, visitChart);

        // Revenue Chart
        VBox revenueChartContainer = new VBox(10);
        revenueChartContainer.getStyleClass().add("modern-card");
        revenueChartContainer.setPadding(new Insets(20));

        Text revenueChartTitle = new Text("💰 Revenue Distribution");
        revenueChartTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        PieChart revenueChart = createRevenueChart();
        revenueChart.setPrefHeight(300);

        revenueChartContainer.getChildren().addAll(revenueChartTitle, revenueChart);

        chartsSection.getChildren().addAll(visitChartContainer, revenueChartContainer);

        return chartsSection;
    }

    private LineChart<String, Number> createVisitChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        yAxis.setLabel("Patient Visits");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Monthly Patient Visits");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Visits");
        series.getData().add(new XYChart.Data<>("Jan", 120));
        series.getData().add(new XYChart.Data<>("Feb", 135));
        series.getData().add(new XYChart.Data<>("Mar", 150));
        series.getData().add(new XYChart.Data<>("Apr", 145));
        series.getData().add(new XYChart.Data<>("May", 160));
        series.getData().add(new XYChart.Data<>("Jun", 175));

        lineChart.getData().add(series);
        return lineChart;
    }

    private PieChart createRevenueChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Revenue by Department");

        PieChart.Data slice1 = new PieChart.Data("Cardiology", 35);
        PieChart.Data slice2 = new PieChart.Data("Neurology", 25);
        PieChart.Data slice3 = new PieChart.Data("Orthopedics", 20);
        PieChart.Data slice4 = new PieChart.Data("Emergency", 15);
        PieChart.Data slice5 = new PieChart.Data("Others", 5);

        pieChart.getData().addAll(slice1, slice2, slice3, slice4, slice5);
        return pieChart;
    }

    private VBox createQuickReports() {
        VBox quickReports = new VBox(15);
        quickReports.getStyleClass().add("modern-card");
        quickReports.setPadding(new Insets(20));

        Text title = new Text("⚡ Quick Reports");
        title.getStyleClass().add("section-title");

        // Quick report buttons
        HBox reportButtons = new HBox(15);
        reportButtons.setAlignment(Pos.CENTER_LEFT);

        Button dailyReportBtn = new Button("📅 Daily Summary");
        dailyReportBtn.getStyleClass().add("secondary-button");
        dailyReportBtn.setOnAction(e -> showDailyReport());

        Button weeklyReportBtn = new Button("📊 Weekly Analysis");
        weeklyReportBtn.getStyleClass().add("secondary-button");
        weeklyReportBtn.setOnAction(e -> showWeeklyReport());

        Button monthlyReportBtn = new Button("📈 Monthly Report");
        monthlyReportBtn.getStyleClass().add("secondary-button");
        monthlyReportBtn.setOnAction(e -> showMonthlyReport());

        Button patientReportBtn = new Button("👥 Patient Report");
        patientReportBtn.getStyleClass().add("secondary-button");
        patientReportBtn.setOnAction(e -> showPatientReport());

        Button financeReportBtn = new Button("💰 Finance Report");
        financeReportBtn.getStyleClass().add("secondary-button");
        financeReportBtn.setOnAction(e -> showFinanceReport());

        reportButtons.getChildren().addAll(dailyReportBtn, weeklyReportBtn, monthlyReportBtn, patientReportBtn, financeReportBtn);

        quickReports.getChildren().addAll(title, reportButtons);

        return quickReports;
    }

    private void showExportDialog() {
        // Create a sample data list for demonstration
        javafx.collections.ObservableList<String> sampleData = javafx.collections.FXCollections.observableArrayList(
            "Patient Report - Alice Johnson - Cardiology - 2024-01-15",
            "Patient Report - Bob Wilson - Neurology - 2024-01-16",
            "Patient Report - Carol Davis - Orthopedics - 2024-01-17",
            "Financial Report - Revenue: ₹2,45,000 - January 2024",
            "Appointment Report - 156 appointments - January 2024"
        );

        // Show export options dialog
        Alert exportDialog = new Alert(Alert.AlertType.CONFIRMATION);
        exportDialog.setTitle("Export Report");
        exportDialog.setHeaderText("Choose Export Type");

        ButtonType allDeptBtn = new ButtonType("All Departments");
        ButtonType cardiologyBtn = new ButtonType("Cardiology Only");
        ButtonType neurologyBtn = new ButtonType("Neurology Only");
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        exportDialog.getButtonTypes().setAll(allDeptBtn, cardiologyBtn, neurologyBtn, cancelBtn);

        exportDialog.showAndWait().ifPresent(response -> {
            try {
                javafx.stage.Stage stage = (javafx.stage.Stage) root.getScene().getWindow();

                if (response == allDeptBtn) {
                    ReportExporter.exportAllDepartmentsReport(sampleData, stage);
                } else if (response == cardiologyBtn) {
                    ReportExporter.exportDepartmentReport("Cardiology", sampleData, stage);
                } else if (response == neurologyBtn) {
                    ReportExporter.exportDepartmentReport("Neurology", sampleData, stage);
                }
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Export Error");
                errorAlert.setContentText("Failed to export report: " + e.getMessage());
                errorAlert.showAndWait();
            }
        });
    }

    private void showPrintDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Print Report");
        dialog.setHeaderText("Print Options");
        dialog.setContentText("Print functionality will be implemented here with:\n• Print preview\n• Page setup\n• Printer selection\n• Print quality options");
        dialog.showAndWait();
    }

    private void generateReport() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Generate Report");
        dialog.setHeaderText("Report Generation");
        dialog.setContentText("Report generation in progress...\n\nThis will include:\n• Data compilation\n• Chart generation\n• Statistical analysis\n• Report formatting");
        dialog.showAndWait();
    }

    private void showDailyReport() {
        javafx.collections.ObservableList<String> dailyData = javafx.collections.FXCollections.observableArrayList(
            "Daily Patients: 45", "Daily Revenue: ₹85,000", "Appointments: 32", "Emergency Cases: 8"
        );
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) root.getScene().getWindow();
            ReportExporter.exportReport("Daily Summary Report", "All Departments", dailyData, stage);
        } catch (Exception e) {
            showReportDialog("Daily Summary Report", "Today's hospital statistics and activities");
        }
    }

    private void showWeeklyReport() {
        javafx.collections.ObservableList<String> weeklyData = javafx.collections.FXCollections.observableArrayList(
            "Weekly Patients: 315", "Weekly Revenue: ₹5,95,000", "Appointments: 224", "Surgery Cases: 18"
        );
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) root.getScene().getWindow();
            ReportExporter.exportReport("Weekly Analysis Report", "All Departments", weeklyData, stage);
        } catch (Exception e) {
            showReportDialog("Weekly Analysis Report", "Weekly trends and performance metrics");
        }
    }

    private void showMonthlyReport() {
        javafx.collections.ObservableList<String> monthlyData = javafx.collections.FXCollections.observableArrayList(
            "Monthly Patients: 1,247", "Monthly Revenue: ₹24,50,000", "Appointments: 856", "Surgery Cases: 72"
        );
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) root.getScene().getWindow();
            ReportExporter.exportReport("Monthly Report", "All Departments", monthlyData, stage);
        } catch (Exception e) {
            showReportDialog("Monthly Report", "Comprehensive monthly hospital report");
        }
    }

    private void showPatientReport() {
        javafx.collections.ObservableList<String> patientData = javafx.collections.FXCollections.observableArrayList(
            "Total Patients: 1,247", "New Patients: 156", "Regular Patients: 1,091", "Average Age: 42 years"
        );
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) root.getScene().getWindow();
            ReportExporter.exportPatientReport(patientData, stage);
        } catch (Exception e) {
            showReportDialog("Patient Statistics Report", "Patient demographics and visit patterns");
        }
    }

    private void showFinanceReport() {
        javafx.collections.ObservableList<String> financeData = javafx.collections.FXCollections.observableArrayList(
            "Total Revenue: ₹24,50,000", "Pending Payments: ₹45,000", "Insurance Claims: ₹12,00,000", "Expenses: ₹8,50,000"
        );
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) root.getScene().getWindow();
            ReportExporter.exportFinancialReport(financeData, stage);
        } catch (Exception e) {
            showReportDialog("Financial Report", "Revenue, expenses, and financial analysis");
        }
    }

    private void showReportDialog(String title, String description) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle(title);
        dialog.setHeaderText("Report Details");
        dialog.setContentText(description + "\n\nReport features:\n• Interactive charts\n• Detailed statistics\n• Trend analysis\n• Export options");
        dialog.showAndWait();
    }

    public VBox getRoot() {
        return root;
    }
}
