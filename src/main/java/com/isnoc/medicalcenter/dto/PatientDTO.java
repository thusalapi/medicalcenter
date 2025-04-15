package com.isnoc.medicalcenter.dto;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long patientId;
    private String phoneNumber;
    private String name;
    private JsonNode otherDetails; // Keep as JsonNode for flexibility
}