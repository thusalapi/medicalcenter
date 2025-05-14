package com.isnoc.medicalcenter.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.util.JsonNodeConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @Column(unique = true, nullable = false)
    private String phoneNumber; // Lookup key

    private String name;
    // Add other fixed/searchable fields if needed

    @Column(columnDefinition = "TEXT") // Store as TEXT in SQLite
    @Convert(converter = JsonNodeConverter.class) // Use our converter
    private JsonNode otherDetails; // Flexible JSON data
    
    // Constructors
    public Patient() {}
    
    public Patient(Long patientId, String phoneNumber, String name, JsonNode otherDetails) {
        this.patientId = patientId;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.otherDetails = otherDetails;
    }
    
    // Getters and setters
    public Long getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    
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