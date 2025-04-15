package com.isnoc.medicalcenter.dto;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientRequest {
    // Phone number is usually not updated via this request
    @NotBlank(message = "Patient name cannot be blank")
    private String name;
    private JsonNode otherDetails;
}