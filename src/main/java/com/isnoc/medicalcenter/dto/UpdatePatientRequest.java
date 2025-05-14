package com.isnoc.medicalcenter.dto;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;

public class UpdatePatientRequest {
    // Phone number is usually not updated via this request
    @NotBlank(message = "Patient name cannot be blank")
    private String name;
    private JsonNode otherDetails;
    
    // Constructors
    public UpdatePatientRequest() {}
    
    public UpdatePatientRequest(String name, JsonNode otherDetails) {
        this.name = name;
        this.otherDetails = otherDetails;
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public JsonNode getOtherDetails() {
        return otherDetails;
    }
    
    public void setOtherDetails(JsonNode otherDetails) {
        this.otherDetails = otherDetails;
    }
}