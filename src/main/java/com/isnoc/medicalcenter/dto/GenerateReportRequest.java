package com.isnoc.medicalcenter.dto;

import java.util.Map;

public class GenerateReportRequest {
    
    private Map<String, Object> fieldValues;
    
    // Constructors
    public GenerateReportRequest() {}
    
    public GenerateReportRequest(Map<String, Object> fieldValues) {
        this.fieldValues = fieldValues;
    }
    
    // Getters and setters
    public Map<String, Object> getFieldValues() {
        return fieldValues;
    }
    
    public void setFieldValues(Map<String, Object> fieldValues) {
        this.fieldValues = fieldValues;
    }
}
