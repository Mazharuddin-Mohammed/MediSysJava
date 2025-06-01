package com.medisys.desktop.model;

public class Doctor {
    private Long id;
    private String name;
    private String specialty;
    private String contactInfo;
    private Long departmentId;

    // Constructors
    public Doctor() {}

    public Doctor(Long id, String name, String specialty, String contactInfo, Long departmentId) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.contactInfo = contactInfo;
        this.departmentId = departmentId;
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

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialty='" + specialty + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", departmentId=" + departmentId +
                '}';
    }
}