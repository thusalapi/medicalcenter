package com.isnoc.medicalcenter.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitDTO {
    private Long visitId;
    private Long patientId; // Include patient ID for reference
    private String patientName; // Include patient name for display convenience
    private LocalDateTime visitDate;
    // Optionally include counts or summarized info about reports/bills
}