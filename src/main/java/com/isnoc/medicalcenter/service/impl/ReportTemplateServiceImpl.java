package com.isnoc.medicalcenter.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.isnoc.medicalcenter.entity.ReportTemplate;
import com.isnoc.medicalcenter.exception.ResourceNotFoundException;
import com.isnoc.medicalcenter.repository.ReportTemplateRepository;
import com.isnoc.medicalcenter.service.ReportTemplateService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReportTemplateServiceImpl implements ReportTemplateService {

    private final ReportTemplateRepository templateRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ReportTemplateServiceImpl(ReportTemplateRepository templateRepository, ObjectMapper objectMapper) {
        this.templateRepository = templateRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public ReportTemplate createTemplate(String templateName, String description, String category,
                                        JsonNode staticContent, JsonNode dynamicFields, JsonNode layoutConfig) {
        
        // Validate template structure
        if (!validateTemplateStructure(staticContent, dynamicFields)) {
            throw new IllegalArgumentException("Invalid template structure");
        }

        // Check if template name already exists
        if (templateRepository.findByTemplateNameAndIsActiveTrue(templateName).isPresent()) {
            throw new IllegalArgumentException("Template with name '" + templateName + "' already exists");
        }

        ReportTemplate template = new ReportTemplate();
        template.setTemplateName(templateName);
        template.setDescription(description);
        template.setCategory(category.toUpperCase());
        template.setStaticContent(staticContent);
        template.setDynamicFields(dynamicFields);
        template.setLayoutConfig(layoutConfig);
        template.setCreatedDate(LocalDateTime.now());
        template.setLastModifiedDate(LocalDateTime.now());

        return templateRepository.save(template);
    }

    @Override
    @Transactional
    public ReportTemplate updateTemplate(Long templateId, String templateName, String description,
                                        String category, JsonNode staticContent, JsonNode dynamicFields, 
                                        JsonNode layoutConfig) {
        
        ReportTemplate template = getTemplateById(templateId);

        // Validate template structure
        if (!validateTemplateStructure(staticContent, dynamicFields)) {
            throw new IllegalArgumentException("Invalid template structure");
        }

        // Check if new template name conflicts with existing ones (excluding current template)
        Optional<ReportTemplate> existingTemplate = templateRepository.findByTemplateNameAndIsActiveTrue(templateName);
        if (existingTemplate.isPresent() && !existingTemplate.get().getTemplateId().equals(templateId)) {
            throw new IllegalArgumentException("Template with name '" + templateName + "' already exists");
        }

        template.setTemplateName(templateName);
        template.setDescription(description);
        template.setCategory(category.toUpperCase());
        template.setStaticContent(staticContent);
        template.setDynamicFields(dynamicFields);
        template.setLayoutConfig(layoutConfig);
        template.setLastModifiedDate(LocalDateTime.now());

        return templateRepository.save(template);
    }

    @Override
    public ReportTemplate getTemplateById(Long templateId) {
        return templateRepository.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Report template not found with id: " + templateId));
    }

    @Override
    public List<ReportTemplate> getAllActiveTemplates() {
        return templateRepository.findByIsActiveTrue();
    }

    @Override
    public List<ReportTemplate> getTemplatesByCategory(String category) {
        return templateRepository.findByCategoryAndIsActiveTrue(category.toUpperCase());
    }

    @Override
    public List<String> getAvailableCategories() {
        return templateRepository.findDistinctCategories();
    }

    @Override
    @Transactional
    public void deactivateTemplate(Long templateId) {
        ReportTemplate template = getTemplateById(templateId);
        template.setIsActive(false);
        template.setLastModifiedDate(LocalDateTime.now());
        templateRepository.save(template);
    }

    @Override
    public boolean validateTemplateStructure(JsonNode staticContent, JsonNode dynamicFields) {
        try {
            // Validate static content structure
            if (staticContent == null || !staticContent.has("content")) {
                return false;
            }

            // Validate dynamic fields structure
            if (dynamicFields != null && dynamicFields.has("fields")) {
                JsonNode fields = dynamicFields.get("fields");
                if (fields.isArray()) {
                    for (JsonNode field : fields) {
                        if (!field.has("fieldName") || !field.has("fieldType")) {
                            return false;
                        }
                    }
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generateReportFromTemplate(Long templateId, Map<String, Object> fieldValues) {
        ReportTemplate template = getTemplateById(templateId);
        
        // Get static content
        String reportContent = template.getStaticContent().get("content").asText();
        
        // Log the original content and field values for debugging
        System.out.println("Original template content: " + reportContent);
        System.out.println("Field values provided: " + fieldValues);
        
        // Replace dynamic field placeholders with actual values
        JsonNode dynamicFields = template.getDynamicFields();
        if (dynamicFields != null && dynamicFields.has("fields")) {
            JsonNode fields = dynamicFields.get("fields");
            
            for (JsonNode field : fields) {
                String fieldName = field.get("fieldName").asText();
                String placeholder = "{{" + fieldName + "}}";
                
                Object value = fieldValues.get(fieldName);
                String displayValue = value != null ? value.toString() : "";
                
                System.out.println("Replacing placeholder: " + placeholder + " with value: " + displayValue);
                reportContent = reportContent.replace(placeholder, displayValue);
            }
        }
        
        // Also handle standard patient fields that might be in the template
        String[] standardFields = {"patientName", "patientAge", "patientGender", "patientPhone", "visitDate"};
        for (String fieldName : standardFields) {
            String placeholder = "{{" + fieldName + "}}";
            Object value = fieldValues.get(fieldName);
            String displayValue = value != null ? value.toString() : "";
            
            if (reportContent.contains(placeholder)) {
                System.out.println("Replacing standard field: " + placeholder + " with value: " + displayValue);
                reportContent = reportContent.replace(placeholder, displayValue);
            }
        }
        
        System.out.println("Final report content: " + reportContent);
        return reportContent;
    }

    @Override
    public byte[] generateReportPdfFromTemplate(Long templateId, Map<String, Object> fieldValues) {
        String reportContent = generateReportFromTemplate(templateId, fieldValues);
        ReportTemplate template = getTemplateById(templateId);
        
        try {
            // Ensure the HTML content is properly formatted for PDF generation
            String htmlContent = ensureValidHtml(reportContent, template.getTemplateName());
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(outputStream);
            builder.run();
            
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate report PDF: " + e.getMessage(), e);
        }
    }
    
    /**
     * Ensures the HTML content is valid and properly formatted for PDF generation
     */
    private String ensureValidHtml(String htmlContent, String templateName) {
        // If the content is already a complete HTML document, return as is
        if (htmlContent.trim().toLowerCase().startsWith("<!doctype html") || 
            htmlContent.trim().toLowerCase().startsWith("<html")) {
            return htmlContent;
        }
        
        // Otherwise, wrap the content in a minimal HTML structure that preserves the original layout
        return String.format("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    * { 
                        margin: 0; 
                        padding: 0; 
                        box-sizing: border-box; 
                    }
                    body { 
                        font-family: Arial, sans-serif; 
                        margin: 0; 
                        padding: 0;
                        background: white;
                    }
                    @page {
                        size: A4;
                        margin: 0;
                    }
                    @media print {
                        body { margin: 0; }
                    }
                </style>
            </head>
            <body>
                %s
            </body>
            </html>
            """, htmlContent);
    }

    @Override
    public List<String> extractDynamicFieldsFromTemplate(Long templateId) {
        ReportTemplate template = getTemplateById(templateId);
        List<String> fields = new ArrayList<>();
        
        JsonNode dynamicFields = template.getDynamicFields();
        if (dynamicFields != null && dynamicFields.has("fields")) {
            JsonNode fieldsArray = dynamicFields.get("fields");
            
            for (JsonNode field : fieldsArray) {
                if (field.has("fieldName")) {
                    fields.add(field.get("fieldName").asText());
                }
            }
        }
        
        return fields;
    }

    @Override
    public Map<String, Object> getDefaultFieldValues(Long templateId) {
        List<String> fieldNames = extractDynamicFieldsFromTemplate(templateId);
        Map<String, Object> defaultValues = new HashMap<>();
        
        for (String fieldName : fieldNames) {
            defaultValues.put(fieldName, "");
        }
        
        return defaultValues;
    }
}
