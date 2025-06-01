package com.medisys.desktop.utils;

/**
 * Free Icon Library using Unicode Characters
 * Provides consistent iconography throughout the MediSys application
 */
public class IconLibrary {
    
    // Medical Icons
    public static final String HOSPITAL = "🏥";
    public static final String DOCTOR = "👨‍⚕️";
    public static final String NURSE = "👩‍⚕️";
    public static final String PATIENT = "👤";
    public static final String PATIENTS = "👥";
    public static final String STETHOSCOPE = "🩺";
    public static final String PILL = "💊";
    public static final String SYRINGE = "💉";
    public static final String THERMOMETER = "🌡️";
    public static final String AMBULANCE = "🚑";
    public static final String MEDICAL_CROSS = "✚";
    public static final String HEART = "❤️";
    public static final String BRAIN = "🧠";
    public static final String BONE = "🦴";
    public static final String EYE = "👁️";
    public static final String TOOTH = "🦷";
    
    // Administrative Icons
    public static final String CALENDAR = "📅";
    public static final String APPOINTMENT = "📋";
    public static final String SCHEDULE = "🗓️";
    public static final String CLOCK = "⏰";
    public static final String TIME = "🕐";
    public static final String DOCUMENT = "📄";
    public static final String FOLDER = "📁";
    public static final String FILE = "📃";
    public static final String REPORT = "📊";
    public static final String CHART = "📈";
    public static final String GRAPH = "📉";
    public static final String STATISTICS = "📊";
    
    // Financial Icons
    public static final String MONEY = "💰";
    public static final String PAYMENT = "💳";
    public static final String BILL = "🧾";
    public static final String INVOICE = "📄";
    public static final String CASH = "💵";
    public static final String BANK = "🏦";
    public static final String CREDIT_CARD = "💳";
    public static final String RECEIPT = "🧾";
    
    // Communication Icons
    public static final String PHONE = "📞";
    public static final String EMAIL = "📧";
    public static final String MESSAGE = "💬";
    public static final String NOTIFICATION = "🔔";
    public static final String ALERT = "🚨";
    public static final String WARNING = "⚠️";
    public static final String INFO = "ℹ️";
    public static final String HELP = "❓";
    
    // Action Icons
    public static final String ADD = "➕";
    public static final String EDIT = "✏️";
    public static final String DELETE = "🗑️";
    public static final String SAVE = "💾";
    public static final String PRINT = "🖨️";
    public static final String EXPORT = "📤";
    public static final String IMPORT = "📥";
    public static final String SEARCH = "🔍";
    public static final String FILTER = "🔽";
    public static final String SORT = "🔀";
    public static final String REFRESH = "🔄";
    public static final String SYNC = "🔄";
    
    // Status Icons
    public static final String SUCCESS = "✅";
    public static final String ERROR = "❌";
    public static final String PENDING = "⏳";
    public static final String COMPLETED = "✔️";
    public static final String CANCELLED = "❌";
    public static final String ACTIVE = "🟢";
    public static final String INACTIVE = "🔴";
    public static final String ONLINE = "🟢";
    public static final String OFFLINE = "🔴";
    
    // Navigation Icons
    public static final String HOME = "🏠";
    public static final String DASHBOARD = "📊";
    public static final String MENU = "☰";
    public static final String BACK = "⬅️";
    public static final String FORWARD = "➡️";
    public static final String UP = "⬆️";
    public static final String DOWN = "⬇️";
    public static final String LEFT = "⬅️";
    public static final String RIGHT = "➡️";
    
    // System Icons
    public static final String SETTINGS = "⚙️";
    public static final String PREFERENCES = "🔧";
    public static final String TOOLS = "🛠️";
    public static final String BACKUP = "💾";
    public static final String RESTORE = "🔄";
    public static final String UPDATE = "🔄";
    public static final String DOWNLOAD = "⬇️";
    public static final String UPLOAD = "⬆️";
    public static final String CLOUD = "☁️";
    public static final String DATABASE = "🗄️";
    
    // Emergency Icons
    public static final String EMERGENCY = "🚨";
    public static final String FIRE = "🔥";
    public static final String SECURITY = "🔒";
    public static final String POLICE = "👮";
    public static final String FIRST_AID = "🩹";
    public static final String EMERGENCY_EXIT = "🚪";
    
