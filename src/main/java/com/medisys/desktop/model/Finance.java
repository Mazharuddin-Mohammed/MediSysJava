package com.medisys.desktop.model;

public class Finance {
    private Long id;
    private Long patientId;
    private Double amount;
    private String status;

    // Constructors
    public Finance() {}

    public Finance(Long id, Long patientId, Double amount, String status) {
        this.id = id;
        this.patientId = patientId;
        this.amount = amount;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Finance{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}