package com.isnoc.medicalcenter.service;


import com.isnoc.medicalcenter.entity.Visit;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitService {
    Visit createVisit(Long patientId, LocalDateTime visitDate);
    Visit getVisitById(Long visitId);
    List<Visit> getVisitsByPatientId(Long patientId);
    // Update/Delete methods if needed
}