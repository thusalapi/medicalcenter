package com.isnoc.medicalcenter.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public class CreatePatientRequest {

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format") // Example regex, adjust as needed
    private String phoneNumber;

    @NotBlank(message = "Patient name cannot be blank")
    private String name;

    // otherDetails can be null or an empty object initially
    private JsonNode otherDetails;
    
    // Constructors
    public CreatePatientRequest() {}
    
    public CreatePatientRequest(String phoneNumber, String name, JsonNode otherDetails) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.otherDetails = otherDetails;
    }
    
    // Getters and setters
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
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