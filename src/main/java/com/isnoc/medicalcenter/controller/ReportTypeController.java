package com.isnoc.medicalcenter.controller;

import com.isnoc.medicalcenter.dto.CreateReportTypeRequest;
import com.isnoc.medicalcenter.dto.ReportTypeDTO;
import com.isnoc.medicalcenter.dto.UpdateReportTypeRequest;
import com.isnoc.medicalcenter.entity.ReportType;
import com.isnoc.medicalcenter.service.ReportTypeService;
import com.isnoc.medicalcenter.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report-types")
public class ReportTypeController {

    private final ReportTypeService reportTypeService;

    @Autowired
    public ReportTypeController(ReportTypeService reportTypeService) {
        this.reportTypeService = reportTypeService;
    }

    @PostMapping
    public ResponseEntity<ReportTypeDTO> createReportType(@Valid @RequestBody CreateReportTypeRequest request) {
        ReportType reportType = reportTypeService.createReportType(
                request.getReportName(), 
                request.getDescription(), 
                request.getReportTemplate());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MapperUtils.mapReportTypeToDTO(reportType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportTypeDTO> getReportTypeById(@PathVariable("id") Long reportTypeId) {
        ReportType reportType = reportTypeService.getReportTypeById(reportTypeId);
        return ResponseEntity.ok(MapperUtils.mapReportTypeToDTO(reportType));
    }

    @GetMapping
    public ResponseEntity<List<ReportTypeDTO>> getAllReportTypes() {
        List<ReportType> reportTypes = reportTypeService.getAllReportTypes();
        List<ReportTypeDTO> reportTypeDTOs = reportTypes.stream()
                .map(MapperUtils::mapReportTypeToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(reportTypeDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportTypeDTO> updateReportType(
            @PathVariable("id") Long reportTypeId,
            @Valid @RequestBody UpdateReportTypeRequest request) {
        
        ReportType updatedReportType = reportTypeService.updateReportType(
                reportTypeId,
                request.getReportName(),
                request.getDescription(),
                request.getReportTemplate());
        
        return ResponseEntity.ok(MapperUtils.mapReportTypeToDTO(updatedReportType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportType(@PathVariable("id") Long reportTypeId) {
        reportTypeService.deleteReportType(reportTypeId);
        return ResponseEntity.noContent().build();
    }
}