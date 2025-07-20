package com.isnoc.medicalcenter.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public class ReportTemplateDTO {
    
    private Long templateId;
    private String templateName;
    private String description;
    private String category;
    private JsonNode staticContent;
    private JsonNode dynamicFields;
    private JsonNode layoutConfig;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String createdBy;
    
    // Constructors
    public ReportTemplateDTO() {}
    
    public ReportTemplateDTO(Long templateId, String templateName, String description, String category,
                           JsonNode staticContent, JsonNode dynamicFields, JsonNode layoutConfig,
                           Boolean isActive, LocalDateTime createdDate, LocalDateTime lastModifiedDate, String createdBy) {
        this.templateId = templateId;
        this.templateName = templateName;
        this.description = description;
        this.category = category;
        this.staticContent = staticContent;
        this.dynamicFields = dynamicFields;
        this.layoutConfig = layoutConfig;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.createdBy = createdBy;
    }
    
    // Getters and setters
    public Long getTemplateId() {
        return templateId;
    }
    
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
    
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
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
