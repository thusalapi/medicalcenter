package com.isnoc.medicalcenter.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.dto.CreateReportTemplateRequest;
import com.isnoc.medicalcenter.dto.UpdateReportTemplateRequest;
import com.isnoc.medicalcenter.dto.ReportTemplateDTO;
import com.isnoc.medicalcenter.dto.GenerateReportRequest;
import com.isnoc.medicalcenter.entity.ReportTemplate;
import com.isnoc.medicalcenter.service.ReportTemplateService;
import com.isnoc.medicalcenter.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/report-templates")
public class ReportTemplateController {

    private final ReportTemplateService templateService;

    @Autowired
    public ReportTemplateController(ReportTemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping
    public ResponseEntity<ReportTemplateDTO> createTemplate(@Valid @RequestBody CreateReportTemplateRequest request) {
        ReportTemplate template = templateService.createTemplate(
                request.getTemplateName(),
                request.getDescription(),
                request.getCategory(),
                request.getStaticContent(),
                request.getDynamicFields(),
                request.getLayoutConfig()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MapperUtils.mapReportTemplateToDTO(template));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportTemplateDTO> getTemplateById(@PathVariable("id") Long templateId) {
        ReportTemplate template = templateService.getTemplateById(templateId);
        return ResponseEntity.ok(MapperUtils.mapReportTemplateToDTO(template));
    }

    @GetMapping
    public ResponseEntity<List<ReportTemplateDTO>> getAllTemplates() {
        List<ReportTemplate> templates = templateService.getAllActiveTemplates();
        List<ReportTemplateDTO> templateDTOs = templates.stream()
                .map(MapperUtils::mapReportTemplateToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(templateDTOs);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ReportTemplateDTO>> getTemplatesByCategory(@PathVariable String category) {
        List<ReportTemplate> templates = templateService.getTemplatesByCategory(category);
        List<ReportTemplateDTO> templateDTOs = templates.stream()
                .map(MapperUtils::mapReportTemplateToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(templateDTOs);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = templateService.getAvailableCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportTemplateDTO> updateTemplate(
            @PathVariable("id") Long templateId,
            @Valid @RequestBody UpdateReportTemplateRequest request) {
        
        ReportTemplate updatedTemplate = templateService.updateTemplate(
                templateId,
                request.getTemplateName(),
                request.getDescription(),
                request.getCategory(),
                request.getStaticContent(),
                request.getDynamicFields(),
                request.getLayoutConfig()
        );
        
        return ResponseEntity.ok(MapperUtils.mapReportTemplateToDTO(updatedTemplate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateTemplate(@PathVariable("id") Long templateId) {
        templateService.deactivateTemplate(templateId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/fields")
    public ResponseEntity<List<String>> getTemplateFields(@PathVariable("id") Long templateId) {
        List<String> fields = templateService.extractDynamicFieldsFromTemplate(templateId);
        return ResponseEntity.ok(fields);
    }

    @PostMapping("/{id}/generate")
    public ResponseEntity<String> generateReport(
            @PathVariable("id") Long templateId,
            @RequestBody GenerateReportRequest request) {
        
        String reportContent = templateService.generateReportFromTemplate(templateId, request.getFieldValues());
        return ResponseEntity.ok(reportContent);
    }

    @PostMapping("/{id}/generate/pdf")
    public ResponseEntity<byte[]> generateReportPdf(
            @PathVariable("id") Long templateId,
            @RequestBody GenerateReportRequest request) {
        
        byte[] pdfBytes = templateService.generateReportPdfFromTemplate(templateId, request.getFieldValues());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "report_" + templateId + ".pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Map<String, Object>> validateTemplate(
            @PathVariable("id") Long templateId,
            @RequestBody JsonNode validationData) {
        
        JsonNode staticContent = validationData.get("staticContent");
        JsonNode dynamicFields = validationData.get("dynamicFields");
        
        boolean isValid = templateService.validateTemplateStructure(staticContent, dynamicFields);
        
        return ResponseEntity.ok(Map.of(
                "valid", isValid,
                "templateId", templateId
        ));
    }
}
