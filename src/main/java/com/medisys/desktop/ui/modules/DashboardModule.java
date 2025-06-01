package com.medisys.desktop.ui.modules;

import com.medisys.desktop.model.User;
import com.medisys.desktop.ui.components.QuickActionsPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Modern Dashboard Module with statistics and charts
 */
public class DashboardModule {
    
    private final User currentUser;
    private final VBox root;
    
    public DashboardModule(User currentUser) {
        this.currentUser = currentUser;
        this.root = new VBox(20);
        
        initializeUI();
    }
    
    private void initializeUI() {
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);
        
        // Welcome header
        VBox header = createWelcomeHeader();
        
        // Statistics cards
        HBox statsRow = createStatsCards();
        
        // Charts section
        HBox chartsRow = createChartsSection();
        
        // Recent activities
        VBox recentActivities = createRecentActivities();
        
        root.getChildren().addAll(header, statsRow, chartsRow, recentActivities);
    }
    
    private VBox createWelcomeHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        Text welcomeText = new Text("Welcome back, " + currentUser.getFirstName() + "!");
        welcomeText.getStyleClass().add("header-title");
        
        Text dateText = new Text("Today is " + java.time.LocalDate.now().toString());
        dateText.getStyleClass().add("header-subtitle");
        
        header.getChildren().addAll(welcomeText, dateText);
        
        return header;
    }
    
    private HBox createStatsCards() {
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        
        // Create stat cards based on user role
        if ("admin".equals(currentUser.getRole())) {
            statsRow.getChildren().addAll(
                createStatCard("👥", "Total Patients", "1,234", "#2E86AB"),
                createStatCard("👨‍⚕️", "Active Doctors", "45", "#A23B72"),
                createStatCard("📅", "Today's Appointments", "89", "#F18F01"),
                createStatCard("💰", "Monthly Revenue", "₹2,45,000", "#4ECDC4")
            );
        } else if ("doctor".equals(currentUser.getRole())) {
            statsRow.getChildren().addAll(
                createStatCard("👥", "My Patients", "156", "#2E86AB"),
                createStatCard("📅", "Today's Appointments", "12", "#F18F01"),
                createStatCard("⏰", "Next Appointment", "2:30 PM", "#A23B72"),
                createStatCard("📊", "This Week", "67 Patients", "#4ECDC4")
            );
        } else if ("finance".equals(currentUser.getRole())) {
            statsRow.getChildren().addAll(
                createStatCard("💰", "Today's Revenue", "₹45,000", "#4ECDC4"),
                createStatCard("📋", "Pending Bills", "23", "#F18F01"),
                createStatCard("💳", "Insurance Claims", "15", "#A23B72"),
                createStatCard("📈", "Monthly Growth", "+12%", "#2E86AB")
            );
        } else {
            // Default stats for other roles
            statsRow.getChildren().addAll(
                createStatCard("📊", "Tasks Today", "8", "#2E86AB"),
                createStatCard("✅", "Completed", "5", "#4ECDC4"),
                createStatCard("⏳", "Pending", "3", "#F18F01"),
                createStatCard("📈", "Efficiency", "87%", "#A23B72")
            );
        }
        
        return statsRow;
    }
    
    private VBox createStatCard(String icon, String label, String value, String color) {
        VBox card = new VBox(10);
        card.getStyleClass().add("stats-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setPrefHeight(120);
        
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 24px;");
        
        Text valueText = new Text(value);
        valueText.getStyleClass().add("stats-number");
        valueText.setStyle("-fx-fill: " + color + ";");
        
        Text labelText = new Text(label);
        labelText.getStyleClass().add("stats-label");
        
        card.getChildren().addAll(iconText, valueText, labelText);
        
        return card;
    }
    
    private HBox createChartsSection() {
        HBox chartsRow = new HBox(20);
        chartsRow.setAlignment(Pos.CENTER);
        chartsRow.setPrefHeight(300);
        
        // Patient visits chart
        VBox chartContainer = new VBox(10);
        chartContainer.getStyleClass().add("modern-card");
        chartContainer.setPrefWidth(600);
        
        Text chartTitle = new Text("Patient Visits This Week");
        chartTitle.getStyleClass().add("section-title");
        
        BarChart<String, Number> chart = createPatientVisitsChart();
        
        chartContainer.getChildren().addAll(chartTitle, chart);
        
        // Quick actions panel
        QuickActionsPanel quickActionsPanel = new QuickActionsPanel(currentUser);
        VBox quickActions = quickActionsPanel.getRoot();
        
        chartsRow.getChildren().addAll(chartContainer, quickActions);
        
        return chartsRow;
    }
    
    private BarChart<String, Number> createPatientVisitsChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days");
        yAxis.setLabel("Patients");
        
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Daily Patient Visits");
        chart.setPrefHeight(250);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Patients");
        
        // Sample data
        series.getData().add(new XYChart.Data<>("Mon", 45));
        series.getData().add(new XYChart.Data<>("Tue", 52));
        series.getData().add(new XYChart.Data<>("Wed", 38));
        series.getData().add(new XYChart.Data<>("Thu", 61));
        series.getData().add(new XYChart.Data<>("Fri", 47));
        series.getData().add(new XYChart.Data<>("Sat", 33));
        series.getData().add(new XYChart.Data<>("Sun", 28));
        
        chart.getData().add(series);
        chart.setLegendVisible(false);
        
        return chart;
    }
    
    private VBox createQuickActionsPanel() {
        VBox quickActions = new VBox(15);
        quickActions.getStyleClass().add("modern-card");
        quickActions.setPrefWidth(300);
        quickActions.setAlignment(Pos.TOP_LEFT);
        
        Text title = new Text("Quick Actions");
        title.getStyleClass().add("section-title");
        
        VBox actionsList = new VBox(10);
        
        // Role-based quick actions
        if ("admin".equals(currentUser.getRole())) {
            actionsList.getChildren().addAll(
                createQuickActionButton("👥", "Add New Patient"),
                createQuickActionButton("👨‍⚕️", "Register Doctor"),
                createQuickActionButton("📊", "Generate Report"),
                createQuickActionButton("⚙️", "System Settings")
            );
        } else if ("doctor".equals(currentUser.getRole())) {
            actionsList.getChildren().addAll(
                createQuickActionButton("👥", "View My Patients"),
                createQuickActionButton("📅", "Today's Schedule"),
                createQuickActionButton("📝", "Add Medical Record"),
                createQuickActionButton("💊", "Prescribe Medication")
            );
        } else {
            actionsList.getChildren().addAll(
                createQuickActionButton("👥", "Search Patient"),
                createQuickActionButton("📅", "Book Appointment"),
                createQuickActionButton("📊", "View Reports"),
                createQuickActionButton("📞", "Contact Support")
            );
        }
        
        quickActions.getChildren().addAll(title, actionsList);
        
        return quickActions;
    }
    
    private HBox createQuickActionButton(String icon, String text) {
        HBox button = new HBox(10);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(10));
        button.setStyle("""
            -fx-background-color: #F8F9FA;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-cursor: hand;
            """);
        
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 16px;");
        
        Text labelText = new Text(text);
        labelText.setStyle("""
            -fx-font-size: 14px;
            -fx-text-fill: #2C3E50;
            """);
        
        button.getChildren().addAll(iconText, labelText);
        
        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle("""
            -fx-background-color: #E9ECEF;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-cursor: hand;
            """));
        
        button.setOnMouseExited(e -> button.setStyle("""
            -fx-background-color: #F8F9FA;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-cursor: hand;
            """));
        
        return button;
    }
    
    private VBox createRecentActivities() {
        VBox activities = new VBox(15);
        activities.getStyleClass().add("modern-card");
        
        Text title = new Text("Recent Activities");
        title.getStyleClass().add("section-title");
        
        VBox activitiesList = new VBox(10);
        
        // Sample activities
        activitiesList.getChildren().addAll(
            createActivityItem("👥", "New patient registered: John Doe", "2 minutes ago"),
            createActivityItem("📅", "Appointment scheduled with Dr. Smith", "15 minutes ago"),
            createActivityItem("💰", "Payment received: ₹5,000", "1 hour ago"),
            createActivityItem("📊", "Monthly report generated", "2 hours ago"),
            createActivityItem("👨‍⚕️", "Dr. Johnson updated schedule", "3 hours ago")
        );
        
        activities.getChildren().addAll(title, activitiesList);
        
        return activities;
    }
    
    private HBox createActivityItem(String icon, String description, String time) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("""
            -fx-background-color: #F8F9FA;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            """);
        
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 16px;");
        
        VBox content = new VBox(2);
        
        Text descText = new Text(description);
        descText.setStyle("""
            -fx-font-size: 14px;
            -fx-text-fill: #2C3E50;
            """);
        
        Text timeText = new Text(time);
        timeText.setStyle("""
            -fx-font-size: 12px;
            -fx-text-fill: #7F8C8D;
            """);
        
        content.getChildren().addAll(descText, timeText);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        item.getChildren().addAll(iconText, content, spacer);
        
        return item;
    }
    
    public VBox getRoot() {
        return root;
    }
}
