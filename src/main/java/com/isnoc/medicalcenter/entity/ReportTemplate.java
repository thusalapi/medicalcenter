package com.isnoc.medicalcenter.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.util.JsonNodeConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_templates")
public class ReportTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;

    @Column(nullable = false)
    private String templateName;

    private String description;

    @Column(nullable = false)
    private String category; // e.g., "BLOOD_TEST", "URINE_TEST", "X_RAY", etc.

    @Column(columnDefinition = "TEXT")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode staticContent; // HTML/text content with placeholders

    @Column(columnDefinition = "TEXT")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode dynamicFields; // Field definitions and mappings

    @Column(columnDefinition = "TEXT")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode layoutConfig; // Layout configuration for drag-drop

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    private String createdBy;

    // Constructors
    public ReportTemplate() {}

    public ReportTemplate(String templateName, String description, String category,
                         JsonNode staticContent, JsonNode dynamicFields, JsonNode layoutConfig) {
        this.templateName = templateName;
        this.description = description;
        this.category = category;
        this.staticContent = staticContent;
        this.dynamicFields = dynamicFields;
        this.layoutConfig = layoutConfig;
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
