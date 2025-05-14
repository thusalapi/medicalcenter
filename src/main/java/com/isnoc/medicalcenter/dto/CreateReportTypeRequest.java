package com.isnoc.medicalcenter.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateReportTypeRequest {

    @NotBlank(message = "Report name cannot be blank")
    private String reportName;
    
    private String description;

    @NotNull(message = "Report template cannot be null")
    private JsonNode reportTemplate; // This will contain the template structure including field positions
    
    // Constructors
    public CreateReportTypeRequest() {}
    
    public CreateReportTypeRequest(String reportName, String description, JsonNode reportTemplate) {
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