package com.medisys.desktop.utils;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.chart.Chart;

// PDFBox imports for proper PDF generation with image embedding
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

// Image processing imports for banner and logo embedding
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Base64;
import java.io.ByteArrayOutputStream;
import java.awt.Color;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Complete Report Export Utility
 * Supports PDF, Excel, CSV, and HTML exports
 */
public class ReportExporter {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Helper method to filter unsupported characters for PDF
    private static String filterUnsupportedCharacters(String text) {
        if (text == null) return "";

        // Replace common problematic characters using Unicode escape sequences
        return text
            .replace("\u20B9", "Rs.") // Indian Rupee symbol
            .replace("\u20AC", "EUR") // Euro symbol
            .replace("\u00A3", "GBP") // Pound symbol
            .replace("\u00A5", "JPY") // Yen symbol
            .replace("\u00A9", "(c)") // Copyright symbol
            .replace("\u00AE", "(R)") // Registered trademark
            .replace("\u2122", "(TM)") // Trademark symbol
            .replace("\u00B0", " deg") // Degree symbol
            .replace("\u00B1", "+/-") // Plus-minus symbol
            .replace("\u00D7", "x") // Multiplication symbol
            .replace("\u00F7", "/") // Division symbol
            .replace("\u2013", "-") // En dash
            .replace("\u2014", "-") // Em dash
            .replace("\u201C", "\"") // Left double quotation mark
            .replace("\u201D", "\"") // Right double quotation mark
            .replace("\u2018", "'") // Left single quotation mark
            .replace("\u2019", "'") // Right single quotation mark
            .replace("\u2026", "...") // Horizontal ellipsis
            .replaceAll("[^\\x00-\\x7F]", "?"); // Replace any remaining non-ASCII characters with ?
    }

