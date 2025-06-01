package com.medisys.desktop.model;

import lombok.Data;

@Data
public class Patient {
    private Long id;
    private String name;
    private String dateOfBirth;
    private String contactInfo;
}