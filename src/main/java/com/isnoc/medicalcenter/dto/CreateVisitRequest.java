package com.isnoc.medicalcenter.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVisitRequest {
    
    @NotNull(message = "Patient ID cannot be null")
    private Long patientId;
    
    private LocalDateTime visitDate; // optional, will default to now if not provided
}