package com.isnoc.medicalcenter.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class UpdateReportTypeRequest {
    private String reportName; // Can be null if not updating
    private String description; // Can be null if not updating
    private JsonNode reportTemplate; // Can be null if not updating
    
    // Constructors
    public UpdateReportTypeRequest() {}
    
    public UpdateReportTypeRequest(String reportName, String description, JsonNode reportTemplate) {
        this.reportName = reportName;
        this.description = description;
        this.reportTemplate = reportTemplate;
    }
    
    // Getters and setters
    public String getReportName() {
        return reportName;
    }
    
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public JsonNode getReportTemplate() {
        return reportTemplate;
    }
    
    public void setReportTemplate(JsonNode reportTemplate) {
        this.reportTemplate = reportTemplate;
    }
}