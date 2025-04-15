package com.isnoc.medicalcenter.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientRequest {

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format") // Example regex, adjust as needed
    private String phoneNumber;

    @NotBlank(message = "Patient name cannot be blank")
    private String name;

    // otherDetails can be null or an empty object initially
    private JsonNode otherDetails;
}