package com.medisys.desktop.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String role;
}