package com.isnoc.medicalcenter.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.isnoc.medicalcenter.entity.ReportType;
import com.isnoc.medicalcenter.service.ReportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller for bulk importing report templates
 */
@RestController
@RequestMapping("/api/template-import")
public class ReportTemplateImportController {

    private final ReportTypeService reportTypeService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ReportTemplateImportController(ReportTypeService reportTypeService, ObjectMapper objectMapper) {
        this.reportTypeService = reportTypeService;
        this.objectMapper = objectMapper;
    }

    /**
     * Bulk import of report templates from a list of report names
     *
     * @param reportNames List of report names to create templates for
     * @return Response with details of created templates
     */
    @PostMapping("/bulk-create")
    public ResponseEntity<Map<String, Object>> bulkCreateTemplates(@RequestBody List<String> reportNames) {
        List<String> created = new ArrayList<>();
        List<String> skipped = new ArrayList<>();
        
        for (String reportName : reportNames) {
            try {
                // Check if template already exists
                if (reportTypeService.findByReportName(reportName).isPresent()) {
                    skipped.add(reportName + " (already exists)");
                    continue;
                }
                
                // Create a standardized template for each report
                JsonNode templateJson = createStandardTemplate(reportName);
                
                // Create the report type
                reportTypeService.createReportType(
                    reportName,
                    "Standard template for " + reportName,
                    templateJson
                );
                
                created.add(reportName);
            } catch (Exception e) {
                skipped.add(reportName + " (" + e.getMessage() + ")");
            }
        }
        
        // Return results
        ObjectNode result = objectMapper.createObjectNode();
        result.put("success", true);
        result.put("createdCount", created.size());
        result.put("skippedCount", skipped.size());
        
        ArrayNode createdArray = result.putArray("created");
        created.forEach(createdArray::add);
        
        ArrayNode skippedArray = result.putArray("skipped");
        skipped.forEach(skippedArray::add);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(objectMapper.convertValue(result, Map.class));
    }
    
    /**
     * Creates a standardized template for a report type
     *
     * @param reportName The name of the report
     * @return JsonNode containing the template structure
     */
    private JsonNode createStandardTemplate(String reportName) {
        ObjectNode template = objectMapper.createObjectNode();
        template.put("paperSize", "A4");
        template.put("orientation", "portrait");
        
        // Create header section with clinic information
        ArrayNode fields = template.putArray("fields");
        
        // Add clinic logo/header
        addField(fields, "clinic_header", "UNAWATUNE MEDICAL CENTER", "text", 10, 10, 16, true, false);
        addField(fields, "clinic_address", "Main Street, Unawatune", "text", 10, 30, 12, false, false);
        addField(fields, "clinic_contact", "Tel: +94 123 456 789", "text", 10, 50, 12, false, false);
        
        // Add report title
        addField(fields, "report_title", reportName, "text", 10, 90, 18, true, false);
        
        // Add patient information section
        addField(fields, "patient_name_label", "Patient Name:", "text", 10, 130, 12, true, true);
        addField(fields, "patient_name", "", "text", 120, 130, 12, false, false);
        
        addField(fields, "patient_id_label", "Patient ID:", "text", 350, 130, 12, true, true);
        addField(fields, "patient_id", "", "text", 430, 130, 12, false, false);
        
        addField(fields, "age_label", "Age:", "text", 10, 155, 12, true, true);
        addField(fields, "age", "", "text", 120, 155, 12, false, false);
        
        addField(fields, "gender_label", "Gender:", "text", 350, 155, 12, true, true);
        addField(fields, "gender", "", "text", 430, 155, 12, false, false);
        
        // Add report date
        addField(fields, "report_date_label", "Report Date:", "text", 10, 180, 12, true, true);
        addField(fields, "report_date", "", "date", 120, 180, 12, false, false);
        
        // Add referring doctor
        addField(fields, "doctor_label", "Referring Doctor:", "text", 350, 180, 12, true, true);
        addField(fields, "doctor", "", "text", 450, 180, 12, false, false);
        
        // Add divider line
        addField(fields, "divider", "--------------------------------------------------------------------------------------------------------------------------------------", "text", 10, 210, 12, false, false);
        
        // Add result fields based on test type
        int yPos = 245;
        
        // Standard result fields (adjusted for each report type)
        addField(fields, "result_label", "Result:", "text", 10, yPos, 14, true, true);
        addField(fields, "result_value", "", "text", 150, yPos, 14, false, false);
        
        yPos += 40;
        addField(fields, "unit_label", "Unit:", "text", 10, yPos, 12, true, true);
        addField(fields, "unit_value", "", "text", 150, yPos, 12, false, false);
        
        yPos += 40;
        addField(fields, "reference_label", "Normal Reference Range:", "text", 10, yPos, 12, true, true);
        addField(fields, "reference_value", "", "text", 150, yPos, 12, false, false);
        
        yPos += 40;
        addField(fields, "note_label", "Notes:", "text", 10, yPos, 12, true, true);
        addField(fields, "note_value", "", "text", 150, yPos, 12, false, false);
        
        // Add signature section at bottom
        addField(fields, "signature_line", "__________________________", "text", 400, 600, 12, false, false);
        addField(fields, "signature_name", "Medical Officer", "text", 400, 620, 12, true, false);
        
        return template;
    }
    
    /**
     * Helper method to add a field to the fields array
     */
    private void addField(ArrayNode fields, String id, String label, String type, 
                          int x, int y, int fontSize, boolean bold, boolean showLabel) {
        ObjectNode field = objectMapper.createObjectNode();
        field.put("id", id);
        field.put("label", label);
        field.put("type", type);
        field.put("x", x);
        field.put("y", y);
        field.put("fontSize", fontSize);
        field.put("bold", bold);
        field.put("showLabel", showLabel);
        fields.add(field);
    }
}
