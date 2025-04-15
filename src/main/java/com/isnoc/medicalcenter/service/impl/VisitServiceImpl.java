package com.isnoc.medicalcenter.service.impl;

import lombok.RequiredArgsConstructor;
import com.isnoc.medicalcenter.entity.Patient;
import com.isnoc.medicalcenter.entity.Visit;
import com.isnoc.medicalcenter.exception.ResourceNotFoundException;
import com.isnoc.medicalcenter.repository.PatientRepository;
import com.isnoc.medicalcenter.repository.VisitRepository;
import com.isnoc.medicalcenter.service.VisitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository; // Inject PatientRepository

    @Override
    @Transactional
    public Visit createVisit(Long patientId, LocalDateTime visitDate) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        Visit visit = new Visit();
        visit.setPatient(patient);
        if (visitDate != null) {
            visit.setVisitDate(visitDate);
        } // else it defaults to now()

        return visitRepository.save(visit);
    }

    @Override
    @Transactional(readOnly = true)
    public Visit getVisitById(Long visitId) {
        // Fetch associated entities if needed often, e.g., patient name
        return visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));
        // Consider using findByIdWithPatient to avoid lazy loading issues in DTO mapping
    }

    @Override
    @Transactional(readOnly = true)
    public List<Visit> getVisitsByPatientId(Long patientId) {
        // Ensure patient exists first? Optional.
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        return visitRepository.findByPatientPatientIdOrderByVisitDateDesc(patientId);
    }
}