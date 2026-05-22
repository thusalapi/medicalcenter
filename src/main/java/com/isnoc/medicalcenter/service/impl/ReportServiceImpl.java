package com.isnoc.medicalcenter.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isnoc.medicalcenter.entity.Report;
import com.isnoc.medicalcenter.exception.ResourceNotFoundException;
import com.isnoc.medicalcenter.repository.ReportRepository;
import com.isnoc.medicalcenter.service.ReportService;
import com.isnoc.medicalcenter.service.ReportTemplateService;
import com.isnoc.medicalcenter.util.PdfGenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PdfGenerationUtil pdfGenerationUtil;
    private final ReportTemplateService reportTemplateService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, 
                           PdfGenerationUtil pdfGenerationUtil,
                           ReportTemplateService reportTemplateService,
                           ObjectMapper objectMapper) {
        this.reportRepository = reportRepository;
        this.pdfGenerationUtil = pdfGenerationUtil;
        this.reportTemplateService = reportTemplateService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Report getReportById(Long reportId) {
        Report report = reportRepository.findByIdWithRelations(reportId);
        if (report == null) {
            throw new ResourceNotFoundException("Report not found with id: " + reportId);
        }
        return report;
    }

    @Override
    @Transactional
    public Report updateReport(Long reportId, JsonNode reportData) {
        Report report = getReportById(reportId);
        report.setReportData(reportData);
        report.setLastModifiedDate(LocalDateTime.now());
        return reportRepository.save(report);
    }

    @Override
    @Transactional
    public void deleteReport(Long reportId) {
        Report report = getReportById(reportId);
        reportRepository.delete(report);
    }

    @Override
    public List<Report> getReportsForPatient(Long patientId) {
        return reportRepository.findByPatientId(patientId);
    }

    @Override
    public byte[] generateReportPdf(Long reportId) throws IOException {
        Report report = getReportById(reportId);
        
        // Check if this report was created from a template
        if (isTemplateBasedReport(report)) {
            return generateTemplateBasedReportPdf(report);
        } else {
            // Use the legacy PDF generation for non-template reports
            return pdfGenerationUtil.generateReportPdf(report);
        }
    }
    
    /**
     * Check if a report was created from a template by examining its structure
     */
    private boolean isTemplateBasedReport(Report report) {
        JsonNode reportData = report.getReportData();
        
        // Template-based reports have a "content" field with HTML content
        return reportData != null && 
               reportData.isObject() && 
               reportData.has("content") &&
               reportData.get("content").isTextual();
    }
    
    /**
     * Generate PDF for template-based reports using the HTML content
     */
    private byte[] generateTemplateBasedReportPdf(Report report) {
        try {
            JsonNode reportData = report.getReportData();
            String htmlContent = reportData.get("content").asText();
            
            // Use OpenHTMLToPDF for template-based reports to preserve styling
            return generatePdfFromHtml(htmlContent, report);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF from template-based report: " + e.getMessage(), e);
        }
    }
    
    /**
     * Generate PDF from HTML content using OpenHTMLToPDF
     */
    private byte[] generatePdfFromHtml(String htmlContent, Report report) {
        try {
            // Ensure the HTML is properly formatted for PDF generation
            String formattedHtml = ensureValidHtmlForPdf(htmlContent, report);
            
            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            
            com.openhtmltopdf.pdfboxout.PdfRendererBuilder builder = new com.openhtmltopdf.pdfboxout.PdfRendererBuilder();
            builder.withHtmlContent(formattedHtml, null);
            builder.toStream(outputStream);
            builder.run();
            
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF from HTML: " + e.getMessage(), e);
        }
    }
    
    /**
     * Ensures the HTML content is valid and properly formatted for PDF generation
     * This preserves the exact template design without adding extra content
     */
    private String ensureValidHtmlForPdf(String htmlContent, Report report) {
        // Get field labels to replace field IDs with human-readable names
        Map<String, String> fieldLabels = getFieldLabelsForReport(report.getReportId());
        
        // Replace field IDs with human-readable labels in the HTML content
        String processedHtml = htmlContent;
        for (Map.Entry<String, String> entry : fieldLabels.entrySet()) {
            String fieldId = entry.getKey();
            String fieldLabel = entry.getValue();
            
            // Replace field ID with label in the HTML content
            // This handles cases where field IDs appear in the HTML
            processedHtml = processedHtml.replaceAll("\\b" + fieldId + "\\b", fieldLabel);
        }
        
        // Also replace any remaining field patterns that might not have been caught
        JsonNode reportData = report.getReportData();
        if (reportData != null && reportData.isObject()) {
            final String[] htmlRef = {processedHtml}; // Make it effectively final for lambda
            reportData.fields().forEachRemaining(entry -> {
                String fieldKey = entry.getKey();
                JsonNode fieldValue = entry.getValue();
                
                // Get the human-readable label for this field
                String displayName = fieldLabels.getOrDefault(fieldKey, 
                    // Convert camelCase or field_id to readable format as fallback
                    fieldKey.replaceAll("([a-z])([A-Z])", "$1 $2")
                           .replaceAll("_", " ")
                           .replaceAll("field\\s+\\d+", "Field")
                           .trim()
                );
                
                // Capitalize first letter
                if (!displayName.isEmpty()) {
                    displayName = displayName.substring(0, 1).toUpperCase() + 
                                 (displayName.length() > 1 ? displayName.substring(1) : "");
                }
                
                // Replace field key with display name in HTML
                htmlRef[0] = htmlRef[0].replaceAll("\\b" + fieldKey + "\\b", displayName);
            });
            processedHtml = htmlRef[0]; // Get the final result
        }
        
        // Wrap in proper HTML structure if needed, but don't add any extra content
        if (!processedHtml.toLowerCase().contains("<html")) {
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<!DOCTYPE html>");
            htmlBuilder.append("<html>");
            htmlBuilder.append("<head>");
            htmlBuilder.append("<meta charset='UTF-8'/>");
            htmlBuilder.append("<style>");
            // Only add minimal CSS to ensure proper rendering
            htmlBuilder.append("* { box-sizing: border-box; }");
            htmlBuilder.append("body { margin: 0; padding: 0; font-family: Arial, sans-serif; }");
            htmlBuilder.append("div { position: relative; }");
            htmlBuilder.append("</style>");
            htmlBuilder.append("</head>");
            htmlBuilder.append("<body>");
            // Only include the template content, no extra headers or patient info
            htmlBuilder.append(processedHtml);
            htmlBuilder.append("</body>");
            htmlBuilder.append("</html>");
            return htmlBuilder.toString();
        }
        
        return processedHtml;
    }
    
    /**
     * Get field labels mapped from field IDs using the report's template
     * This allows displaying human-readable field names instead of generated IDs
     */
    public Map<String, String> getFieldLabelsForReport(Long reportId) {
        Report report = getReportById(reportId);
        Map<String, String> fieldLabels = new HashMap<>();
        
        // Get the template from the report type
        if (report.getReportType() != null) {
            try {
                JsonNode reportTemplate = report.getReportType().getReportTemplate();
                
                if (reportTemplate != null && reportTemplate.has("fields")) {
                    JsonNode fields = reportTemplate.get("fields");
                    
                    // Create mapping from fieldName/id to label
                    for (JsonNode field : fields) {
                        if (field.has("fieldName") && field.has("label")) {
                            String fieldName = field.get("fieldName").asText();
                            String label = field.get("label").asText();
                            fieldLabels.put(fieldName, label);
                        }
                        
                        // Also map by id if it exists (for cases like field_1748159180778)
                        if (field.has("id") && field.has("label")) {
                            String fieldId = field.get("id").asText();
                            String label = field.get("label").asText();
                            fieldLabels.put(fieldId, label);
                        }
                    }
                }
                
                // Also check if there's a dynamicFields structure (for newer template format)
                if (reportTemplate != null && reportTemplate.has("dynamicFields")) {
                    JsonNode dynamicFields = reportTemplate.get("dynamicFields");
                    if (dynamicFields.has("fields")) {
                        JsonNode fields = dynamicFields.get("fields");
                        
                        for (JsonNode field : fields) {
                            if (field.has("fieldName") && field.has("label")) {
                                String fieldName = field.get("fieldName").asText();
                                String label = field.get("label").asText();
                                fieldLabels.put(fieldName, label);
                            }
                            
                            if (field.has("id") && field.has("label")) {
                                String fieldId = field.get("id").asText();
                                String label = field.get("label").asText();
                                fieldLabels.put(fieldId, label);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error getting field labels for report " + reportId + ": " + e.getMessage());
            }
        }
        
        return fieldLabels;
    }
    
    /**
     * Get report data with human-readable field names instead of field IDs
     */
    public Map<String, Object> getReportDataWithLabels(Long reportId) {
        Report report = getReportById(reportId);
        Map<String, Object> reportDataWithLabels = new HashMap<>();
        Map<String, String> fieldLabels = getFieldLabelsForReport(reportId);
        
        // Debug logging
        System.out.println("Report ID: " + reportId);
        System.out.println("Field labels found: " + fieldLabels);
        
        JsonNode reportData = report.getReportData();
        if (reportData != null && reportData.isObject()) {
            reportData.fields().forEachRemaining(entry -> {
                String fieldKey = entry.getKey();
                JsonNode fieldValue = entry.getValue();
                
                System.out.println("Processing field: " + fieldKey + " = " + fieldValue);
                
                // Get the human-readable label for this field, or use the field key if no label found
                String displayName = fieldLabels.getOrDefault(fieldKey, 
                    // Convert camelCase or field_id to readable format as fallback
                    fieldKey.replaceAll("([a-z])([A-Z])", "$1 $2")
                           .replaceAll("_", " ")
                           .replaceAll("field\\s+\\d+", "Field")
                           .trim()
                );
                
                // Capitalize first letter
                if (!displayName.isEmpty()) {
                    displayName = displayName.substring(0, 1).toUpperCase() + 
                                 (displayName.length() > 1 ? displayName.substring(1) : "");
                }
                
                // Convert JsonNode to appropriate Java object
                Object value;
                if (fieldValue.isTextual()) {
                    value = fieldValue.asText();
                } else if (fieldValue.isNumber()) {
                    value = fieldValue.asDouble();
                } else if (fieldValue.isBoolean()) {
                    value = fieldValue.asBoolean();
                } else {
                    value = fieldValue.toString();
                }
                
                System.out.println("Mapping: " + fieldKey + " -> " + displayName + " = " + value);
                reportDataWithLabels.put(displayName, value);
            });
        }
        
        System.out.println("Final report data with labels: " + reportDataWithLabels);
        return reportDataWithLabels;
    }
    
    /**
     * Generate PDF with human-readable field names
     * This method creates a new formatted PDF using the field labels instead of IDs
     */
    public byte[] generateReportPdfWithLabels(Long reportId) throws IOException {
        Report report = getReportById(reportId);
        Map<String, Object> reportDataWithLabels = getReportDataWithLabels(reportId);
        
        // Create a formatted HTML content with proper field names
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<!DOCTYPE html>");
        htmlBuilder.append("<html>");
        htmlBuilder.append("<head>");
        htmlBuilder.append("<meta charset='UTF-8'/>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        htmlBuilder.append(".header { text-align: center; margin-bottom: 30px; }");
        htmlBuilder.append(".report-title { font-size: 24px; font-weight: bold; margin-bottom: 10px; }");
        htmlBuilder.append(".patient-info { margin-bottom: 20px; }");
        htmlBuilder.append(".field { margin-bottom: 15px; border-bottom: 1px solid #eee; padding-bottom: 10px; }");
        htmlBuilder.append(".field-label { font-weight: bold; color: #333; }");
        htmlBuilder.append(".field-value { margin-top: 5px; color: #666; }");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head>");
        htmlBuilder.append("<body>");
        
        // Header
        htmlBuilder.append("<div class='header'>");
        htmlBuilder.append("<div class='report-title'>Medical Report</div>");
        if (report.getReportType() != null) {
            htmlBuilder.append("<div>").append(report.getReportType().getReportName()).append("</div>");
        }
        htmlBuilder.append("</div>");
        
        // Patient information
        if (report.getVisit() != null && report.getVisit().getPatient() != null) {
            htmlBuilder.append("<div class='patient-info'>");
            htmlBuilder.append("<div><strong>Patient:</strong> ").append(report.getVisit().getPatient().getName()).append("</div>");
            htmlBuilder.append("<div><strong>Patient ID:</strong> ").append(report.getVisit().getPatient().getPatientId()).append("</div>");
            htmlBuilder.append("<div><strong>Phone:</strong> ").append(report.getVisit().getPatient().getPhoneNumber()).append("</div>");
            if (report.getCreatedDate() != null) {
                htmlBuilder.append("<div><strong>Date:</strong> ").append(report.getCreatedDate().toString()).append("</div>");
            }
            htmlBuilder.append("</div>");
        }
        
        // Report details with human-readable field names
        htmlBuilder.append("<div>");
        htmlBuilder.append("<h3>Report Details</h3>");
        
        for (Map.Entry<String, Object> entry : reportDataWithLabels.entrySet()) {
            String fieldLabel = entry.getKey();
            Object value = entry.getValue();
            
            htmlBuilder.append("<div class='field'>");
            htmlBuilder.append("<div class='field-label'>").append(fieldLabel).append(":</div>");
            htmlBuilder.append("<div class='field-value'>");
            if (value != null && !value.toString().trim().isEmpty()) {
                htmlBuilder.append(value.toString());
            } else {
                htmlBuilder.append("<em>Not specified</em>");
            }
            htmlBuilder.append("</div>");
            htmlBuilder.append("</div>");
        }
        
        htmlBuilder.append("</div>");
        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");
        
        // Generate PDF from the formatted HTML
        return generatePdfFromHtml(htmlBuilder.toString(), report);
    }
}