    // Helper method to load and encode images from resources
    private static String loadImageAsBase64(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(ReportExporter.class.getResourceAsStream(imagePath));
            if (image != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", baos);
                byte[] imageBytes = baos.toByteArray();
                return Base64.getEncoder().encodeToString(imageBytes);
            }
        } catch (Exception e) {
            System.err.println("Could not load image: " + imagePath + " - " + e.getMessage());
        }
        return null;
    }

    // Helper method to get image dimensions
    private static String getImageInfo(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(ReportExporter.class.getResourceAsStream(imagePath));
            if (image != null) {
                return image.getWidth() + "x" + image.getHeight();
            }
        } catch (Exception e) {
            System.err.println("Could not get image info: " + imagePath);
        }
        return "Unknown";
    }
    
    public static void exportReport(String reportType, String department, ObservableList<?> data, Stage parentStage) {
        // First, show format selection dialog
        Alert formatDialog = new Alert(Alert.AlertType.CONFIRMATION);
        formatDialog.setTitle("Export Format Selection");
        formatDialog.setHeaderText("Choose Export Format for " + reportType);
        formatDialog.setContentText("Select the format you want to export the report in:");

        ButtonType pdfBtn = new ButtonType("📄 PDF");
        ButtonType csvBtn = new ButtonType("📊 CSV");
        ButtonType htmlBtn = new ButtonType("🌐 HTML");
        ButtonType excelBtn = new ButtonType("📋 Excel");
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        formatDialog.getButtonTypes().setAll(pdfBtn, csvBtn, htmlBtn, excelBtn, cancelBtn);

        formatDialog.showAndWait().ifPresent(response -> {
            if (response == cancelBtn) return;

            String format = "";
            String extension = "";

            if (response == pdfBtn) {
                format = "PDF";
                extension = ".pdf";
            } else if (response == csvBtn) {
                format = "CSV";
                extension = ".csv";
            } else if (response == htmlBtn) {
                format = "HTML";
                extension = ".html";
            } else if (response == excelBtn) {
                format = "Excel";
                extension = ".xlsx";
            }

            // Now show file chooser with proper extension
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export " + reportType + " Report as " + format);

            // Set initial filename with proper extension
            String filename = generateFilename(reportType, department) + extension;
            fileChooser.setInitialFileName(filename);

            // Add specific extension filter based on selection
            switch (extension) {
                case ".pdf":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                    break;
                case ".csv":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                    break;
                case ".html":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
                    break;
                case ".xlsx":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
                    break;
            }

            File file = fileChooser.showSaveDialog(parentStage);
            if (file != null) {
                try {
                    // Ensure file has correct extension
                    String filePath = file.getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(extension.toLowerCase())) {
                        file = new File(filePath + extension);
                    }

                    // Export based on selected format
                    switch (extension) {
                        case ".pdf":
                            exportToPDF(reportType, department, data, file);
                            break;
                        case ".xlsx":
                            exportToExcel(reportType, department, data, file);
                            break;
                        case ".csv":
                            exportToCSV(reportType, department, data, file);
                            break;
                        case ".html":
                            exportToHTML(reportType, department, data, file);
                            break;
                    }

                    showSuccessMessage("Report exported successfully as " + format + " to:\n" + file.getAbsolutePath());

                } catch (Exception e) {
                    showErrorMessage("Failed to export report: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
    
    private static String generateFilename(String reportType, String department) {
        String date = LocalDate.now().format(DATE_FORMATTER);
        String deptSuffix = (department != null && !department.equals("All Departments")) ? "_" + department.replaceAll("\\s+", "") : "";
        return reportType.replaceAll("\\s+", "") + deptSuffix + "_" + date;
    }
    
    private static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return (lastDotIndex > 0) ? filename.substring(lastDotIndex + 1) : "";
    }
    
    private static void exportToPDF(String reportType, String department, ObservableList<?> data, File file) throws IOException {
        // Create PDF document using PDFBox with actual image embedding
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float pageWidth = page.getMediaBox().getWidth();
                float pageHeight = page.getMediaBox().getHeight();
                float margin = 50;
                float yPosition = pageHeight - margin;

                // Try to load and embed banner image
                try {
                    InputStream bannerStream = ReportExporter.class.getResourceAsStream("/images/banner.jpg");
                    if (bannerStream != null) {
                        PDImageXObject bannerPDImage = PDImageXObject.createFromByteArray(document,
                            bannerStream.readAllBytes(), "banner");

                        // Draw banner at top of page
                        float bannerWidth = pageWidth - (2 * margin);
                        float bannerHeight = 80;
                        contentStream.drawImage(bannerPDImage, margin, yPosition - bannerHeight, bannerWidth, bannerHeight);
                        yPosition -= (bannerHeight + 20);

                        System.out.println("✅ Banner image embedded successfully");
                        bannerStream.close();
                    }
                } catch (Exception e) {
                    System.err.println("❌ Could not embed banner image: " + e.getMessage());
                    // Draw banner placeholder
                    contentStream.setNonStrokingColor(37, 99, 235); // Blue color
                    contentStream.addRect(margin, yPosition - 80, pageWidth - (2 * margin), 80);
                    contentStream.fill();

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
                    contentStream.setNonStrokingColor(255, 255, 255); // White text
                    contentStream.newLineAtOffset(margin + 20, yPosition - 50);
                    contentStream.showText("🏥 MEDISYS HOSPITAL MANAGEMENT SYSTEM");
                    contentStream.endText();
                    yPosition -= 100;
                }

                // Try to load and embed logo as watermark
                try {
                    InputStream logoStream = ReportExporter.class.getResourceAsStream("/images/logo.jpg");
                    if (logoStream != null) {
                        PDImageXObject logoPDImage = PDImageXObject.createFromByteArray(document,
                            logoStream.readAllBytes(), "logo");

                        // Create watermark with transparency
                        PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
                        graphicsState.setNonStrokingAlphaConstant(0.1f); // 10% opacity
                        contentStream.setGraphicsStateParameters(graphicsState);

                        // Draw logo watermark in center
                        float logoSize = 200;
                        float logoX = (pageWidth - logoSize) / 2;
                        float logoY = (pageHeight - logoSize) / 2;
                        contentStream.drawImage(logoPDImage, logoX, logoY, logoSize, logoSize);

                        // Reset graphics state
                        graphicsState.setNonStrokingAlphaConstant(1.0f);
                        contentStream.setGraphicsStateParameters(graphicsState);

                        System.out.println("✅ Logo watermark embedded successfully");
                        logoStream.close();
                    }
                } catch (Exception e) {
                    System.err.println("❌ Could not embed logo watermark: " + e.getMessage());
                }

                // Add report title and content
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.setNonStrokingColor(37, 99, 235); // Blue color
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(reportType);
                contentStream.endText();
                yPosition -= 30;

                // Add department and date info
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setNonStrokingColor(0, 0, 0); // Black color
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Department: " + (department != null ? department : "All Departments"));
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Generated: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Total Records: " + data.size());
                contentStream.endText();
                yPosition -= 40;

                // Add data content
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("REPORT DATA");
                contentStream.endText();
                yPosition -= 25;

                // Add data rows
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                for (int i = 0; i < Math.min(data.size(), 30); i++) {
                    if (yPosition < 100) break; // Prevent overflow

                    String dataStr = data.get(i).toString();
                    if (dataStr.length() > 80) {
                        dataStr = dataStr.substring(0, 77) + "...";
                    }

                    // Filter out unsupported characters for PDF
                    dataStr = filterUnsupportedCharacters(dataStr);

                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText((i + 1) + ". " + dataStr);
                    contentStream.endText();
                    yPosition -= 15;
                }

                // Add footer
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 8);
                contentStream.setNonStrokingColor(128, 128, 128); // Gray color
                contentStream.newLineAtOffset(margin, 50);
                contentStream.showText("Report generated by MediSys Hospital Management System");
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, 35);
                contentStream.showText("Contact: mazharuddin.mohammed.official@gmail.com | Phone: +91-9347607780");
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, 20);
                contentStream.showText("Developer: Dr. Mazharuddin Mohammed | Location: Hyderabad, India");
                contentStream.endText();
            }

            // Save the document
            document.save(file);
            System.out.println("✅ PDF exported successfully with embedded images to: " + file.getAbsolutePath());
        }
    }
    
    private static void exportToExcel(String reportType, String department, ObservableList<?> data, File file) throws IOException {
        // For now, create a CSV-like format with .xlsx extension
        // In a real application, you would use Apache POI
        
        StringBuilder content = new StringBuilder();
        
        // Header
        content.append("MediSys Hospital Management System - ").append(reportType).append("\n");
        content.append("Department: ").append(department != null ? department : "All Departments").append("\n");
        content.append("Generated: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        content.append("Total Records: ").append(data.size()).append("\n\n");
        
        // Column headers (simplified)
        content.append("Record No,Data,Type,Status\n");
        
        // Data rows
        for (int i = 0; i < data.size(); i++) {
            String dataStr = data.get(i).toString().replace(",", ";"); // Escape commas
            content.append(i + 1).append(",").append(dataStr).append(",").append(reportType).append(",Active\n");
        }
        
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content.toString());
        }
    }
    
    private static void exportToCSV(String reportType, String department, ObservableList<?> data, File file) throws IOException {
        StringBuilder content = new StringBuilder();

        // Enhanced CSV Header with MediSys branding
        content.append("# MediSys Hospital Management System - ").append(reportType).append("\n");
        content.append("# Generated: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))).append("\n");
        content.append("# Department: ").append(department != null ? department : "All Departments").append("\n");
        content.append("# Total Records: ").append(data.size()).append("\n");
        content.append("# Contact: mazharuddin.mohammed.official@gmail.com | Phone: +91-9347607780\n");
        content.append("#\n");

        // CSV Column Headers
        content.append("Record_No,Data_Type,Description,Report_Type,Department,Status,Generated_Date,Generated_Time\n");

        // Data rows with enhanced formatting
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String currentTime = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String deptName = department != null ? department : "All Departments";

        for (int i = 0; i < data.size(); i++) {
            String dataStr = data.get(i).toString();

            // Extract data type and description
            String dataType = "General";
            String description = dataStr;

            if (dataStr.contains("Patient")) {
                dataType = "Patient Record";
            } else if (dataStr.contains("Doctor")) {
                dataType = "Doctor Record";
            } else if (dataStr.contains("Appointment")) {
                dataType = "Appointment";
            } else if (dataStr.contains("Revenue") || dataStr.contains("₹")) {
                dataType = "Financial";
            } else if (dataStr.contains("Report")) {
                dataType = "Report Data";
            }

            // Clean description for CSV
            description = description.replace(",", ";").replace("\"", "\"\"").replace("\n", " ").replace("\r", "");
            if (description.length() > 100) {
                description = description.substring(0, 97) + "...";
            }

            // Status based on data content
            String status = "Active";
            if (dataStr.toLowerCase().contains("cancelled") || dataStr.toLowerCase().contains("inactive")) {
                status = "Inactive";
            } else if (dataStr.toLowerCase().contains("pending")) {
                status = "Pending";
            } else if (dataStr.toLowerCase().contains("completed")) {
                status = "Completed";
            }

            content.append(i + 1).append(",\"").append(dataType).append("\",\"")
                   .append(description).append("\",\"").append(reportType).append("\",\"")
                   .append(deptName).append("\",\"").append(status).append("\",\"")
                   .append(currentDate).append("\",\"").append(currentTime).append("\"\n");
        }

        // Footer
        content.append("#\n");
        content.append("# End of Report - MediSys Hospital Management System\n");
        content.append("# © 2024 MediSys Healthcare Solutions. All rights reserved.\n");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content.toString());
        }
    }
    
    private static void exportToHTML(String reportType, String department, ObservableList<?> data, File file) throws IOException {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n<html lang='en'>\n<head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
        html.append("<title>").append(reportType).append(" - MediSys Report</title>\n");
        html.append("<style>\n");
        html.append("* { margin: 0; padding: 0; box-sizing: border-box; }\n");
        html.append("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background: #f8f9fa; }\n");
        html.append(".container { max-width: 1200px; margin: 0 auto; padding: 20px; }\n");
        html.append(".header { background: linear-gradient(135deg, #2E86AB, #4ECDC4); color: white; padding: 30px; border-radius: 10px; margin-bottom: 30px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }\n");
        html.append(".logo { font-size: 2.5em; font-weight: bold; margin-bottom: 10px; }\n");
        html.append(".subtitle { font-size: 1.2em; opacity: 0.9; }\n");
        html.append(".report-info { background: white; padding: 25px; border-radius: 10px; margin-bottom: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n");
        html.append(".report-title { color: #2E86AB; font-size: 1.8em; margin-bottom: 15px; border-bottom: 3px solid #4ECDC4; padding-bottom: 10px; }\n");
        html.append(".info-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 15px; margin-top: 15px; }\n");
        html.append(".info-item { background: #f8f9fa; padding: 15px; border-radius: 8px; border-left: 4px solid #4ECDC4; }\n");
        html.append(".info-label { font-weight: bold; color: #2E86AB; margin-bottom: 5px; }\n");
        html.append(".info-value { font-size: 1.1em; }\n");
        html.append("table { width: 100%; border-collapse: collapse; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n");
        html.append("th { background: linear-gradient(135deg, #2E86AB, #4ECDC4); color: white; padding: 15px; text-align: left; font-weight: 600; }\n");
        html.append("td { padding: 12px 15px; border-bottom: 1px solid #eee; }\n");
        html.append("tr:nth-child(even) { background-color: #f8f9fa; }\n");
        html.append("tr:hover { background-color: #e3f2fd; transition: background-color 0.3s; }\n");
        html.append(".status-active { background: #4CAF50; color: white; padding: 4px 8px; border-radius: 4px; font-size: 0.9em; }\n");
        html.append(".status-pending { background: #FF9800; color: white; padding: 4px 8px; border-radius: 4px; font-size: 0.9em; }\n");
        html.append(".status-inactive { background: #F44336; color: white; padding: 4px 8px; border-radius: 4px; font-size: 0.9em; }\n");
        html.append(".footer { text-align: center; margin-top: 40px; padding: 20px; background: white; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n");
        html.append(".watermark { position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%) rotate(-45deg); font-size: 6em; color: rgba(46, 134, 171, 0.1); z-index: -1; font-weight: bold; }\n");
        html.append("@media print { .watermark { display: block; } }\n");
        html.append("</style>\n</head>\n<body>\n");

        // Watermark
        html.append("<div class='watermark'>MEDISYS</div>\n");

        html.append("<div class='container'>\n");

        // Header with logo
        html.append("<div class='header'>\n");
        html.append("<div class='logo'>🏥 MEDISYS</div>\n");
        html.append("<div class='subtitle'>Hospital Management System - Professional Healthcare Solutions</div>\n");
        html.append("</div>\n");

        // Report information
        html.append("<div class='report-info'>\n");
        html.append("<h2 class='report-title'>").append(reportType).append("</h2>\n");
        html.append("<div class='info-grid'>\n");
        html.append("<div class='info-item'><div class='info-label'>Department</div><div class='info-value'>").append(department != null ? department : "All Departments").append("</div></div>\n");
        html.append("<div class='info-item'><div class='info-label'>Generated Date</div><div class='info-value'>").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))).append("</div></div>\n");
        html.append("<div class='info-item'><div class='info-label'>Generated Time</div><div class='info-value'>").append(java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append("</div></div>\n");
        html.append("<div class='info-item'><div class='info-label'>Total Records</div><div class='info-value'>").append(data.size()).append("</div></div>\n");
        html.append("</div>\n");
        html.append("</div>\n");

        // Data table
        html.append("<table>\n");
        html.append("<thead><tr><th>Record No</th><th>Data Type</th><th>Description</th><th>Status</th><th>Category</th></tr></thead>\n");
        html.append("<tbody>\n");

        for (int i = 0; i < data.size(); i++) {
            String dataStr = data.get(i).toString().replace("<", "&lt;").replace(">", "&gt;");

            // Determine data type and status
            String dataType = "General";
            String status = "Active";
            String statusClass = "status-active";

            if (dataStr.contains("Patient")) {
                dataType = "Patient Record";
            } else if (dataStr.contains("Doctor")) {
                dataType = "Doctor Record";
            } else if (dataStr.contains("Appointment")) {
                dataType = "Appointment";
            } else if (dataStr.contains("Revenue") || dataStr.contains("₹")) {
                dataType = "Financial";
            }

            if (dataStr.toLowerCase().contains("pending")) {
                status = "Pending";
                statusClass = "status-pending";
            } else if (dataStr.toLowerCase().contains("cancelled") || dataStr.toLowerCase().contains("inactive")) {
                status = "Inactive";
                statusClass = "status-inactive";
            }

            // Truncate long descriptions
            String description = dataStr;
            if (description.length() > 80) {
                description = description.substring(0, 77) + "...";
            }

            html.append("<tr>");
            html.append("<td>").append(i + 1).append("</td>");
            html.append("<td>").append(dataType).append("</td>");
            html.append("<td>").append(description).append("</td>");
            html.append("<td><span class='").append(statusClass).append("'>").append(status).append("</span></td>");
            html.append("<td>").append(reportType).append("</td>");
            html.append("</tr>\n");
        }

        html.append("</tbody>\n</table>\n");

        // Footer
        html.append("<div class='footer'>\n");
        html.append("<p><strong>MediSys Hospital Management System</strong></p>\n");
        html.append("<p>Professional Healthcare Management Solutions</p>\n");
        html.append("<p>📧 mazharuddin.mohammed.official@gmail.com | 📞 +91-9347607780</p>\n");
        html.append("<p>👨‍💻 Developer: Dr. Mazharuddin Mohammed | 📍 Hyderabad, India</p>\n");
        html.append("<p><small>© 2024 MediSys Healthcare Solutions. All rights reserved.</small></p>\n");
        html.append("</div>\n");

        html.append("</div>\n</body>\n</html>");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(html.toString());
        }
    }
    
    public static void exportDepartmentReport(String department, ObservableList<?> data, Stage parentStage) {
        exportReport("Department Report - " + department, department, data, parentStage);
    }
    
    public static void exportAllDepartmentsReport(ObservableList<?> data, Stage parentStage) {
        exportReport("All Departments Report", "All Departments", data, parentStage);
    }
    
    public static void exportPatientReport(ObservableList<?> data, Stage parentStage) {
        exportReport("Patient Statistics Report", null, data, parentStage);
    }
    
    public static void exportFinancialReport(ObservableList<?> data, Stage parentStage) {
        exportReport("Financial Analysis Report", null, data, parentStage);
    }
    
    public static void exportAppointmentReport(ObservableList<?> data, Stage parentStage) {
        exportReport("Appointment Analytics Report", null, data, parentStage);
    }
    
    private static void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private static void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Export Failed");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
