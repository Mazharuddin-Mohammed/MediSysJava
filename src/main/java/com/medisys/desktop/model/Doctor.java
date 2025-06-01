package com.medisys.desktop.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Doctor model with comprehensive professional information
 */
public class Doctor {
    private Long id;
    private String doctorId;
    private String firstName;
    private String lastName;
    private String name; // Legacy field for compatibility
    private String specialization;
    private String specialty; // Legacy field for compatibility
    private String qualification;
    private String email;
    private String phone;
    private String contactInfo; // Legacy field for compatibility
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private String licenseNumber;
    private LocalDate licenseExpiryDate;
    private String department;
    private Long departmentId; // Legacy field for compatibility
    private String designation;
    private LocalDate joiningDate;
    private Double consultationFee;
    private String profilePhotoPath;
    private String biography;
    private String languages;
    private LocalTime morningStartTime;
    private LocalTime morningEndTime;
    private LocalTime eveningStartTime;
    private LocalTime eveningEndTime;
    private String workingDays;
    private boolean active;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Long createdBy;

    public Doctor() {
        this.active = true;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public Doctor(String firstName, String lastName, String specialization, String email, String phone) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.email = email;
        this.phone = phone;
    }

    // Legacy constructor for compatibility
    public Doctor(Long id, String name, String specialty, String contactInfo, Long departmentId) {
        this();
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.contactInfo = contactInfo;
        this.departmentId = departmentId;

        // Parse name into first and last name
        if (name != null && name.contains(" ")) {
            String[] parts = name.split(" ", 2);
            this.firstName = parts[0];
            this.lastName = parts[1];
        } else {
            this.firstName = name;
        }
        this.specialization = specialty;
        this.phone = contactInfo;
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

    // Enhanced getters and setters
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() {
        return "Dr. " + (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public LocalDate getLicenseExpiryDate() { return licenseExpiryDate; }
    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) { this.licenseExpiryDate = licenseExpiryDate; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public Double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(Double consultationFee) { this.consultationFee = consultationFee; }

    public String getProfilePhotoPath() { return profilePhotoPath; }
    public void setProfilePhotoPath(String profilePhotoPath) { this.profilePhotoPath = profilePhotoPath; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    public String getLanguages() { return languages; }
    public void setLanguages(String languages) { this.languages = languages; }

    public LocalTime getMorningStartTime() { return morningStartTime; }
    public void setMorningStartTime(LocalTime morningStartTime) { this.morningStartTime = morningStartTime; }

    public LocalTime getMorningEndTime() { return morningEndTime; }
    public void setMorningEndTime(LocalTime morningEndTime) { this.morningEndTime = morningEndTime; }

    public LocalTime getEveningStartTime() { return eveningStartTime; }
    public void setEveningStartTime(LocalTime eveningStartTime) { this.eveningStartTime = eveningStartTime; }

    public LocalTime getEveningEndTime() { return eveningEndTime; }
    public void setEveningEndTime(LocalTime eveningEndTime) { this.eveningEndTime = eveningEndTime; }

    public String getWorkingDays() { return workingDays; }
    public void setWorkingDays(String workingDays) { this.workingDays = workingDays; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public int getExperience() {
        if (joiningDate != null) {
            return LocalDate.now().getYear() - joiningDate.getYear();
        }
        return 0;
    }

    public String getSchedule() {
        StringBuilder schedule = new StringBuilder();
        if (morningStartTime != null && morningEndTime != null) {
            schedule.append("Morning: ").append(morningStartTime).append(" - ").append(morningEndTime);
        }
        if (eveningStartTime != null && eveningEndTime != null) {
            if (schedule.length() > 0) schedule.append(", ");
            schedule.append("Evening: ").append(eveningStartTime).append(" - ").append(eveningEndTime);
        }
        return schedule.toString();
    }

    @Override
    public String toString() {
        return getFullName() + " (" + specialization + ")";
    }
}