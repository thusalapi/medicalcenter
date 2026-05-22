package com.isnoc.medicalcenter.entity;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import com.isnoc.medicalcenter.util.JsonNodeConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_types")
public class ReportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportTypeId;

    @Column(unique = true, nullable = false)
    private String reportName;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode reportTemplate; // Structure defined by Admin UI
    
    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
    
    // Constructors
    public ReportType() {}
    
    public ReportType(Long reportTypeId, String reportName, String description, JsonNode reportTemplate,
                      LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.reportTypeId = reportTypeId;
        this.reportName = reportName;
        this.description = description;
        this.reportTemplate = reportTemplate;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
    
    // Getters and setters
    public Long getReportTypeId() {
        return reportTypeId;
    }
    
    public void setReportTypeId(Long reportTypeId) {
        this.reportTypeId = reportTypeId;
    }
    
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
}