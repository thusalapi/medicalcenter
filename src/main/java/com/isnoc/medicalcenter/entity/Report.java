package com.isnoc.medicalcenter.entity;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import com.isnoc.medicalcenter.util.JsonNodeConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY) // Fetch type info when needed
    @JoinColumn(name = "report_type_id", nullable = false)
    private ReportType reportType;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode reportData; // Data entered by Receptionist

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
    
    // Constructors
    public Report() {}
    
    public Report(Long reportId, Visit visit, ReportType reportType, JsonNode reportData, 
                  LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.reportId = reportId;
        this.visit = visit;
        this.reportType = reportType;
        this.reportData = reportData;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
    
    // Getters and setters
    public Long getReportId() {
        return reportId;
    }
    
    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
    
    public Visit getVisit() {
        return visit;
    }
    
    public void setVisit(Visit visit) {
        this.visit = visit;
    }
    
    public ReportType getReportType() {
        return reportType;
    }
    
    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }
    
    public JsonNode getReportData() {
        return reportData;
    }
    
    public void setReportData(JsonNode reportData) {
        this.reportData = reportData;
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