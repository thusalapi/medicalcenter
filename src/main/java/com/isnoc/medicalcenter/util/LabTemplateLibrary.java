package com.isnoc.medicalcenter.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for managing lab report templates
 */
@Component
public class LabTemplateLibrary {
    
    private final ObjectMapper objectMapper;
    private Map<String, JsonNode> templateMap;
    
    public LabTemplateLibrary(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.templateMap = new HashMap<>();
        initializeDefaultTemplates();
    }
    
    /**
     * Initialize some default templates for common lab reports
     */
    private void initializeDefaultTemplates() {
        // Example: Full Blood Count template
        ObjectNode fbcTemplate = objectMapper.createObjectNode();
        fbcTemplate.put("name", "Full Blood Count (FBC)");
        
        ArrayNode fbcFields = objectMapper.createArrayNode();
        addField(fbcFields, "WBC", "White Blood Cell Count", "x10^9/L", "4.0-11.0");
        addField(fbcFields, "RBC", "Red Blood Cell Count", "x10^12/L", "4.5-6.5 (M), 3.8-5.8 (F)");
        addField(fbcFields, "Hgb", "Hemoglobin", "g/dL", "13.5-17.5 (M), 12.0-16.0 (F)");
        addField(fbcFields, "Hct", "Hematocrit", "%", "41-53 (M), 36-46 (F)");
        addField(fbcFields, "MCV", "Mean Corpuscular Volume", "fL", "80-100");
        
        fbcTemplate.set("fields", fbcFields);
        templateMap.put("fbc", fbcTemplate);
        
        // Example: Liver Function Test template
        ObjectNode lftTemplate = objectMapper.createObjectNode();
        lftTemplate.put("name", "Liver Function Test (LFT)");
        
        ArrayNode lftFields = objectMapper.createArrayNode();
        addField(lftFields, "ALT", "Alanine Transaminase", "U/L", "7-56");
        addField(lftFields, "AST", "Aspartate Transaminase", "U/L", "5-40");
        addField(lftFields, "ALP", "Alkaline Phosphatase", "U/L", "44-147");
        addField(lftFields, "GGT", "Gamma-Glutamyl Transferase", "U/L", "9-48");
        addField(lftFields, "Bilirubin", "Total Bilirubin", "mg/dL", "0.1-1.2");
        
        lftTemplate.set("fields", lftFields);
        templateMap.put("lft", lftTemplate);
    }
    
    /**
     * Helper method to add a field to the fields array
     */
    private void addField(ArrayNode fields, String code, String name, String unit, String referenceRange) {
        ObjectNode field = objectMapper.createObjectNode();
        field.put("code", code);
        field.put("name", name);
        field.put("unit", unit);
        field.put("referenceRange", referenceRange);
        fields.add(field);
    }
    
    /**
     * Get all available templates
     */
    public List<JsonNode> getAllTemplates() {
        return new ArrayList<>(templateMap.values());
    }
    
    /**
     * Get a specific template by ID
     */
    public JsonNode getTemplate(String templateId) {
        return templateMap.get(templateId);
    }
    
    /**
     * Add or update a template
     */
    public void saveTemplate(String templateId, JsonNode template) {
        templateMap.put(templateId, template);
    }
    
    /**
     * Delete a template
     */
    public boolean deleteTemplate(String templateId) {
        return templateMap.remove(templateId) != null;
    }
}