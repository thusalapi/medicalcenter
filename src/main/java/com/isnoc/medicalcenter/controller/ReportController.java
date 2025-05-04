package com.isnoc.medicalcenter.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.dto.ReportDTO;
import com.isnoc.medicalcenter.entity.Report;
import com.isnoc.medicalcenter.service.ReportService;
import com.isnoc.medicalcenter.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable("id") Long reportId) {
        Report report = reportService.getReportById(reportId);
        return ResponseEntity.ok(MapperUtils.mapReportToDTO(report));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> updateReport(
            @PathVariable("id") Long reportId,
            @RequestBody JsonNode reportData) {
        Report updatedReport = reportService.updateReport(reportId, reportData);
        return ResponseEntity.ok(MapperUtils.mapReportToDTO(updatedReport));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable("id") Long reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ReportDTO>> getReportsForPatient(@PathVariable Long patientId) {
        List<Report> reports = reportService.getReportsForPatient(patientId);
        List<ReportDTO> reportDTOs = reports.stream()
                .map(MapperUtils::mapReportToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reportDTOs);
    }

    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReportPdf(@PathVariable("id") Long reportId) throws IOException {
        Report report = reportService.getReportById(reportId);
        byte[] pdfBytes = reportService.generateReportPdf(reportId);
        
        String filename = "Report_" + reportId + "_" + report.getReportType().getReportName().replaceAll("\\s+", "_") + ".pdf";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}