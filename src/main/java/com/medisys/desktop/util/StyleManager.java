package com.medisys.desktop.util;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.Region;

/**
 * Enhanced Style Manager for MediSys Application
 * Provides comprehensive styling utilities and design system constants
 */
public class StyleManager {

    // ===== DESIGN SYSTEM CONSTANTS =====

    // Primary Color Palette
    public static final String PRIMARY_COLOR = "#2E86AB";
    public static final String PRIMARY_LIGHT = "#4A9BC7";
    public static final String PRIMARY_DARK = "#1F5F85";

    // Secondary Colors
    public static final String SECONDARY_COLOR = "#A23B72";
    public static final String SECONDARY_LIGHT = "#B85A8A";
    public static final String SECONDARY_DARK = "#8F3366";

    // Accent Colors
    public static final String ACCENT_COLOR = "#F18F01";
    public static final String ACCENT_LIGHT = "#F4A533";
    public static final String ACCENT_DARK = "#D97D01";

    // Status Colors
    public static final String SUCCESS_COLOR = "#4ECDC4";
    public static final String SUCCESS_LIGHT = "#6DD5CD";
    public static final String SUCCESS_DARK = "#45B7B8";

    public static final String WARNING_COLOR = "#FFE66D";
    public static final String WARNING_LIGHT = "#FFF085";
    public static final String WARNING_DARK = "#E6CF62";

    public static final String ERROR_COLOR = "#FF6B6B";
    public static final String ERROR_LIGHT = "#FF8E8E";
    public static final String ERROR_DARK = "#FF5252";

    public static final String INFO_COLOR = "#74B9FF";
    public static final String INFO_LIGHT = "#A8CCFF";
    public static final String INFO_DARK = "#5A9FFF";

    // Neutral Colors
    public static final String BACKGROUND_COLOR = "#F8F9FA";
    public static final String SURFACE_COLOR = "#FFFFFF";
    public static final String CARD_COLOR = "#FFFFFF";
    public static final String BORDER_COLOR = "#E9ECEF";
    public static final String BORDER_LIGHT = "#F1F3F4";
    public static final String BORDER_DARK = "#DEE2E6";

    // Text Colors
    public static final String TEXT_PRIMARY = "#2C3E50";
    public static final String TEXT_SECONDARY = "#7F8C8D";
    public static final String TEXT_MUTED = "#95A5A6";
    public static final String TEXT_LIGHT = "#BDC3C7";
    public static final String TEXT_WHITE = "#FFFFFF";

    // Gradient Colors
    public static final String GRADIENT_PRIMARY = "linear-gradient(135deg, #667eea 0%, #764ba2 100%)";
    public static final String GRADIENT_SECONDARY = "linear-gradient(135deg, #f093fb 0%, #f5576c 100%)";
    public static final String GRADIENT_SUCCESS = "linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)";
    public static final String GRADIENT_WARNING = "linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)";
    public static final String GRADIENT_ACCENT = "linear-gradient(135deg, #fa709a 0%, #fee140 100%)";

    // ===== SPACING CONSTANTS =====
    public static final String SPACING_XS = "4px";
    public static final String SPACING_SM = "8px";
    public static final String SPACING_MD = "16px";
    public static final String SPACING_LG = "24px";
    public static final String SPACING_XL = "32px";
    public static final String SPACING_XXL = "48px";

    // ===== BORDER RADIUS CONSTANTS =====
    public static final String RADIUS_SM = "4px";
    public static final String RADIUS_MD = "8px";
    public static final String RADIUS_LG = "12px";
    public static final String RADIUS_XL = "16px";
    public static final String RADIUS_ROUND = "50%";

    // ===== SHADOW CONSTANTS =====
    public static final String SHADOW_SM = "0 1px 3px rgba(0,0,0,0.1)";
    public static final String SHADOW_MD = "0 4px 6px rgba(0,0,0,0.1)";
    public static final String SHADOW_LG = "0 10px 15px rgba(0,0,0,0.1)";
    public static final String SHADOW_XL = "0 20px 25px rgba(0,0,0,0.15)";

    // ===== FONT SIZE CONSTANTS =====
    public static final String FONT_SIZE_XS = "11px";
    public static final String FONT_SIZE_SM = "12px";
    public static final String FONT_SIZE_BASE = "14px";
    public static final String FONT_SIZE_MD = "16px";
    public static final String FONT_SIZE_LG = "18px";
    public static final String FONT_SIZE_XL = "20px";
    public static final String FONT_SIZE_XXL = "24px";
    public static final String FONT_SIZE_DISPLAY = "28px";

    // ===== CSS CLASS CONSTANTS =====
    public static final String BTN_PRIMARY = "btn-primary";
    public static final String BTN_SECONDARY = "btn-secondary";
    public static final String BTN_SUCCESS = "btn-success";
    public static final String BTN_WARNING = "btn-warning";
    public static final String BTN_DANGER = "btn-danger";
    public static final String BTN_INFO = "btn-info";

