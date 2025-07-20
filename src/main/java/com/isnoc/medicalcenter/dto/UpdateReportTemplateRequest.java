package com.isnoc.medicalcenter.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class UpdateReportTemplateRequest {
    
    private String templateName;
    private String description;
    private String category;
    private JsonNode staticContent;
    private JsonNode dynamicFields;
    private JsonNode layoutConfig;
    
    // Constructors
    public UpdateReportTemplateRequest() {}
    
    public UpdateReportTemplateRequest(String templateName, String description, String category,
                                     JsonNode staticContent, JsonNode dynamicFields, JsonNode layoutConfig) {
        this.templateName = templateName;
        this.description = description;
        this.category = category;
        this.staticContent = staticContent;
        this.dynamicFields = dynamicFields;
        this.layoutConfig = layoutConfig;
    }
    
    // Getters and setters
    public String getTemplateName() {
        return templateName;
    }
    
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public JsonNode getStaticContent() {
        return staticContent;
    }
    
    public void setStaticContent(JsonNode staticContent) {
        this.staticContent = staticContent;
    }
    
    public JsonNode getDynamicFields() {
        return dynamicFields;
    }
    
    public void setDynamicFields(JsonNode dynamicFields) {
        this.dynamicFields = dynamicFields;
    }
    
    public JsonNode getLayoutConfig() {
        return layoutConfig;
    }
    
    public void setLayoutConfig(JsonNode layoutConfig) {
        this.layoutConfig = layoutConfig;
    }
}