    // Department Icons
    public static final String CARDIOLOGY = "❤️";
    public static final String NEUROLOGY = "🧠";
    public static final String ORTHOPEDICS = "🦴";
    public static final String PEDIATRICS = "👶";
    public static final String EMERGENCY_DEPT = "🚨";
    public static final String SURGERY = "🔪";
    public static final String RADIOLOGY = "📡";
    public static final String LABORATORY = "🧪";
    public static final String PHARMACY = "💊";
    public static final String ICU = "🏥";
    
    // User Role Icons
    public static final String ADMIN = "👑";
    public static final String MANAGER = "👔";
    public static final String STAFF = "👤";
    public static final String USER = "👤";
    public static final String GUEST = "👤";
    
    // Quality Icons (for better visual appeal)
    public static final String STAR = "⭐";
    public static final String TROPHY = "🏆";
    public static final String MEDAL = "🏅";
    public static final String CERTIFICATE = "📜";
    public static final String AWARD = "🎖️";
    
    // Utility Methods
    public static String getIconForDepartment(String department) {
        if (department == null) return HOSPITAL;
        
        return switch (department.toLowerCase()) {
            case "cardiology" -> CARDIOLOGY;
            case "neurology" -> NEUROLOGY;
            case "orthopedics" -> ORTHOPEDICS;
            case "pediatrics" -> PEDIATRICS;
            case "emergency" -> EMERGENCY_DEPT;
            case "surgery" -> SURGERY;
            case "radiology" -> RADIOLOGY;
            case "laboratory" -> LABORATORY;
            case "pharmacy" -> PHARMACY;
            case "icu" -> ICU;
            default -> HOSPITAL;
        };
    }
    
    public static String getIconForRole(String role) {
        if (role == null) return USER;
        
        return switch (role.toLowerCase()) {
            case "admin" -> ADMIN;
            case "doctor" -> DOCTOR;
            case "nurse" -> NURSE;
            case "manager" -> MANAGER;
            case "staff" -> STAFF;
            default -> USER;
        };
    }
    
    public static String getIconForStatus(String status) {
        if (status == null) return PENDING;
        
        return switch (status.toLowerCase()) {
            case "active", "completed", "success" -> SUCCESS;
            case "inactive", "cancelled", "error" -> ERROR;
            case "pending", "waiting" -> PENDING;
            case "online" -> ONLINE;
            case "offline" -> OFFLINE;
            default -> PENDING;
        };
    }
    
    public static String getIconForReportType(String reportType) {
        if (reportType == null) return REPORT;
        
        return switch (reportType.toLowerCase()) {
            case "financial", "revenue", "billing" -> MONEY;
            case "patient", "patients" -> PATIENTS;
            case "doctor", "doctors" -> DOCTOR;
            case "appointment", "appointments" -> APPOINTMENT;
            case "statistics", "stats" -> STATISTICS;
            case "chart", "graph" -> CHART;
            default -> REPORT;
        };
    }
    
    // Color schemes for icons
    public static class Colors {
        public static final String PRIMARY = "#2E86AB";
        public static final String SECONDARY = "#4ECDC4";
        public static final String SUCCESS = "#27AE60";
        public static final String WARNING = "#F39C12";
        public static final String DANGER = "#E74C3C";
        public static final String INFO = "#3498DB";
        public static final String LIGHT = "#BDC3C7";
        public static final String DARK = "#2C3E50";
        
        // Medical department colors
        public static final String CARDIOLOGY_COLOR = "#E74C3C";
        public static final String NEUROLOGY_COLOR = "#9B59B6";
        public static final String ORTHOPEDICS_COLOR = "#F39C12";
        public static final String PEDIATRICS_COLOR = "#E91E63";
        public static final String EMERGENCY_COLOR = "#E74C3C";
    }
    
    // Font sizes for consistent typography
    public static class FontSizes {
        public static final String ICON_SMALL = "16px";
        public static final String ICON_MEDIUM = "20px";
        public static final String ICON_LARGE = "24px";
        public static final String ICON_XLARGE = "32px";
        
        public static final String TEXT_SMALL = "12px";
        public static final String TEXT_MEDIUM = "14px";
        public static final String TEXT_LARGE = "16px";
        public static final String TEXT_XLARGE = "18px";
        public static final String TEXT_TITLE = "20px";
        public static final String TEXT_HEADER = "24px";
        public static final String TEXT_DISPLAY = "28px";
    }
}
