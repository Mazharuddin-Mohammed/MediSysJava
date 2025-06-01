package com.medisys.desktop.util;

import javafx.scene.Scene;

/**
 * Modern Style Manager for MediSys Application
 * Provides vibrant, modern, and responsive styling
 */
public class StyleManager {
    
    // Modern Color Palette
    public static final String PRIMARY_COLOR = "#2E86AB";      // Ocean Blue
    public static final String SECONDARY_COLOR = "#A23B72";    // Magenta
    public static final String ACCENT_COLOR = "#F18F01";       // Orange
    public static final String SUCCESS_COLOR = "#C73E1D";      // Red-Orange
    public static final String WARNING_COLOR = "#FFE66D";      // Yellow
    public static final String ERROR_COLOR = "#FF6B6B";        // Light Red
    public static final String INFO_COLOR = "#4ECDC4";         // Teal
    
    // Neutral Colors
    public static final String BACKGROUND_COLOR = "#F8F9FA";   // Light Gray
    public static final String SURFACE_COLOR = "#FFFFFF";      // White
    public static final String CARD_COLOR = "#FFFFFF";         // White
    public static final String TEXT_PRIMARY = "#2C3E50";       // Dark Blue-Gray
    public static final String TEXT_SECONDARY = "#7F8C8D";     // Gray
    public static final String BORDER_COLOR = "#E9ECEF";       // Light Border
    
    // Gradient Colors
    public static final String GRADIENT_PRIMARY = "linear-gradient(135deg, #667eea 0%, #764ba2 100%)";
    public static final String GRADIENT_SECONDARY = "linear-gradient(135deg, #f093fb 0%, #f5576c 100%)";
    public static final String GRADIENT_SUCCESS = "linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)";
    public static final String GRADIENT_WARNING = "linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)";
    
    public static void applyGlobalStyles(Scene scene) {
        try {
            scene.getStylesheets().clear();
            var cssResource = StyleManager.class.getResource("/styles/modern-theme.css");
            if (cssResource != null) {
                scene.getStylesheets().add(cssResource.toExternalForm());
                System.out.println("✅ CSS styles loaded successfully");
            } else {
                System.out.println("⚠️ CSS file not found, using default styles");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error loading CSS: " + e.getMessage());
            // Continue without custom styles
        }
    }
    
    // CSS styles are loaded from external file
}
