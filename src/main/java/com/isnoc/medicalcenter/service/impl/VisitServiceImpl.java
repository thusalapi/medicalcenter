package com.isnoc.medicalcenter.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.entity.Patient;
import com.isnoc.medicalcenter.entity.Report;
import com.isnoc.medicalcenter.entity.ReportType;
import com.isnoc.medicalcenter.entity.Visit;
import com.isnoc.medicalcenter.exception.ResourceNotFoundException;
import com.isnoc.medicalcenter.repository.PatientRepository;
import com.isnoc.medicalcenter.repository.ReportRepository;
import com.isnoc.medicalcenter.repository.ReportTypeRepository;
import com.isnoc.medicalcenter.repository.VisitRepository;
import com.isnoc.medicalcenter.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final ReportRepository reportRepository;
    private final ReportTypeRepository reportTypeRepository;

    @Autowired
    public VisitServiceImpl(
            VisitRepository visitRepository,
            PatientRepository patientRepository,
            ReportRepository reportRepository,
            ReportTypeRepository reportTypeRepository) {
        this.visitRepository = visitRepository;
        this.patientRepository = patientRepository;
        this.reportRepository = reportRepository;
        this.reportTypeRepository = reportTypeRepository;
    }

    @Override
    @Transactional
    public Visit createVisit(Long patientId, LocalDateTime visitDate) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        
        Visit visit = new Visit();
        visit.setPatient(patient);
        visit.setVisitDate(visitDate != null ? visitDate : LocalDateTime.now());
        
        return visitRepository.save(visit);
    }

    @Override
    public Visit getVisitById(Long visitId) {
        return visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));
    }

    @Override
    public List<Visit> getVisitHistoryForPatient(Long patientId) {
        // Verify patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        
        return visitRepository.findByPatientPatientIdOrderByVisitDateDesc(patientId);
    }

    @Override
    public List<Visit> getRecentVisits(int limit) {
        return visitRepository.findAll(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "visitDate"))
        ).getContent();
    }

    @Override
    @Transactional
    public Report addReportToVisit(Long visitId, Long reportTypeId, JsonNode reportData) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));
                
        ReportType reportType = reportTypeRepository.findById(reportTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Report type not found with id: " + reportTypeId));
        
        Report report = new Report();
        report.setVisit(visit);
        report.setReportType(reportType);
        report.setReportData(reportData);
        report.setCreatedDate(LocalDateTime.now());
        report.setLastModifiedDate(LocalDateTime.now());
        
        // Save the report
        Report savedReport = reportRepository.save(report);
        
        // Add to visit's report list (optional if you want to maintain the bidirectional relationship)
        visit.getReports().add(savedReport);
        
        return savedReport;
    }

    @Override
    public List<Report> getReportsForVisit(Long visitId) {
        if (!visitRepository.existsById(visitId)) {
            throw new ResourceNotFoundException("Visit not found with id: " + visitId);
        }
        
        return reportRepository.findByVisitVisitId(visitId);
    }
}