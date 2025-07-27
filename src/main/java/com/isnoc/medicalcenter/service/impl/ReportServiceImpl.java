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
        // Wrap in proper HTML structure if needed, but don't add any extra content
        if (!htmlContent.toLowerCase().contains("<html")) {
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
            htmlBuilder.append(htmlContent);
            htmlBuilder.append("</body>");
            htmlBuilder.append("</html>");
            return htmlBuilder.toString();
        }
        
        return htmlContent;
    }
}