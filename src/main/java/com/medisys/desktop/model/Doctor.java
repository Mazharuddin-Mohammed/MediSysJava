package com.medisys.desktop.model;

import lombok.Data;

@Data
public class Doctor {
    private Long id;
    private String name;
    private String specialty;
    private String contactInfo;
    private Long departmentId;
}