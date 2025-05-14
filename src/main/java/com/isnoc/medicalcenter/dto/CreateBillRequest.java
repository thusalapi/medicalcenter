package com.isnoc.medicalcenter.dto;

import jakarta.validation.constraints.NotNull;

public class CreateBillRequest {
    @NotNull(message = "Visit ID cannot be null")
    private Long visitId;
    
    // Constructors
    public CreateBillRequest() {}
    
    public CreateBillRequest(Long visitId) {
        this.visitId = visitId;
    }
    
    // Getters and setters
    public Long getVisitId() {
        return visitId;
    }
    
    public void setVisitId(Long visitId) {
        this.visitId = visitId;
    }
}