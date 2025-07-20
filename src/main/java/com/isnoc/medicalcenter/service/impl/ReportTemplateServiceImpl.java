package com.isnoc.medicalcenter.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.isnoc.medicalcenter.entity.ReportTemplate;
import com.isnoc.medicalcenter.exception.ResourceNotFoundException;
import com.isnoc.medicalcenter.repository.ReportTemplateRepository;
import com.isnoc.medicalcenter.service.ReportTemplateService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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
        
        // Replace dynamic field placeholders with actual values
        JsonNode dynamicFields = template.getDynamicFields();
        if (dynamicFields != null && dynamicFields.has("fields")) {
            JsonNode fields = dynamicFields.get("fields");
            
            for (JsonNode field : fields) {
                String fieldName = field.get("fieldName").asText();
                String placeholder = "{{" + fieldName + "}}";
                
                Object value = fieldValues.get(fieldName);
                String displayValue = value != null ? value.toString() : "";
                
                reportContent = reportContent.replace(placeholder, displayValue);
            }
        }
        
        return reportContent;
    }

    @Override
    public byte[] generateReportPdfFromTemplate(Long templateId, Map<String, Object> fieldValues) {
        String reportContent = generateReportFromTemplate(templateId, fieldValues);
        ReportTemplate template = getTemplateById(templateId);
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float yPosition = yStart;
            float lineHeight = 15f;
            
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Title
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(template.getTemplateName());
                contentStream.endText();
                yPosition -= lineHeight * 2;
                
                // Content - split by lines and handle page breaks
                String[] lines = reportContent.split("\n");
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                
                for (String line : lines) {
                    if (yPosition <= margin) {
                        // Need new page
                        contentStream.close();
                        
                        PDPage newPage = new PDPage(PDRectangle.A4);
                        document.addPage(newPage);
                        yPosition = yStart;
                        
                        PDPageContentStream newContentStream = new PDPageContentStream(document, newPage);
                        newContentStream.setFont(PDType1Font.HELVETICA, 12);
                        
                        newContentStream.beginText();
                        newContentStream.newLineAtOffset(margin, yPosition);
                        newContentStream.showText(line);
                        newContentStream.endText();
                        yPosition -= lineHeight;
                        
                        newContentStream.close();
                    } else {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText(line);
                        contentStream.endText();
                        yPosition -= lineHeight;
                    }
                }
            }
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate report PDF", e);
        }
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
