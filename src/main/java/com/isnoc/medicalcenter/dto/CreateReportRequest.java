package com.isnoc.medicalcenter.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;

public class CreateReportRequest {
    @NotNull(message = "Report type ID cannot be null")
    private Long reportTypeId;

    @NotNull(message = "Report data cannot be null")
    private JsonNode reportData; // Actual values entered for the report
    
    // Constructors
    public CreateReportRequest() {}
    
    public CreateReportRequest(Long reportTypeId, JsonNode reportData) {
        this.reportTypeId = reportTypeId;
        this.reportData = reportData;
    }
    
    // Getters and setters
    public Long getReportTypeId() {
        return reportTypeId;
    }
    
    public void setReportTypeId(Long reportTypeId) {
        this.reportTypeId = reportTypeId;
    }
    
    public JsonNode getReportData() {
        return reportData;
    }
    
    public void setReportData(JsonNode reportData) {
        this.reportData = reportData;
    }
}