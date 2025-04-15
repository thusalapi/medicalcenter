package com.isnoc.medicalcenter.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReportRequest {
    @NotNull(message = "Visit ID cannot be null")
    private Long visitId;

    @NotNull(message = "Report Type ID cannot be null")
    private Long reportTypeId;

    @NotNull(message = "Report data cannot be null")
    private JsonNode reportData;
}