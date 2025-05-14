package com.isnoc.medicalcenter.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateVisitRequest {
    
    @NotNull(message = "Patient ID cannot be null")
    private Long patientId;
    
    private LocalDateTime visitDate; // optional, will default to now if not provided
    
    // Constructors
    public CreateVisitRequest() {}
    
    public CreateVisitRequest(Long patientId, LocalDateTime visitDate) {
        this.patientId = patientId;
        this.visitDate = visitDate;
    }
    
    // Getters and setters
    public Long getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    
    public LocalDateTime getVisitDate() {
        return visitDate;
    }
    
    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }
}