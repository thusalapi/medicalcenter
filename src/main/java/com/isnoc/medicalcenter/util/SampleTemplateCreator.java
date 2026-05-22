package com.isnoc.medicalcenter.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility class for creating sample medical report templates
 */
@Component
public class SampleTemplateCreator {

    private final ObjectMapper objectMapper;

    @Autowired
    public SampleTemplateCreator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Creates a complete blood test report template with static content and dynamic fields
     */
    public JsonNode createBloodTestReportTemplate() {
        ObjectNode template = objectMapper.createObjectNode();

        // Static content - permanent text and layout
        ObjectNode staticContent = objectMapper.createObjectNode();
        ArrayNode staticElements = objectMapper.createArrayNode();

        // Header
        ObjectNode header = objectMapper.createObjectNode();
        header.put("id", "header-1");
        header.put("type", "heading");
        header.put("content", "MEDICAL CENTER LABORATORY");
        ObjectNode headerPosition = objectMapper.createObjectNode();
        headerPosition.put("x", 200);
        headerPosition.put("y", 30);
        header.set("position", headerPosition);
        ObjectNode headerSize = objectMapper.createObjectNode();
        headerSize.put("width", 400);
        headerSize.put("height", 40);
        header.set("size", headerSize);
        ObjectNode headerStyle = objectMapper.createObjectNode();
        headerStyle.put("fontSize", 24);
        headerStyle.put("fontWeight", "bold");
        headerStyle.put("textAlign", "center");
        headerStyle.put("color", "#1f2937");
        header.set("style", headerStyle);
        staticElements.add(header);

        // Report title
        ObjectNode title = objectMapper.createObjectNode();
        title.put("id", "title-1");
        title.put("type", "subheading");
        title.put("content", "BLOOD TEST REPORT");
        ObjectNode titlePosition = objectMapper.createObjectNode();
        titlePosition.put("x", 250);
        titlePosition.put("y", 80);
        title.set("position", titlePosition);
        ObjectNode titleSize = objectMapper.createObjectNode();
        titleSize.put("width", 300);
        titleSize.put("height", 30);
        title.set("size", titleSize);
        ObjectNode titleStyle = objectMapper.createObjectNode();
        titleStyle.put("fontSize", 18);
        titleStyle.put("fontWeight", "bold");
        titleStyle.put("textAlign", "center");
        titleStyle.put("color", "#374151");
        title.set("style", titleStyle);
        staticElements.add(title);

        // Patient Information Section
        ObjectNode patientSection = objectMapper.createObjectNode();
        patientSection.put("id", "patient-section");
        patientSection.put("type", "text");
        patientSection.put("content", "PATIENT INFORMATION");
        ObjectNode patientPosition = objectMapper.createObjectNode();
        patientPosition.put("x", 50);
        patientPosition.put("y", 140);
        patientSection.set("position", patientPosition);
        ObjectNode patientSize = objectMapper.createObjectNode();
        patientSize.put("width", 200);
        patientSize.put("height", 25);
        patientSection.set("size", patientSize);
        ObjectNode patientStyle = objectMapper.createObjectNode();
        patientStyle.put("fontSize", 14);
        patientStyle.put("fontWeight", "bold");
        patientStyle.put("textAlign", "left");
        patientStyle.put("color", "#4b5563");
        patientSection.set("style", patientStyle);
        staticElements.add(patientSection);

        // Test Results Section
        ObjectNode testSection = objectMapper.createObjectNode();
        testSection.put("id", "test-section");
        testSection.put("type", "text");
        testSection.put("content", "TEST RESULTS");
        ObjectNode testPosition = objectMapper.createObjectNode();
        testPosition.put("x", 50);
        testPosition.put("y", 300);
        testSection.set("position", testPosition);
        ObjectNode testSize = objectMapper.createObjectNode();
        testSize.put("width", 200);
        testSize.put("height", 25);
        testSection.set("size", testSize);
        ObjectNode testStyle = objectMapper.createObjectNode();
        testStyle.put("fontSize", 14);
        testStyle.put("fontWeight", "bold");
        testStyle.put("textAlign", "left");
        testStyle.put("color", "#4b5563");
        testSection.set("style", testStyle);
        staticElements.add(testSection);

        staticContent.set("elements", staticElements);

        // Dynamic fields - will be filled with database data
        ObjectNode dynamicFields = objectMapper.createObjectNode();
        ArrayNode fieldsArray = objectMapper.createArrayNode();

        // Patient Name
        ObjectNode patientNameField = objectMapper.createObjectNode();
        patientNameField.put("id", "patient-name");
        patientNameField.put("fieldName", "patient_name");
        patientNameField.put("fieldType", "text");
        patientNameField.put("label", "Patient Name");
        patientNameField.put("required", true);
        patientNameField.put("dbMapping", "patient_name");
        ObjectNode namePosition = objectMapper.createObjectNode();
        namePosition.put("x", 150);
        namePosition.put("y", 180);
        patientNameField.set("position", namePosition);
        ObjectNode nameSize = objectMapper.createObjectNode();
        nameSize.put("width", 200);
        nameSize.put("height", 25);
        patientNameField.set("size", nameSize);
        fieldsArray.add(patientNameField);

        // Patient ID
        ObjectNode patientIdField = objectMapper.createObjectNode();
        patientIdField.put("id", "patient-id");
        patientIdField.put("fieldName", "patient_id");
        patientIdField.put("fieldType", "text");
        patientIdField.put("label", "Patient ID");
        patientIdField.put("required", true);
        patientIdField.put("dbMapping", "patient_id");
        ObjectNode idPosition = objectMapper.createObjectNode();
        idPosition.put("x", 400);
        idPosition.put("y", 180);
        patientIdField.set("position", idPosition);
        ObjectNode idSize = objectMapper.createObjectNode();
        idSize.put("width", 150);
        idSize.put("height", 25);
        patientIdField.set("size", idSize);
        fieldsArray.add(patientIdField);

        // Patient Age
        ObjectNode ageField = objectMapper.createObjectNode();
        ageField.put("id", "patient-age");
        ageField.put("fieldName", "patient_age");
        ageField.put("fieldType", "number");
        ageField.put("label", "Age");
        ageField.put("required", true);
        ageField.put("dbMapping", "patient_age");
        ObjectNode agePosition = objectMapper.createObjectNode();
        agePosition.put("x", 150);
        agePosition.put("y", 210);
        ageField.set("position", agePosition);
        ObjectNode ageSize = objectMapper.createObjectNode();
        ageSize.put("width", 100);
        ageSize.put("height", 25);
        ageField.set("size", ageSize);
        fieldsArray.add(ageField);

        // Patient Gender
        ObjectNode genderField = objectMapper.createObjectNode();
        genderField.put("id", "patient-gender");
        genderField.put("fieldName", "patient_gender");
        genderField.put("fieldType", "text");
        genderField.put("label", "Gender");
        genderField.put("required", true);
        genderField.put("dbMapping", "patient_gender");
        ObjectNode genderPosition = objectMapper.createObjectNode();
        genderPosition.put("x", 300);
        genderPosition.put("y", 210);
        genderField.set("position", genderPosition);
        ObjectNode genderSize = objectMapper.createObjectNode();
        genderSize.put("width", 100);
        genderSize.put("height", 25);
        genderField.set("size", genderSize);
        fieldsArray.add(genderField);

        // Test Date
        ObjectNode testDateField = objectMapper.createObjectNode();
        testDateField.put("id", "test-date");
        testDateField.put("fieldName", "test_date");
        testDateField.put("fieldType", "date");
        testDateField.put("label", "Test Date");
        testDateField.put("required", true);
        testDateField.put("dbMapping", "test_date");
        ObjectNode testDatePosition = objectMapper.createObjectNode();
        testDatePosition.put("x", 450);
        testDatePosition.put("y", 210);
        testDateField.set("position", testDatePosition);
        ObjectNode testDateSize = objectMapper.createObjectNode();
        testDateSize.put("width", 150);
        testDateSize.put("height", 25);
        testDateField.set("size", testDateSize);
        fieldsArray.add(testDateField);

        // Hemoglobin
        ObjectNode hbField = objectMapper.createObjectNode();
        hbField.put("id", "hemoglobin");
        hbField.put("fieldName", "hemoglobin");
        hbField.put("fieldType", "number");
        hbField.put("label", "Hemoglobin (g/dL)");
        hbField.put("required", false);
        ObjectNode hbPosition = objectMapper.createObjectNode();
        hbPosition.put("x", 150);
        hbPosition.put("y", 340);
        hbField.set("position", hbPosition);
        ObjectNode hbSize = objectMapper.createObjectNode();
        hbSize.put("width", 120);
        hbSize.put("height", 25);
        hbField.set("size", hbSize);
        fieldsArray.add(hbField);

        // White Blood Cell Count
        ObjectNode wbcField = objectMapper.createObjectNode();
        wbcField.put("id", "wbc");
        wbcField.put("fieldName", "wbc_count");
        wbcField.put("fieldType", "number");
        wbcField.put("label", "WBC Count (/μL)");
        wbcField.put("required", false);
        ObjectNode wbcPosition = objectMapper.createObjectNode();
        wbcPosition.put("x", 300);
        wbcPosition.put("y", 340);
        wbcField.set("position", wbcPosition);
        ObjectNode wbcSize = objectMapper.createObjectNode();
        wbcSize.put("width", 120);
        wbcSize.put("height", 25);
        wbcField.set("size", wbcSize);
        fieldsArray.add(wbcField);

        // Platelet Count
        ObjectNode plateletField = objectMapper.createObjectNode();
        plateletField.put("id", "platelets");
        plateletField.put("fieldName", "platelet_count");
        plateletField.put("fieldType", "number");
        plateletField.put("label", "Platelets (/μL)");
        plateletField.put("required", false);
        ObjectNode plateletPosition = objectMapper.createObjectNode();
        plateletPosition.put("x", 450);
        plateletPosition.put("y", 340);
        plateletField.set("position", plateletPosition);
        ObjectNode plateletSize = objectMapper.createObjectNode();
        plateletSize.put("width", 120);
        plateletSize.put("height", 25);
        plateletField.set("size", plateletSize);
        fieldsArray.add(plateletField);

        // Blood Sugar
        ObjectNode sugarField = objectMapper.createObjectNode();
        sugarField.put("id", "blood-sugar");
        sugarField.put("fieldName", "blood_sugar");
        sugarField.put("fieldType", "number");
        sugarField.put("label", "Blood Sugar (mg/dL)");
        sugarField.put("required", false);
        ObjectNode sugarPosition = objectMapper.createObjectNode();
        sugarPosition.put("x", 150);
        sugarPosition.put("y", 380);
        sugarField.set("position", sugarPosition);
        ObjectNode sugarSize = objectMapper.createObjectNode();
        sugarSize.put("width", 120);
        sugarSize.put("height", 25);
        sugarField.set("size", sugarSize);
        fieldsArray.add(sugarField);

        // Cholesterol
        ObjectNode cholesterolField = objectMapper.createObjectNode();
        cholesterolField.put("id", "cholesterol");
        cholesterolField.put("fieldName", "cholesterol");
        cholesterolField.put("fieldType", "number");
        cholesterolField.put("label", "Cholesterol (mg/dL)");
        cholesterolField.put("required", false);
        ObjectNode cholesterolPosition = objectMapper.createObjectNode();
        cholesterolPosition.put("x", 300);
        cholesterolPosition.put("y", 380);
        cholesterolField.set("position", cholesterolPosition);
        ObjectNode cholesterolSize = objectMapper.createObjectNode();
        cholesterolSize.put("width", 120);
        cholesterolSize.put("height", 25);
        cholesterolField.set("size", cholesterolSize);
        fieldsArray.add(cholesterolField);

        // Doctor Notes
        ObjectNode notesField = objectMapper.createObjectNode();
        notesField.put("id", "doctor-notes");
        notesField.put("fieldName", "doctor_notes");
        notesField.put("fieldType", "textarea");
        notesField.put("label", "Doctor's Notes");
        notesField.put("required", false);
        ObjectNode notesPosition = objectMapper.createObjectNode();
        notesPosition.put("x", 50);
        notesPosition.put("y", 450);
        notesField.set("position", notesPosition);
        ObjectNode notesSize = objectMapper.createObjectNode();
        notesSize.put("width", 500);
        notesSize.put("height", 80);
        notesField.set("size", notesSize);
        fieldsArray.add(notesField);

        dynamicFields.set("fields", fieldsArray);

        // Layout configuration
        ObjectNode layoutConfig = objectMapper.createObjectNode();
        layoutConfig.put("paperSize", "A4");
        layoutConfig.put("orientation", "portrait");
        ObjectNode margins = objectMapper.createObjectNode();
        margins.put("top", 50);
        margins.put("right", 50);
        margins.put("bottom", 50);
        margins.put("left", 50);
        layoutConfig.set("margins", margins);

        // Assemble the complete template
        template.set("staticContent", staticContent);
        template.set("dynamicFields", dynamicFields);
        template.set("layoutConfig", layoutConfig);

        return template;
    }

