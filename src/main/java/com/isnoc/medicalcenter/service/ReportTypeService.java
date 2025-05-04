package com.isnoc.medicalcenter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.entity.ReportType;

import java.util.List;
import java.util.Optional;

public interface ReportTypeService {
    /**
     * Get all report types
     * 
     * @return List of all ReportType entities
     */
    List<ReportType> getAllReportTypes();

    /**
     * Get a report type by its ID
     * 
     * @param reportTypeId ID of the report type to retrieve
     * @return the ReportType entity
     */
    ReportType getReportTypeById(Long reportTypeId);

    /**
     * Find a report type by its name
     * 
     * @param reportName Name of the report type to find
     * @return Optional containing the ReportType entity if found
     */
    Optional<ReportType> findByReportName(String reportName);

    /**
     * Create a new report type template
     * 
     * @param reportName Name of the report type
     * @param description Description of what the report is for
     * @param reportTemplate JSON template defining the structure and fields
     * @return the created ReportType entity
     */
    ReportType createReportType(String reportName, String description, JsonNode reportTemplate);

    /**
     * Update an existing report type
     * 
     * @param reportTypeId ID of the report type to update
     * @param reportName Updated report name
     * @param description Updated description
     * @param reportTemplate Updated JSON template
     * @return the updated ReportType entity
     */
    ReportType updateReportType(Long reportTypeId, String reportName, String description, JsonNode reportTemplate);

    /**
     * Delete a report type
     * 
     * @param reportTypeId ID of the report type to delete
     */
    void deleteReportType(Long reportTypeId);
}