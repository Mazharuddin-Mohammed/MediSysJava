package com.medisys.desktop.model;

public class Patient {
    private Long id;
    private String name;
    private String dateOfBirth;
    private String contactInfo;

    // Constructors
    public Patient() {}

    public Patient(Long id, String name, String dateOfBirth, String contactInfo) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}