    /**
     * Creates a urine test report template
     */
    public JsonNode createUrineTestReportTemplate() {
        ObjectNode template = objectMapper.createObjectNode();

        // Similar structure to blood test but with urine-specific fields
        ObjectNode staticContent = objectMapper.createObjectNode();
        ArrayNode staticElements = objectMapper.createArrayNode();

        // Header
        ObjectNode header = objectMapper.createObjectNode();
        header.put("id", "header-1");
        header.put("type", "heading");
        header.put("content", "URINE ANALYSIS REPORT");
        ObjectNode headerPosition = objectMapper.createObjectNode();
        headerPosition.put("x", 200);
        headerPosition.put("y", 30);
        header.set("position", headerPosition);
        ObjectNode headerSize = objectMapper.createObjectNode();
        headerSize.put("width", 400);
        headerSize.put("height", 40);
        header.set("size", headerSize);
        ObjectNode headerStyle = objectMapper.createObjectNode();
        headerStyle.put("fontSize", 24);
        headerStyle.put("fontWeight", "bold");
        headerStyle.put("textAlign", "center");
        headerStyle.put("color", "#1f2937");
        header.set("style", headerStyle);
        staticElements.add(header);

        staticContent.set("elements", staticElements);

        // Dynamic fields for urine test
        ObjectNode dynamicFields = objectMapper.createObjectNode();
        ArrayNode fieldsArray = objectMapper.createArrayNode();

        // Add patient information fields (similar to blood test)
        // Add urine-specific test fields
        ObjectNode proteinField = objectMapper.createObjectNode();
        proteinField.put("id", "protein");
        proteinField.put("fieldName", "protein");
        proteinField.put("fieldType", "text");
        proteinField.put("label", "Protein");
        proteinField.put("required", false);
        ObjectNode proteinPosition = objectMapper.createObjectNode();
        proteinPosition.put("x", 150);
        proteinPosition.put("y", 300);
        proteinField.set("position", proteinPosition);
        ObjectNode proteinSize = objectMapper.createObjectNode();
        proteinSize.put("width", 120);
        proteinSize.put("height", 25);
        proteinField.set("size", proteinSize);
        fieldsArray.add(proteinField);

        // Add more urine-specific fields here...

        dynamicFields.set("fields", fieldsArray);

        // Layout configuration
        ObjectNode layoutConfig = objectMapper.createObjectNode();
        layoutConfig.put("paperSize", "A4");
        layoutConfig.put("orientation", "portrait");

        template.set("staticContent", staticContent);
        template.set("dynamicFields", dynamicFields);
        template.set("layoutConfig", layoutConfig);

        return template;
    }
}
