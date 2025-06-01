package com.medisys.desktop.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLog {
    private Long id;
    private Long userId;
    private String action;
    private String entityType;
    private Long entityId;
    private LocalDateTime timestamp;
}