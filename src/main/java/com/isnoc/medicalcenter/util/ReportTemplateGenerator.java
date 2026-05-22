package com.isnoc.medicalcenter.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility class for creating report templates
 */
@Component
public class ReportTemplateGenerator {
    
    private final ObjectMapper objectMapper;
    
    @Autowired
    public ReportTemplateGenerator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
      /**
     * Creates a standardized template for a laboratory report
     *
     * @param reportName The name of the report/test
     * @param referenceRanges Optional reference range information for the test
     * @param units Optional units for the test
     * @return A JsonNode containing the template structure
     */
    public JsonNode createLabReportTemplate(String reportName, String referenceRanges, String units) {
        ObjectNode template = objectMapper.createObjectNode();
        template.put("paperSize", "A4");
        template.put("orientation", "portrait");
        template.put("reportType", "LAB_TEST");
        
        ArrayNode fields = template.putArray("fields");
        
        // Header section
        addField(fields, "clinic_header", "UNAWATUNE MEDICAL CENTER", "text", 10, 10, 16, true, false);
        addField(fields, "clinic_address", "Main Street, Unawatune", "text", 10, 30, 12, false, false);
        addField(fields, "clinic_contact", "Tel: +94 123 456 789", "text", 10, 50, 12, false, false);
        
        // Report title
        addField(fields, "report_title", reportName, "text", 10, 90, 18, true, false);
        
        // Patient information section
        addField(fields, "patient_name_label", "Patient Name:", "text", 10, 130, 12, true, true);
        addField(fields, "patient_name", "", "text", 120, 130, 12, false, false);
        
        addField(fields, "patient_id_label", "Patient ID:", "text", 350, 130, 12, true, true);
        addField(fields, "patient_id", "", "text", 430, 130, 12, false, false);
        
        addField(fields, "age_label", "Age:", "text", 10, 155, 12, true, true);
        addField(fields, "age", "", "text", 120, 155, 12, false, false);
        
        addField(fields, "gender_label", "Gender:", "text", 350, 155, 12, true, true);
        addField(fields, "gender", "", "text", 430, 155, 12, false, false);
        
        // Report metadata
        addField(fields, "report_date_label", "Report Date:", "text", 10, 180, 12, true, true);        addField(fields, "report_date", "", "date", 120, 180, 12, false, false);
        
        addField(fields, "sample_date_label", "Sample Collected:", "text", 350, 180, 12, true, true);
        addField(fields, "sample_date", "", "date", 450, 180, 12, false, false);
        
        addField(fields, "doctor_label", "Referring Doctor:", "text", 10, 205, 12, true, true);
        addField(fields, "doctor", "", "text", 120, 205, 12, false, false);
        
        // Divider
        addField(fields, "divider", "--------------------------------------------------------------------------------------------------------------------------------------", "text", 10, 235, 12, false, false);
        
        // Test Information
        addField(fields, "test_name", reportName, "text", 10, 250, 14, true, false);
        
        // Result fields
        int yPos = 280;
        
        addField(fields, "result_label", "Result:", "text", 10, yPos, 14, true, true);
        addField(fields, "result_value", "", "number", 150, yPos, 14, false, false);
        
        yPos += 40;
        
        if (units != null && !units.isEmpty()) {
            addField(fields, "unit_label", "Unit:", "text", 10, yPos, 12, true, true);
            addField(fields, "unit_value", units, "text", 150, yPos, 12, false, false);
            yPos += 40;
        }
        
        if (referenceRanges != null && !referenceRanges.isEmpty()) {
            addField(fields, "reference_label", "Normal Reference Range:", "text", 10, yPos, 12, true, true);
            addField(fields, "reference_value", referenceRanges, "text", 150, yPos, 12, false, false);
            yPos += 40;
        }
        
        addField(fields, "note_label", "Notes:", "text", 10, yPos, 12, true, true);
        addField(fields, "note_value", "", "text", 150, yPos, 12, false, false);
        
        // Signature section
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
    
    /**
     * Creates templates customized for specific report types
     *
     * @param reportName The name of the report/test
     * @return A JsonNode containing the customized template
     */
    public JsonNode createCustomizedTemplate(String reportName) {
        // This method can be expanded to handle special template cases
        // For now, we'll create specific templates for some common test types
        
        switch (reportName.trim().toUpperCase()) {
            case "COMPLETE BLOOD COUNT":
            case "FBC - AUTO CALCULATION":
                return createCompleteBloodCountTemplate();
            case "BLOOD GROUPING":
            case "GROUP RH.":
                return createBloodGroupTemplate();
            case "LIPID PROFILE":
                return createLipidProfileTemplate();
            case "LIVER FUNCTION/PROFILE TEST":
                return createLiverProfileTemplate();
            case "ELECTROLYTE":
                return createElectrolyteTemplate();
            case "RENAL PROFILE":
                return createRenalProfileTemplate();
            case "HAEMOGLOBIN":
                return createLabReportTemplate(reportName, "Male: 13-17 g/dL, Female: 12-15 g/dL", "g/dL");
            case "CHOLESTEROL":
                return createLabReportTemplate(reportName, "<200 mg/dL", "mg/dL");
            case "CREATININE":
                return createLabReportTemplate(reportName, "Male: 0.7-1.3 mg/dL, Female: 0.6-1.1 mg/dL", "mg/dL");
            case "FBS":
            case "RBS":
            case "PPBS":
                return createLabReportTemplate(reportName, "70-100 mg/dL (fasting)", "mg/dL");
            case "UREA":
            case "BLOOD UREA":
                return createLabReportTemplate(reportName, "7-20 mg/dL", "mg/dL");
            case "URIC ACID":
                return createLabReportTemplate(reportName, "Male: 3.4-7.0 mg/dL, Female: 2.4-6.0 mg/dL", "mg/dL");
            default:
                return createLabReportTemplate(reportName, "", "");
        }
    }
    
    public JsonNode createCompleteBloodCountTemplate() {
        ObjectNode template = objectMapper.createObjectNode();
        template.put("paperSize", "A4");
        template.put("orientation", "portrait");
        
        ArrayNode fields = template.putArray("fields");
        
        // Header
        addField(fields, "clinic_header", "UNAWATUNE MEDICAL CENTER", "text", 10, 10, 16, true, false);
        addField(fields, "clinic_address", "Main Street, Unawatune", "text", 10, 30, 12, false, false);
        addField(fields, "clinic_contact", "Tel: +94 123 456 789", "text", 10, 50, 12, false, false);
        
        addField(fields, "report_title", "COMPLETE BLOOD COUNT (CBC)", "text", 10, 90, 18, true, false);
        
        // Patient information
        addField(fields, "patient_name_label", "Patient Name:", "text", 10, 130, 12, true, true);
        addField(fields, "patient_name", "", "text", 120, 130, 12, false, false);
        
        addField(fields, "patient_id_label", "Patient ID:", "text", 350, 130, 12, true, true);
        addField(fields, "patient_id", "", "text", 430, 130, 12, false, false);
        
        addField(fields, "age_label", "Age:", "text", 10, 155, 12, true, true);
        addField(fields, "age", "", "text", 120, 155, 12, false, false);
        
        addField(fields, "gender_label", "Gender:", "text", 350, 155, 12, true, true);
        addField(fields, "gender", "", "text", 430, 155, 12, false, false);
        
        // Report metadata
        addField(fields, "report_date_label", "Report Date:", "text", 10, 180, 12, true, true);
        addField(fields, "report_date", "", "date", 120, 180, 12, false, false);
        
        addField(fields, "sample_date_label", "Sample Collected:", "text", 350, 180, 12, true, true);
        addField(fields, "sample_date", "", "date", 450, 180, 12, false, false);
        
        addField(fields, "doctor_label", "Referring Doctor:", "text", 10, 205, 12, true, true);
        addField(fields, "doctor", "", "text", 120, 205, 12, false, false);
        
        // Divider
        addField(fields, "divider", "--------------------------------------------------------------------------------------------------------------------------------------", "text", 10, 235, 12, false, false);
        
        // CBC Result Table Header
        int yPos = 270;
        addField(fields, "table_header_param", "Parameter", "text", 10, yPos, 12, true, false);
        addField(fields, "table_header_result", "Result", "text", 200, yPos, 12, true, false);
        addField(fields, "table_header_unit", "Unit", "text", 300, yPos, 12, true, false);
        addField(fields, "table_header_range", "Reference Range", "text", 400, yPos, 12, true, false);
        
        // CBC Parameters
        yPos += 30;
        addField(fields, "wbc_label", "WBC (White Blood Cells)", "text", 10, yPos, 12, false, false);
        addField(fields, "wbc_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "wbc_unit", "x10³/μL", "text", 300, yPos, 12, false, false);
        addField(fields, "wbc_range", "4.5-11.0", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "rbc_label", "RBC (Red Blood Cells)", "text", 10, yPos, 12, false, false);
        addField(fields, "rbc_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "rbc_unit", "x10⁶/μL", "text", 300, yPos, 12, false, false);
        addField(fields, "rbc_range", "M: 4.5-5.9, F: 4.0-5.2", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "hgb_label", "Hemoglobin (Hgb)", "text", 10, yPos, 12, false, false);
        addField(fields, "hgb_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "hgb_unit", "g/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "hgb_range", "M: 13-17, F: 12-15", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "hct_label", "Hematocrit (Hct)", "text", 10, yPos, 12, false, false);
        addField(fields, "hct_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "hct_unit", "%", "text", 300, yPos, 12, false, false);
        addField(fields, "hct_range", "M: 40-52, F: 36-48", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "mcv_label", "MCV", "text", 10, yPos, 12, false, false);
        addField(fields, "mcv_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "mcv_unit", "fL", "text", 300, yPos, 12, false, false);
        addField(fields, "mcv_range", "80-100", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "mch_label", "MCH", "text", 10, yPos, 12, false, false);
        addField(fields, "mch_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "mch_unit", "pg", "text", 300, yPos, 12, false, false);
        addField(fields, "mch_range", "27-34", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "mchc_label", "MCHC", "text", 10, yPos, 12, false, false);
        addField(fields, "mchc_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "mchc_unit", "g/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "mchc_range", "32-36", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "platelet_label", "Platelets", "text", 10, yPos, 12, false, false);
        addField(fields, "platelet_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "platelet_unit", "x10³/μL", "text", 300, yPos, 12, false, false);
        addField(fields, "platelet_range", "150-450", "text", 400, yPos, 12, false, false);
        
        // Signature
        addField(fields, "signature_line", "__________________________", "text", 400, 600, 12, false, false);
        addField(fields, "signature_name", "Medical Officer", "text", 400, 620, 12, true, false);
        
        return template;
    }
    
    private JsonNode createBloodGroupTemplate() {
        ObjectNode template = objectMapper.createObjectNode();
        template.put("paperSize", "A4");
        template.put("orientation", "portrait");
        
        ArrayNode fields = template.putArray("fields");
        
        // Header
        addField(fields, "clinic_header", "UNAWATUNE MEDICAL CENTER", "text", 10, 10, 16, true, false);
        addField(fields, "clinic_address", "Main Street, Unawatune", "text", 10, 30, 12, false, false);
        addField(fields, "clinic_contact", "Tel: +94 123 456 789", "text", 10, 50, 12, false, false);
        
        addField(fields, "report_title", "BLOOD GROUPING REPORT", "text", 10, 90, 18, true, false);
        
        // Patient information
        addField(fields, "patient_name_label", "Patient Name:", "text", 10, 130, 12, true, true);
        addField(fields, "patient_name", "", "text", 120, 130, 12, false, false);
        
        addField(fields, "patient_id_label", "Patient ID:", "text", 350, 130, 12, true, true);
        addField(fields, "patient_id", "", "text", 430, 130, 12, false, false);
        
        addField(fields, "age_label", "Age:", "text", 10, 155, 12, true, true);
        addField(fields, "age", "", "text", 120, 155, 12, false, false);
        
        addField(fields, "gender_label", "Gender:", "text", 350, 155, 12, true, true);
        addField(fields, "gender", "", "text", 430, 155, 12, false, false);
        
        // Report metadata
        addField(fields, "report_date_label", "Report Date:", "text", 10, 180, 12, true, true);
        addField(fields, "report_date", "", "date", 120, 180, 12, false, false);
        
        addField(fields, "sample_date_label", "Sample Collected:", "text", 350, 180, 12, true, true);
        addField(fields, "sample_date", "", "date", 450, 180, 12, false, false);
        
        // Divider
        addField(fields, "divider", "--------------------------------------------------------------------------------------------------------------------------------------", "text", 10, 210, 12, false, false);
        
        // Blood Type Result
        int yPos = 270;
        addField(fields, "abo_label", "ABO Group:", "text", 150, yPos, 14, true, true);
        addField(fields, "abo_value", "", "text", 260, yPos, 20, true, false);
        
        yPos += 50;
        addField(fields, "rh_label", "Rh Factor:", "text", 150, yPos, 14, true, true);
        addField(fields, "rh_value", "", "text", 260, yPos, 20, true, false);
        
        yPos += 50;
        addField(fields, "full_group_label", "Blood Group:", "text", 150, yPos, 14, true, true);
        addField(fields, "full_group_value", "", "text", 260, yPos, 20, true, false);
        
        yPos += 80;
        addField(fields, "note_label", "Notes:", "text", 10, yPos, 12, true, true);
        addField(fields, "note_value", "", "text", 150, yPos, 12, false, false);
        
        // Signature
        addField(fields, "signature_line", "__________________________", "text", 400, 600, 12, false, false);
        addField(fields, "signature_name", "Medical Officer", "text", 400, 620, 12, true, false);
        
        return template;
    }
    
    public JsonNode createLipidProfileTemplate() {
        ObjectNode template = objectMapper.createObjectNode();
        template.put("paperSize", "A4");
        template.put("orientation", "portrait");
        
        ArrayNode fields = template.putArray("fields");
        
        // Header
        addField(fields, "clinic_header", "UNAWATUNE MEDICAL CENTER", "text", 10, 10, 16, true, false);
        addField(fields, "clinic_address", "Main Street, Unawatune", "text", 10, 30, 12, false, false);
        addField(fields, "clinic_contact", "Tel: +94 123 456 789", "text", 10, 50, 12, false, false);
        
        addField(fields, "report_title", "LIPID PROFILE", "text", 10, 90, 18, true, false);
        
        // Patient information
        addField(fields, "patient_name_label", "Patient Name:", "text", 10, 130, 12, true, true);
        addField(fields, "patient_name", "", "text", 120, 130, 12, false, false);
        
        addField(fields, "patient_id_label", "Patient ID:", "text", 350, 130, 12, true, true);
        addField(fields, "patient_id", "", "text", 430, 130, 12, false, false);
        
        addField(fields, "age_label", "Age:", "text", 10, 155, 12, true, true);
        addField(fields, "age", "", "text", 120, 155, 12, false, false);
        
        addField(fields, "gender_label", "Gender:", "text", 350, 155, 12, true, true);
        addField(fields, "gender", "", "text", 430, 155, 12, false, false);
        
        // Report metadata
        addField(fields, "report_date_label", "Report Date:", "text", 10, 180, 12, true, true);
        addField(fields, "report_date", "", "date", 120, 180, 12, false, false);
        
        addField(fields, "sample_date_label", "Sample Collected:", "text", 350, 180, 12, true, true);
        addField(fields, "sample_date", "", "date", 450, 180, 12, false, false);
        
        addField(fields, "fasting_label", "Fasting Status:", "text", 10, 205, 12, true, true);
        addField(fields, "fasting_status", "", "text", 120, 205, 12, false, false);
        
        // Divider
        addField(fields, "divider", "--------------------------------------------------------------------------------------------------------------------------------------", "text", 10, 235, 12, false, false);
        
        // Lipid Profile Table Header
        int yPos = 270;
        addField(fields, "table_header_test", "Test", "text", 10, yPos, 12, true, false);
        addField(fields, "table_header_result", "Result", "text", 200, yPos, 12, true, false);
        addField(fields, "table_header_unit", "Unit", "text", 300, yPos, 12, true, false);
        addField(fields, "table_header_range", "Reference Range", "text", 400, yPos, 12, true, false);
        
        // Lipid Parameters
        yPos += 30;
        addField(fields, "total_chol_label", "Total Cholesterol", "text", 10, yPos, 12, false, false);
        addField(fields, "total_chol_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "total_chol_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "total_chol_range", "<200", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "hdl_label", "HDL Cholesterol", "text", 10, yPos, 12, false, false);
        addField(fields, "hdl_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "hdl_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "hdl_range", ">40", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "ldl_label", "LDL Cholesterol", "text", 10, yPos, 12, false, false);
        addField(fields, "ldl_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "ldl_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "ldl_range", "<100", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "trig_label", "Triglycerides", "text", 10, yPos, 12, false, false);
        addField(fields, "trig_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "trig_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "trig_range", "<150", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "vldl_label", "VLDL Cholesterol", "text", 10, yPos, 12, false, false);
        addField(fields, "vldl_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "vldl_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "vldl_range", "<30", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "ratio_label", "Total Chol/HDL Ratio", "text", 10, yPos, 12, false, false);
        addField(fields, "ratio_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "ratio_unit", "", "text", 300, yPos, 12, false, false);
        addField(fields, "ratio_range", "<4.5", "text", 400, yPos, 12, false, false);
        
        yPos += 40;
        addField(fields, "note_label", "Interpretation:", "text", 10, yPos, 12, true, true);
        addField(fields, "note_value", "", "text", 10, yPos + 25, 12, false, false);
        
        // Signature
        addField(fields, "signature_line", "__________________________", "text", 400, 600, 12, false, false);
        addField(fields, "signature_name", "Medical Officer", "text", 400, 620, 12, true, false);
        
        return template;
    }
    
    public JsonNode createLiverProfileTemplate() {
        ObjectNode template = objectMapper.createObjectNode();
        template.put("paperSize", "A4");
        template.put("orientation", "portrait");
        
        ArrayNode fields = template.putArray("fields");
        
        // Header
        addField(fields, "clinic_header", "UNAWATUNE MEDICAL CENTER", "text", 10, 10, 16, true, false);
        addField(fields, "clinic_address", "Main Street, Unawatune", "text", 10, 30, 12, false, false);
        addField(fields, "clinic_contact", "Tel: +94 123 456 789", "text", 10, 50, 12, false, false);
        
        addField(fields, "report_title", "LIVER FUNCTION TEST", "text", 10, 90, 18, true, false);
        
        // Patient information
        addField(fields, "patient_name_label", "Patient Name:", "text", 10, 130, 12, true, true);
        addField(fields, "patient_name", "", "text", 120, 130, 12, false, false);
        
        addField(fields, "patient_id_label", "Patient ID:", "text", 350, 130, 12, true, true);
        addField(fields, "patient_id", "", "text", 430, 130, 12, false, false);
        
        addField(fields, "age_label", "Age:", "text", 10, 155, 12, true, true);
        addField(fields, "age", "", "text", 120, 155, 12, false, false);
        
        addField(fields, "gender_label", "Gender:", "text", 350, 155, 12, true, true);
        addField(fields, "gender", "", "text", 430, 155, 12, false, false);
        
        // Report metadata
        addField(fields, "report_date_label", "Report Date:", "text", 10, 180, 12, true, true);
        addField(fields, "report_date", "", "date", 120, 180, 12, false, false);
        
        addField(fields, "sample_date_label", "Sample Collected:", "text", 350, 180, 12, true, true);
        addField(fields, "sample_date", "", "date", 450, 180, 12, false, false);
        
        // Divider
        addField(fields, "divider", "--------------------------------------------------------------------------------------------------------------------------------------", "text", 10, 210, 12, false, false);
        
        // Liver Function Test Results
        int yPos = 250;
        addField(fields, "table_header_test", "Test", "text", 10, yPos, 12, true, false);
        addField(fields, "table_header_result", "Result", "text", 200, yPos, 12, true, false);
        addField(fields, "table_header_unit", "Unit", "text", 300, yPos, 12, true, false);
        addField(fields, "table_header_range", "Reference Range", "text", 400, yPos, 12, true, false);
        
        // Liver Parameters
        yPos += 30;
        addField(fields, "total_bili_label", "Total Bilirubin", "text", 10, yPos, 12, false, false);
        addField(fields, "total_bili_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "total_bili_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "total_bili_range", "0.3-1.0", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "direct_bili_label", "Direct Bilirubin", "text", 10, yPos, 12, false, false);
        addField(fields, "direct_bili_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "direct_bili_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "direct_bili_range", "0.0-0.3", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "indirect_bili_label", "Indirect Bilirubin", "text", 10, yPos, 12, false, false);
        addField(fields, "indirect_bili_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "indirect_bili_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "indirect_bili_range", "0.2-0.8", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "sgot_label", "SGOT (AST)", "text", 10, yPos, 12, false, false);
        addField(fields, "sgot_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "sgot_unit", "U/L", "text", 300, yPos, 12, false, false);
        addField(fields, "sgot_range", "5-40", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "sgpt_label", "SGPT (ALT)", "text", 10, yPos, 12, false, false);
        addField(fields, "sgpt_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "sgpt_unit", "U/L", "text", 300, yPos, 12, false, false);
        addField(fields, "sgpt_range", "7-56", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "alp_label", "Alkaline Phosphatase", "text", 10, yPos, 12, false, false);
        addField(fields, "alp_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "alp_unit", "U/L", "text", 300, yPos, 12, false, false);
        addField(fields, "alp_range", "45-115", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "ggt_label", "Gamma GT", "text", 10, yPos, 12, false, false);
        addField(fields, "ggt_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "ggt_unit", "U/L", "text", 300, yPos, 12, false, false);
        addField(fields, "ggt_range", "8-61", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "protein_label", "Total Protein", "text", 10, yPos, 12, false, false);
        addField(fields, "protein_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "protein_unit", "g/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "protein_range", "6.4-8.3", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "albumin_label", "Albumin", "text", 10, yPos, 12, false, false);
        addField(fields, "albumin_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "albumin_unit", "g/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "albumin_range", "3.5-5.0", "text", 400, yPos, 12, false, false);
        
        yPos += 40;
        addField(fields, "note_label", "Interpretation:", "text", 10, yPos, 12, true, true);
        addField(fields, "note_value", "", "text", 10, yPos + 20, 12, false, false);
        
        // Signature
        addField(fields, "signature_line", "__________________________", "text", 400, 600, 12, false, false);
        addField(fields, "signature_name", "Medical Officer", "text", 400, 620, 12, true, false);
        
        return template;
    }
    
    private JsonNode createElectrolyteTemplate() {
        ObjectNode template = objectMapper.createObjectNode();
        template.put("paperSize", "A4");
        template.put("orientation", "portrait");
        
        ArrayNode fields = template.putArray("fields");
        
        // Header
        addField(fields, "clinic_header", "UNAWATUNE MEDICAL CENTER", "text", 10, 10, 16, true, false);
        addField(fields, "clinic_address", "Main Street, Unawatune", "text", 10, 30, 12, false, false);
        addField(fields, "clinic_contact", "Tel: +94 123 456 789", "text", 10, 50, 12, false, false);
        
        addField(fields, "report_title", "ELECTROLYTE PANEL", "text", 10, 90, 18, true, false);
        
        // Patient information
        addField(fields, "patient_name_label", "Patient Name:", "text", 10, 130, 12, true, true);
        addField(fields, "patient_name", "", "text", 120, 130, 12, false, false);
        
        addField(fields, "patient_id_label", "Patient ID:", "text", 350, 130, 12, true, true);
        addField(fields, "patient_id", "", "text", 430, 130, 12, false, false);
        
        addField(fields, "age_label", "Age:", "text", 10, 155, 12, true, true);
        addField(fields, "age", "", "text", 120, 155, 12, false, false);
        
        addField(fields, "gender_label", "Gender:", "text", 350, 155, 12, true, true);
        addField(fields, "gender", "", "text", 430, 155, 12, false, false);
        
        // Report metadata
        addField(fields, "report_date_label", "Report Date:", "text", 10, 180, 12, true, true);
        addField(fields, "report_date", "", "date", 120, 180, 12, false, false);
        
        addField(fields, "sample_date_label", "Sample Collected:", "text", 350, 180, 12, true, true);
        addField(fields, "sample_date", "", "date", 450, 180, 12, false, false);
        
        // Divider
        addField(fields, "divider", "--------------------------------------------------------------------------------------------------------------------------------------", "text", 10, 210, 12, false, false);
        
        // Electrolyte Results
        int yPos = 250;
        addField(fields, "table_header_test", "Test", "text", 10, yPos, 12, true, false);
        addField(fields, "table_header_result", "Result", "text", 200, yPos, 12, true, false);
        addField(fields, "table_header_unit", "Unit", "text", 300, yPos, 12, true, false);
        addField(fields, "table_header_range", "Reference Range", "text", 400, yPos, 12, true, false);
        
        // Electrolyte Parameters
        yPos += 30;
        addField(fields, "sodium_label", "Sodium (Na+)", "text", 10, yPos, 12, false, false);
        addField(fields, "sodium_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "sodium_unit", "mmol/L", "text", 300, yPos, 12, false, false);
        addField(fields, "sodium_range", "135-145", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "potassium_label", "Potassium (K+)", "text", 10, yPos, 12, false, false);
        addField(fields, "potassium_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "potassium_unit", "mmol/L", "text", 300, yPos, 12, false, false);
        addField(fields, "potassium_range", "3.5-5.1", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "chloride_label", "Chloride (Cl-)", "text", 10, yPos, 12, false, false);
        addField(fields, "chloride_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "chloride_unit", "mmol/L", "text", 300, yPos, 12, false, false);
        addField(fields, "chloride_range", "98-107", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "bicarbonate_label", "Bicarbonate", "text", 10, yPos, 12, false, false);
        addField(fields, "bicarbonate_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "bicarbonate_unit", "mmol/L", "text", 300, yPos, 12, false, false);
        addField(fields, "bicarbonate_range", "22-29", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "calcium_label", "Calcium", "text", 10, yPos, 12, false, false);
        addField(fields, "calcium_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "calcium_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "calcium_range", "8.6-10.3", "text", 400, yPos, 12, false, false);
        
        yPos += 40;
        addField(fields, "note_label", "Interpretation:", "text", 10, yPos, 12, true, true);
        addField(fields, "note_value", "", "text", 10, yPos + 20, 12, false, false);
        
        // Signature
        addField(fields, "signature_line", "__________________________", "text", 400, 600, 12, false, false);
        addField(fields, "signature_name", "Medical Officer", "text", 400, 620, 12, true, false);
        
        return template;
    }
    
    public JsonNode createRenalProfileTemplate() {
        ObjectNode template = objectMapper.createObjectNode();
        template.put("paperSize", "A4");
        template.put("orientation", "portrait");
        
        ArrayNode fields = template.putArray("fields");
        
        // Header
        addField(fields, "clinic_header", "UNAWATUNE MEDICAL CENTER", "text", 10, 10, 16, true, false);
        addField(fields, "clinic_address", "Main Street, Unawatune", "text", 10, 30, 12, false, false);
        addField(fields, "clinic_contact", "Tel: +94 123 456 789", "text", 10, 50, 12, false, false);
        
        addField(fields, "report_title", "RENAL FUNCTION TEST", "text", 10, 90, 18, true, false);
        
        // Patient information
        addField(fields, "patient_name_label", "Patient Name:", "text", 10, 130, 12, true, true);
        addField(fields, "patient_name", "", "text", 120, 130, 12, false, false);
        
        addField(fields, "patient_id_label", "Patient ID:", "text", 350, 130, 12, true, true);
        addField(fields, "patient_id", "", "text", 430, 130, 12, false, false);
        
        addField(fields, "age_label", "Age:", "text", 10, 155, 12, true, true);
        addField(fields, "age", "", "text", 120, 155, 12, false, false);
        
        addField(fields, "gender_label", "Gender:", "text", 350, 155, 12, true, true);
        addField(fields, "gender", "", "text", 430, 155, 12, false, false);
        
        // Report metadata
        addField(fields, "report_date_label", "Report Date:", "text", 10, 180, 12, true, true);
        addField(fields, "report_date", "", "date", 120, 180, 12, false, false);
        
        addField(fields, "sample_date_label", "Sample Collected:", "text", 350, 180, 12, true, true);
        addField(fields, "sample_date", "", "date", 450, 180, 12, false, false);
        
        // Divider
        addField(fields, "divider", "--------------------------------------------------------------------------------------------------------------------------------------", "text", 10, 210, 12, false, false);
        
        // Renal Function Test Results
        int yPos = 250;
        addField(fields, "table_header_test", "Test", "text", 10, yPos, 12, true, false);
        addField(fields, "table_header_result", "Result", "text", 200, yPos, 12, true, false);
        addField(fields, "table_header_unit", "Unit", "text", 300, yPos, 12, true, false);
        addField(fields, "table_header_range", "Reference Range", "text", 400, yPos, 12, true, false);
        
        // Renal Parameters
        yPos += 30;
        addField(fields, "urea_label", "Blood Urea", "text", 10, yPos, 12, false, false);
        addField(fields, "urea_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "urea_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "urea_range", "7-20", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "bun_label", "BUN", "text", 10, yPos, 12, false, false);
        addField(fields, "bun_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "bun_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "bun_range", "7-20", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "creatinine_label", "Creatinine", "text", 10, yPos, 12, false, false);
        addField(fields, "creatinine_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "creatinine_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "creatinine_range", "M: 0.7-1.3, F: 0.6-1.1", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "uric_acid_label", "Uric Acid", "text", 10, yPos, 12, false, false);
        addField(fields, "uric_acid_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "uric_acid_unit", "mg/dL", "text", 300, yPos, 12, false, false);
        addField(fields, "uric_acid_range", "M: 3.4-7.0, F: 2.4-6.0", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "egfr_label", "eGFR", "text", 10, yPos, 12, false, false);
        addField(fields, "egfr_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "egfr_unit", "mL/min/1.73m²", "text", 300, yPos, 12, false, false);
        addField(fields, "egfr_range", ">90", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "sodium_label", "Sodium", "text", 10, yPos, 12, false, false);
        addField(fields, "sodium_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "sodium_unit", "mmol/L", "text", 300, yPos, 12, false, false);
        addField(fields, "sodium_range", "135-145", "text", 400, yPos, 12, false, false);
        
        yPos += 25;
        addField(fields, "potassium_label", "Potassium", "text", 10, yPos, 12, false, false);
        addField(fields, "potassium_value", "", "number", 200, yPos, 12, false, false);
        addField(fields, "potassium_unit", "mmol/L", "text", 300, yPos, 12, false, false);
        addField(fields, "potassium_range", "3.5-5.1", "text", 400, yPos, 12, false, false);
        
        yPos += 40;
        addField(fields, "note_label", "Interpretation:", "text", 10, yPos, 12, true, true);
        addField(fields, "note_value", "", "text", 10, yPos + 20, 12, false, false);
        
        // Signature
        addField(fields, "signature_line", "__________________________", "text", 400, 600, 12, false, false);
        addField(fields, "signature_name", "Medical Officer", "text", 400, 620, 12, true, false);
        
        return template;
    }
}