    public static final String BTN_SM = "btn-sm";
    public static final String BTN_MD = "btn-md";
    public static final String BTN_LG = "btn-lg";

    public static final String MODERN_BUTTON = "modern-button";
    public static final String MODERN_CARD = "modern-card";
    public static final String MODERN_TEXT_FIELD = "modern-text-field";
    public static final String MODERN_TABLE_VIEW = "modern-table-view";

    public static final String NAV_BUTTON = "nav-button";
    public static final String NAV_BUTTON_ACTIVE = "nav-button-active";

    public static final String FORM_LABEL = "form-label";
    public static final String FORM_SECTION = "form-section";
    public static final String FORM_CONTAINER = "form-container";

    public static final String STATUS_ACTIVE = "status-active";
    public static final String STATUS_INACTIVE = "status-inactive";
    public static final String STATUS_PENDING = "status-pending";

    public static final String HEADER_TITLE = "header-title";
    public static final String HEADER_SUBTITLE = "header-subtitle";
    public static final String SECTION_TITLE = "section-title";

    // ===== UTILITY METHODS =====

    /**
     * Apply global styles to a scene
     */
    public static void applyGlobalStyles(Scene scene) {
        try {
            scene.getStylesheets().clear();
            var cssResource = StyleManager.class.getResource("/styles/modern-theme.css");
            if (cssResource != null) {
                scene.getStylesheets().add(cssResource.toExternalForm());
                System.out.println("✅ Modern CSS theme loaded successfully");
            } else {
                System.out.println("⚠️ CSS file not found, using default styles");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error loading CSS: " + e.getMessage());
            // Continue without custom styles
        }
    }

    /**
     * Apply a CSS class to a control
     */
    public static void applyStyle(Control control, String... styleClasses) {
        if (control != null && styleClasses != null) {
            control.getStyleClass().addAll(styleClasses);
        }
    }

    /**
     * Apply a CSS class to a region
     */
    public static void applyStyle(Region region, String... styleClasses) {
        if (region != null && styleClasses != null) {
            region.getStyleClass().addAll(styleClasses);
        }
    }

    /**
     * Create a styled button with consistent appearance
     */
    public static Button createStyledButton(String text, String... styleClasses) {
        Button button = new Button(text);
        button.getStyleClass().addAll(styleClasses);
        return button;
    }



    /**
     * Create a primary button
     */
    public static Button createPrimaryButton(String text) {
        return createStyledButton(text, MODERN_BUTTON, BTN_PRIMARY);
    }

    /**
     * Create a secondary button
     */
    public static Button createSecondaryButton(String text) {
        return createStyledButton(text, BTN_SECONDARY);
    }

    /**
     * Create a success button
     */
    public static Button createSuccessButton(String text) {
        return createStyledButton(text, BTN_SUCCESS);
    }

    /**
     * Create a warning button
     */
    public static Button createWarningButton(String text) {
        return createStyledButton(text, BTN_WARNING);
    }

    /**
     * Create a danger button
     */
    public static Button createDangerButton(String text) {
        return createStyledButton(text, BTN_DANGER);
    }

    /**
     * Create an info button
     */
    public static Button createInfoButton(String text) {
        return createStyledButton(text, BTN_INFO);
    }

    /**
     * Apply consistent button sizing
     */
    public static void applyButtonSize(Button button, ButtonSize size) {
        switch (size) {
            case SMALL -> button.getStyleClass().add(BTN_SM);
            case MEDIUM -> button.getStyleClass().add(BTN_MD);
            case LARGE -> button.getStyleClass().add(BTN_LG);
        }
    }

    /**
     * Button size enumeration
     */
    public enum ButtonSize {
        SMALL, MEDIUM, LARGE
    }

    /**
     * Get color for status
     */
    public static String getStatusColor(String status) {
        if (status == null) return TEXT_SECONDARY;

        return switch (status.toLowerCase()) {
            case "active", "success", "completed" -> SUCCESS_COLOR;
            case "inactive", "error", "failed" -> ERROR_COLOR;
            case "pending", "warning" -> WARNING_COLOR;
            case "info" -> INFO_COLOR;
            default -> TEXT_SECONDARY;
        };
    }

    /**
     * Get CSS class for status
     */
    public static String getStatusClass(String status) {
        if (status == null) return STATUS_PENDING;

        return switch (status.toLowerCase()) {
            case "active", "success", "completed" -> STATUS_ACTIVE;
            case "inactive", "error", "failed" -> STATUS_INACTIVE;
            case "pending", "warning" -> STATUS_PENDING;
            default -> STATUS_PENDING;
        };
    }
}
