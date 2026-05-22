package com.isnoc.medicalcenter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.entity.Report;
import com.isnoc.medicalcenter.entity.Visit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface VisitService {
    Visit createVisit(Long patientId, LocalDateTime visitDate);
    Visit getVisitById(Long visitId);
    List<Visit> getVisitHistoryForPatient(Long patientId);
    List<Visit> getRecentVisits(int limit);
    List<Visit> getAllVisitsWithPatients(); // New method for fetching all visits with patients
    Report addReportToVisit(Long visitId, Long reportTypeId, JsonNode reportData);
    Report createReportFromTemplate(Long visitId, Long templateId, Map<String, Object> fieldValues);
    List<Report> getReportsForVisit(Long visitId);
}