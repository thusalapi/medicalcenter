package com.isnoc.medicalcenter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.entity.ReportTemplate;

import java.util.List;
import java.util.Map;

public interface ReportTemplateService {
    
    // Template CRUD operations
    ReportTemplate createTemplate(String templateName, String description, String category,
                                 JsonNode staticContent, JsonNode dynamicFields, JsonNode layoutConfig);
    
    ReportTemplate updateTemplate(Long templateId, String templateName, String description,
                                 String category, JsonNode staticContent, JsonNode dynamicFields, 
                                 JsonNode layoutConfig);
    
    ReportTemplate getTemplateById(Long templateId);
    
    List<ReportTemplate> getAllActiveTemplates();
    
    List<ReportTemplate> getTemplatesByCategory(String category);
    
    List<String> getAvailableCategories();
    
    void deactivateTemplate(Long templateId);
    
    // Template validation and processing
    boolean validateTemplateStructure(JsonNode staticContent, JsonNode dynamicFields);
    
    // Report generation from template
    String generateReportFromTemplate(Long templateId, Map<String, Object> fieldValues);
    
    byte[] generateReportPdfFromTemplate(Long templateId, Map<String, Object> fieldValues);
    
    // Field mapping utilities
    List<String> extractDynamicFieldsFromTemplate(Long templateId);
    
    Map<String, Object> getDefaultFieldValues(Long templateId);
}
