package com.isnoc.medicalcenter.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isnoc.medicalcenter.entity.Patient;
import com.isnoc.medicalcenter.entity.Report;
import com.isnoc.medicalcenter.entity.ReportType;
import com.isnoc.medicalcenter.entity.Visit;
import com.isnoc.medicalcenter.exception.ResourceNotFoundException;
import com.isnoc.medicalcenter.repository.PatientRepository;
import com.isnoc.medicalcenter.repository.ReportRepository;
import com.isnoc.medicalcenter.repository.ReportTypeRepository;
import com.isnoc.medicalcenter.repository.VisitRepository;
import com.isnoc.medicalcenter.service.ReportTemplateService;
import com.isnoc.medicalcenter.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final ReportRepository reportRepository;
    private final ReportTypeRepository reportTypeRepository;
    private final ReportTemplateService reportTemplateService;
    private final ObjectMapper objectMapper;

    @Autowired
    public VisitServiceImpl(
            VisitRepository visitRepository,
            PatientRepository patientRepository,
            ReportRepository reportRepository,
            ReportTypeRepository reportTypeRepository,
            ReportTemplateService reportTemplateService,
            ObjectMapper objectMapper) {
        this.visitRepository = visitRepository;
        this.patientRepository = patientRepository;
        this.reportRepository = reportRepository;
        this.reportTypeRepository = reportTypeRepository;
        this.reportTemplateService = reportTemplateService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public Visit createVisit(Long patientId, LocalDateTime visitDate) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        
        Visit visit = new Visit();
        visit.setPatient(patient);
        visit.setVisitDate(visitDate != null ? visitDate : LocalDateTime.now());
        
        return visitRepository.save(visit);
    }    @Override
    public Visit getVisitById(Long visitId) {
        return visitRepository.findByIdWithPatient(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));
    }

    @Override
    public List<Visit> getVisitHistoryForPatient(Long patientId) {
        // Verify patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        
        return visitRepository.findByPatientPatientIdOrderByVisitDateDesc(patientId);
    }    @Override
    public List<Visit> getRecentVisits(int limit) {
        return visitRepository.findRecentVisitsWithPatient(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "visitDate"))
        );
    }
    
    @Override
    public List<Visit> getAllVisitsWithPatients() {
        return visitRepository.findAllWithPatient();
    }

    @Override
    @Transactional
    public Report addReportToVisit(Long visitId, Long reportTypeId, JsonNode reportData) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));
                
        ReportType reportType = reportTypeRepository.findById(reportTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Report type not found with id: " + reportTypeId));
        
        // Check if this is template-based data (contains field values rather than final content)
        JsonNode finalReportData = reportData;
        
        try {
            // If reportData contains field-like keys (field_xxx or field-xxx), it's likely template data
            if (isTemplateBasedData(reportData)) {
                // Try to find a template for this report type and generate content
                String generatedContent = generateContentFromTemplate(reportType, reportData);
                if (generatedContent != null) {
                    // Store the generated content as the final report data
                    finalReportData = objectMapper.createObjectNode().put("content", generatedContent);
                }
            }
        } catch (Exception e) {
            // If template generation fails, log the error but continue with original data
            System.err.println("Failed to generate template content: " + e.getMessage());
            // Keep the original reportData
        }
        
        Report report = new Report();
        report.setVisit(visit);
        report.setReportType(reportType);
        report.setReportData(finalReportData);
        report.setCreatedDate(LocalDateTime.now());
        report.setLastModifiedDate(LocalDateTime.now());
        
        // Save the report
        Report savedReport = reportRepository.save(report);
        
        // Add to visit's report list (optional if you want to maintain the bidirectional relationship)
        visit.getReports().add(savedReport);
        
        return savedReport;
    }
    
    private boolean isTemplateBasedData(JsonNode reportData) {
        // Check if the data structure suggests it's field values rather than final content
        if (reportData == null || !reportData.isObject()) {
            return false;
        }
        
        // Look for field-like patterns
        return reportData.fieldNames().hasNext() && 
               reportData.fieldNames().next().matches("field[_-].*");
    }
    
    private String generateContentFromTemplate(ReportType reportType, JsonNode fieldValuesData) {
        try {
            // Try to find a template by name matching the report type
            List<com.isnoc.medicalcenter.entity.ReportTemplate> templates = 
                reportTemplateService.getAllActiveTemplates();
            
            for (com.isnoc.medicalcenter.entity.ReportTemplate template : templates) {
                if (template.getTemplateName().equalsIgnoreCase(reportType.getReportName()) ||
                    template.getTemplateName().toLowerCase().contains(reportType.getReportName().toLowerCase())) {
                    
                    // Convert JsonNode to Map<String, Object>
                    Map<String, Object> fieldValues = new HashMap<>();
                    fieldValuesData.fields().forEachRemaining(entry -> 
                        fieldValues.put(entry.getKey(), entry.getValue().asText())
                    );
                    
                    return reportTemplateService.generateReportFromTemplate(
                        template.getTemplateId(), fieldValues);
                }
            }
        } catch (Exception e) {
            System.err.println("Error generating content from template: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    @Transactional
    public Report createReportFromTemplate(Long visitId, Long templateId, Map<String, Object> fieldValues) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));
        
        try {
            // Add patient information to field values
            Map<String, Object> enhancedFieldValues = new HashMap<>(fieldValues);
            if (visit.getPatient() != null) {
                enhancedFieldValues.put("patientName", visit.getPatient().getName());
                enhancedFieldValues.put("patientPhone", visit.getPatient().getPhoneNumber());
                enhancedFieldValues.put("visitDate", visit.getVisitDate().toString());
                
                // Extract additional details from JSON if available
                JsonNode otherDetails = visit.getPatient().getOtherDetails();
                if (otherDetails != null) {
                    if (otherDetails.has("age")) {
                        enhancedFieldValues.put("patientAge", otherDetails.get("age").asText());
                    }
                    if (otherDetails.has("gender")) {
                        enhancedFieldValues.put("patientGender", otherDetails.get("gender").asText());
                    }
                    if (otherDetails.has("firstName")) {
                        enhancedFieldValues.put("patientFirstName", otherDetails.get("firstName").asText());
                    }
                    if (otherDetails.has("lastName")) {
                        enhancedFieldValues.put("patientLastName", otherDetails.get("lastName").asText());
                    }
                }
            }
            
            // Generate content from template with enhanced field values
            String generatedContent = reportTemplateService.generateReportFromTemplate(templateId, enhancedFieldValues);
            
            // Get template to determine report type (or create a generic one)
            com.isnoc.medicalcenter.entity.ReportTemplate template = 
                reportTemplateService.getTemplateById(templateId);
            
            // For now, we'll need to find or create a ReportType that matches the template
            // This is a simplified approach - you might want to establish a proper relationship
            ReportType reportType = findOrCreateReportTypeForTemplate(template);
            
            // Create report with generated content
            JsonNode reportData = objectMapper.createObjectNode().put("content", generatedContent);
            
            Report report = new Report();
            report.setVisit(visit);
            report.setReportType(reportType);
            report.setReportData(reportData);
            report.setCreatedDate(LocalDateTime.now());
            report.setLastModifiedDate(LocalDateTime.now());
            
            // Save the report
            Report savedReport = reportRepository.save(report);
            
            // Add to visit's report list
            visit.getReports().add(savedReport);
            
            return savedReport;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create report from template: " + e.getMessage(), e);
        }
    }
    
    private ReportType findOrCreateReportTypeForTemplate(com.isnoc.medicalcenter.entity.ReportTemplate template) {
        // Try to find existing ReportType with matching name
        List<ReportType> existingTypes = reportTypeRepository.findAll();
        for (ReportType type : existingTypes) {
            if (type.getReportName().equalsIgnoreCase(template.getTemplateName())) {
                return type;
            }
        }
        
        // If not found, create a new ReportType
        ReportType newType = new ReportType();
        newType.setReportName(template.getTemplateName());
        newType.setDescription("Auto-generated report type for template: " + template.getTemplateName());
        newType.setReportTemplate(objectMapper.createObjectNode().put("templateId", template.getTemplateId()));
        newType.setCreatedDate(LocalDateTime.now());
        newType.setLastModifiedDate(LocalDateTime.now());
        
        return reportTypeRepository.save(newType);
    }

    @Override
    public List<Report> getReportsForVisit(Long visitId) {
        if (!visitRepository.existsById(visitId)) {
            throw new ResourceNotFoundException("Visit not found with id: " + visitId);
        }
        
        return reportRepository.findByVisitVisitId(visitId);
    }
}