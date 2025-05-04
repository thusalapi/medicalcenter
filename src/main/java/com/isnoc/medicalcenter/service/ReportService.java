package com.isnoc.medicalcenter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.entity.Report;

import java.io.IOException;
import java.util.List;

public interface ReportService {
    /**
     * Get a report by its ID
     * 
     * @param reportId ID of the report to retrieve
     * @return the Report entity
     */
    Report getReportById(Long reportId);
    
    /**
     * Update an existing report
     * 
     * @param reportId ID of the report to update
     * @param reportData Updated JSON data for the report
     * @return the updated Report entity
     */
    Report updateReport(Long reportId, JsonNode reportData);
    
    /**
     * Delete a report
     * 
     * @param reportId ID of the report to delete
     */
    void deleteReport(Long reportId);
    
    /**
     * Get reports for a specific patient
     * 
     * @param patientId ID of the patient
     * @return List of reports for the patient
     */
    List<Report> getReportsForPatient(Long patientId);
    
    /**
     * Generate PDF for a report
     * 
     * @param reportId ID of the report to generate PDF for
     * @return PDF as byte array
     * @throws IOException if there's an error generating the PDF
     */
    byte[] generateReportPdf(Long reportId) throws IOException;
}