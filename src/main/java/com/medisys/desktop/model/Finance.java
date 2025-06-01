package com.medisys.desktop.model;

import lombok.Data;

@Data
public class Finance {
    private Long id;
    private Long patientId;
    private Double amount;
    private String status;
}