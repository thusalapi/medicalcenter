package com.isnoc.medicalcenter.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.isnoc.medicalcenter.service.ReportTypeService;
import com.isnoc.medicalcenter.util.LabTemplateLibrary;
import com.isnoc.medicalcenter.util.ReportTemplateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller for managing laboratory report templates
 */
@RestController
@RequestMapping("/api/lab-templates")
public class LabReportTemplateController {

    private final ReportTypeService reportTypeService;
    private final ReportTemplateGenerator templateGenerator;
    private final ObjectMapper objectMapper;

    @Autowired
    public LabReportTemplateController(
            ReportTypeService reportTypeService,
            ReportTemplateGenerator templateGenerator,
            ObjectMapper objectMapper) {
        this.reportTypeService = reportTypeService;
        this.templateGenerator = templateGenerator;
        this.objectMapper = objectMapper;
    }

    /**
     * Bulk import predefined laboratory test templates
     *
     * @return Response with details of created templates
     */    @PostMapping("/import-predefined")
    public ResponseEntity<Map<String, Object>> importPredefinedTemplates() {
        // Organize lab tests into categories for better organization
        Map<String, List<String>> labTestCategories = new HashMap<>();
        
        // Blood Chemistry Tests
        labTestCategories.put("BLOOD CHEMISTRY", Arrays.asList(
            "ALK PHOSPATASE", "AMYLASE", "BILIRUBIN", "CHLORIDE", "CHOLESTEROL",
            "C.K", "CREATININE", "ELECTROLYTE", "GAMMAGT", "URIC ACID", "UREA",
            "SERUM CALCIUM", "SERUM PHOSPHOROUS", "SERUM MAGNESIUM",
            "SERUM IRON", "SERUM COPPER", "SERUM ZINC", "SERUM AMMONIA", "SERUM LACTATE",
            "TRIGLYCERIDS", "SODIUM", "POTASSIUM", "PROTEIN", "ALBUMIN",
            "S.G.O.T. (AST)", "S.G.P.T. (ALT)", "CPK MB", "TROPONIN I"
        ));
        
        // Glucose Tests
        labTestCategories.put("GLUCOSE", Arrays.asList(
            "FBS", "RBS", "PPBS", "G.T.T.", "G.T.T. (NORMAL)", "G.T.T. (SPOT)",
            "GLUCO HAEMOGLOBIN-HBA1C(HbA1C)", "GLUCOSE CHALLENGE TEST (GCT)", "MINI GTT"
        ));
        
        // Hematology Tests
        labTestCategories.put("HEMATOLOGY", Arrays.asList(
            "HAEMOGLOBIN", "BLOOD GROUPING", "PROTHROMBIN TIME (PT)",
            "PARTIAL PROTHROMBOPLASTIM TIME (APTT)", "BLOOD PICTURE", "BT/CT", 
            "CROSS MATCHING", "ESR", "GROUP RH.", "FBC - AUTO CALCULATION", 
            "MALARIAL PARASITES", "PCV", "PLATELET COUNT", "WBC/DC,MP,PCV,PLATELET COUNT,Hb", 
            "RETICULOCYTE COUNT"
        ));
        
        // Profiles and Panels
        labTestCategories.put("PROFILES", Arrays.asList(
            "LIVER FUNCTION/PROFILE TEST", "LIPID PROFILE", "RENAL PROFILE"
        ));
        
        // Hormone Tests
        labTestCategories.put("HORMONES", Arrays.asList(
            "TSH", "FT4", "FT3", "FSH", "LH", "PROGESTERON", "PROLACTIN", 
            "TESTOSTERONE", "SERUM HCG"
        ));
        
        // Urine Tests
        labTestCategories.put("URINE", Arrays.asList(
            "URINE MICROALBUMIN", "URINE PROTEIN/CR.RATIO(UPCR)",
            "BILE PIGMENTS", "PREGNANCY", "SUGAR", "UROBILINOGEN", "UFR"
        ));
        
        // Infectious Disease Tests
        labTestCategories.put("INFECTIOUS", Arrays.asList(
            "DENGUE ANTIBODY TEST", "DENGUE ANTIGEN", "V.D.R.L", "GONORRHEA", 
            "CHLAMYDIA", "HIV I & II", "HBSAG", "ANTI HCV", "TPHA", "H PYLORI", 
            "TB ANTIBODY", "TB PCR", "AFB", "MANTOUX", "WIDAL"
        ));
        
        // Tumor Markers
        labTestCategories.put("TUMOR MARKERS", Arrays.asList(
            "PSA", "FERRITIN", "CA-125", "CEA"
        ));
          // Other Tests
        labTestCategories.put("OTHER", Arrays.asList(
            "SEMINAL ANALYSIS", "STOOL FULL REPORT", "ANTI NUCLEAR ANTIBODY/ANA",
            "CREATININE CLEARANCE", "BLOOD UREA", "BUN", "BILIRUBIN FOR CHILDREN"
        ));
        
        // Create templates for all categories
        List<String> created = new ArrayList<>();
        List<String> skipped = new ArrayList<>();
        
        for (Map.Entry<String, List<String>> category : labTestCategories.entrySet()) {
            String categoryName = category.getKey();
            List<String> tests = category.getValue();
            
            for (String testName : tests) {
                String formattedName = categoryName + " - " + testName;
                
                try {
                    // Get appropriate template based on test name
                    JsonNode template;                    if (categoryName.equals("PROFILES") && testName.equals("LIPID PROFILE")) {
                        template = templateGenerator.createLipidProfileTemplate();
                    } else if (categoryName.equals("PROFILES") && testName.equals("LIVER FUNCTION/PROFILE TEST")) {
                        template = templateGenerator.createLiverProfileTemplate();
                    } else if (categoryName.equals("PROFILES") && testName.equals("RENAL PROFILE")) {
                        template = templateGenerator.createRenalProfileTemplate();
                    } else if (categoryName.equals("HEMATOLOGY") && testName.equals("FBC - AUTO CALCULATION")) {
                        template = templateGenerator.createCompleteBloodCountTemplate();
                    } else {
                        // For standard lab tests, generate a generic template
                        template = templateGenerator.createLabReportTemplate(testName, "", "");
                    }
                    
                    // Save template to database
                    reportTypeService.createReportType(formattedName, "Laboratory test template for " + testName, template);
                    created.add(formattedName);
                } catch (Exception e) {
                    // If a template with this name already exists or other error
                    skipped.add(formattedName);
                }
            }
        }
        
        // Prepare result
        Map<String, Object> result = new HashMap<>();
        result.put("createdCount", created.size());
        result.put("skippedCount", skipped.size());
        result.put("created", created);
        result.put("skipped", skipped);
        
        return ResponseEntity.ok(result);
    }
      /**
     * Create custom templates from a list of test names
     *
     * @param reportNames List of report/test names to create templates for
     * @return Response with details of created templates
     */
    @PostMapping("/create-custom")
    public ResponseEntity<Map<String, Object>> createCustomTemplates(@RequestBody List<String> reportNames) {
        List<String> created = new ArrayList<>();
        List<String> skipped = new ArrayList<>();
        
        // Process each custom template
        for (String testName : reportNames) {
            String formattedName = "CUSTOM - " + testName.trim();
            
            try {
                // Create a standard lab template
                JsonNode template = templateGenerator.createLabReportTemplate(testName, "", "");
                  // Save template to database
                reportTypeService.createReportType(formattedName, "Custom laboratory test template for " + testName, template);
                created.add(formattedName);
            } catch (Exception e) {
                // If a template with this name already exists or other error
                skipped.add(formattedName);
            }
        }
        
        // Prepare result
        Map<String, Object> result = new HashMap<>();
        result.put("createdCount", created.size());
        result.put("skippedCount", skipped.size());
        result.put("created", created);
        result.put("skipped", skipped);
        
        return ResponseEntity.ok(result);
    }
      /**
     * Helper method to check if a specific template fits special categories
     */    private boolean isSpecialTemplate(String testName) {
        List<String> specialTemplates = Arrays.asList(
            "LIPID PROFILE", "LIVER FUNCTION/PROFILE TEST", "RENAL PROFILE", 
            "FBC - AUTO CALCULATION", "COMPLETE BLOOD COUNT"
        );
        
        return specialTemplates.contains(testName);
    }
}
