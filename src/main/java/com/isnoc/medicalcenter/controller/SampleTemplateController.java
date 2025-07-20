package com.isnoc.medicalcenter.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.service.ReportTemplateService;
import com.isnoc.medicalcenter.util.SampleTemplateCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for creating sample report templates
 */
@RestController
@RequestMapping("/sample-templates")
public class SampleTemplateController {

    private final ReportTemplateService templateService;
    private final SampleTemplateCreator sampleCreator;

    @Autowired
    public SampleTemplateController(ReportTemplateService templateService, 
                                   SampleTemplateCreator sampleCreator) {
        this.templateService = templateService;
        this.sampleCreator = sampleCreator;
    }

    /**
     * Create sample blood test report template
     */
    @PostMapping("/blood-test")
    public ResponseEntity<Map<String, Object>> createBloodTestTemplate() {
        try {
            JsonNode templateData = sampleCreator.createBloodTestReportTemplate();
            
            templateService.createTemplate(
                "Blood Test Report Template",
                "Comprehensive blood test report with patient information and test results",
                "BLOOD_TEST",
                templateData.get("staticContent"),
                templateData.get("dynamicFields"),
                templateData.get("layoutConfig")
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Blood test template created successfully");
            response.put("templateName", "Blood Test Report Template");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to create template: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Create sample urine test report template
     */
    @PostMapping("/urine-test")
    public ResponseEntity<Map<String, Object>> createUrineTestTemplate() {
        try {
            JsonNode templateData = sampleCreator.createUrineTestReportTemplate();
            
            templateService.createTemplate(
                "Urine Analysis Report Template",
                "Comprehensive urine analysis report with patient information and test results",
                "URINE_TEST",
                templateData.get("staticContent"),
                templateData.get("dynamicFields"),
                templateData.get("layoutConfig")
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Urine test template created successfully");
            response.put("templateName", "Urine Analysis Report Template");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to create template: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Create all sample templates
     */
    @PostMapping("/create-all")
    public ResponseEntity<Map<String, Object>> createAllSampleTemplates() {
        Map<String, Object> response = new HashMap<>();
        int successCount = 0;
        int failureCount = 0;
        
        try {
            // Create blood test template
            createBloodTestTemplate();
            successCount++;
        } catch (Exception e) {
            failureCount++;
        }

        try {
            // Create urine test template
            createUrineTestTemplate();
            successCount++;
        } catch (Exception e) {
            failureCount++;
        }

        response.put("success", failureCount == 0);
        response.put("successCount", successCount);
        response.put("failureCount", failureCount);
        response.put("message", String.format("Created %d templates successfully, %d failed", 
                                            successCount, failureCount));

        return ResponseEntity.ok(response);
    }
}
