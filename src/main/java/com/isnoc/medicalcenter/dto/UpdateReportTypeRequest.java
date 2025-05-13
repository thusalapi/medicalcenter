package com.isnoc.medicalcenter.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReportTypeRequest {
    private String reportName; // Can be null if not updating
    private String description; // Can be null if not updating
    private JsonNode reportTemplate; // Can be null if not updating
}