package com.isnoc.medicalcenter.controller;

import com.isnoc.medicalcenter.dto.CreateReportRequest;
import com.isnoc.medicalcenter.dto.CreateVisitRequest;
import com.isnoc.medicalcenter.dto.ReportDTO;
import com.isnoc.medicalcenter.dto.StatisticsDTO;
import com.isnoc.medicalcenter.dto.VisitDTO;
import com.isnoc.medicalcenter.entity.Report;
import com.isnoc.medicalcenter.entity.Visit;
import com.isnoc.medicalcenter.service.ReportService;
import com.isnoc.medicalcenter.service.StatisticsService;
import com.isnoc.medicalcenter.service.VisitService;
import com.isnoc.medicalcenter.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;
    private final ReportService reportService;
    private final StatisticsService statisticsService;

    @Autowired
    public VisitController(VisitService visitService, ReportService reportService, StatisticsService statisticsService) {
        this.visitService = visitService;
        this.reportService = reportService;
        this.statisticsService = statisticsService;
    }

    @PostMapping
    public ResponseEntity<VisitDTO> createVisit(@Valid @RequestBody CreateVisitRequest request) {
        Visit visit = visitService.createVisit(request.getPatientId(), request.getVisitDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtils.mapVisitToDTO(visit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDTO> getVisitById(@PathVariable("id") Long visitId) {
        Visit visit = visitService.getVisitById(visitId);
        return ResponseEntity.ok(MapperUtils.mapVisitToDTO(visit));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<VisitDTO>> getVisitsForPatient(@PathVariable Long patientId) {
        List<Visit> visits = visitService.getVisitHistoryForPatient(patientId);
        List<VisitDTO> visitDTOs = visits.stream()
                .map(MapperUtils::mapVisitToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(visitDTOs);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<VisitDTO>> getRecentVisits(
            @RequestParam(value = "limit", defaultValue = "5") int limit) {
        List<Visit> visits = visitService.getRecentVisits(limit);
        List<VisitDTO> visitDTOs = visits.stream()
                .map(MapperUtils::mapVisitToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(visitDTOs);
    }

    @PostMapping("/{visitId}/reports")
    public ResponseEntity<ReportDTO> addReportToVisit(
            @PathVariable Long visitId,
            @Valid @RequestBody CreateReportRequest request) {
        Report report = visitService.addReportToVisit(visitId, request.getReportTypeId(), request.getReportData());
        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtils.mapReportToDTO(report));
    }

    @PostMapping("/{visitId}/reports/from-template/{templateId}")
    public ResponseEntity<ReportDTO> createReportFromTemplate(
            @PathVariable Long visitId,
            @PathVariable Long templateId,
            @RequestBody Map<String, Object> fieldValues) {
        Report report = visitService.createReportFromTemplate(visitId, templateId, fieldValues);
        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtils.mapReportToDTO(report));
    }

    @GetMapping("/{visitId}/reports")
    public ResponseEntity<List<ReportDTO>> getReportsForVisit(@PathVariable Long visitId) {
        List<Report> reports = visitService.getReportsForVisit(visitId);
        List<ReportDTO> reportDTOs = reports.stream()
                .map(MapperUtils::mapReportToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reportDTOs);    }    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVisits(
            @RequestParam(value = "search", required = false) String searchTerm,
            @RequestParam(value = "dateFrom", required = false) String dateFrom,
            @RequestParam(value = "dateTo", required = false) String dateTo,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        
        // Get all visits with patient data eagerly fetched to avoid LazyInitializationException
        List<Visit> allVisits = visitService.getAllVisitsWithPatients();
        
        // Apply search filter if provided
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            allVisits = allVisits.stream()
                .filter(visit -> visit.getPatient().getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                               visit.getVisitId().toString().contains(searchTerm))
                .collect(Collectors.toList());
        }
        
        // Calculate pagination
        int totalVisits = allVisits.size();
        int startIndex = (page - 1) * limit;
        int endIndex = Math.min(startIndex + limit, totalVisits);
        
        List<Visit> paginatedVisits = allVisits.subList(startIndex, endIndex);
        List<VisitDTO> visitDTOs = paginatedVisits.stream()
                .map(MapperUtils::mapVisitToDTO)
                .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("visits", visitDTOs);
        response.put("total", totalVisits);
        response.put("page", page);
        response.put("limit", limit);
        response.put("totalPages", (int) Math.ceil((double) totalVisits / limit));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getVisitStatistics() {
        StatisticsDTO stats = statisticsService.getVisitStatistics();
        Map<String, Object> response = new HashMap<>();
        response.put("total", stats.getTotalVisits());
        response.put("today", stats.getVisitsToday());
        return ResponseEntity.ok(response);
    }
}