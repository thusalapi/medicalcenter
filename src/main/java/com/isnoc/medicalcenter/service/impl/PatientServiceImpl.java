package com.isnoc.medicalcenter.service.impl;


import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import com.isnoc.medicalcenter.entity.Patient;
import com.isnoc.medicalcenter.exception.ResourceNotFoundException;
import com.isnoc.medicalcenter.repository.PatientRepository;
import com.isnoc.medicalcenter.service.PatientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor // Injects repositories via constructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> findPatientByPhoneNumber(String phoneNumber) {
        return patientRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    @Transactional
    public Patient createPatient(String phoneNumber, String name, JsonNode otherDetails) {
        // Optional: Add check if phone number already exists
        if(patientRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            // Handle duplicate phone number case - throw exception or return existing?
            // For now, let's assume UI prevents duplicates or we allow lookup first
            throw new IllegalArgumentException("Patient with phone number " + phoneNumber + " already exists.");
        }
        Patient patient = new Patient();
        patient.setPhoneNumber(phoneNumber);
        patient.setName(name);
        patient.setOtherDetails(otherDetails);
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    public Patient updatePatient(Long patientId, String name, JsonNode otherDetails) {
        Patient patient = getPatientById(patientId); // Reuse getById which throws exception if not found
        patient.setName(name); // Update fields
        patient.setOtherDetails(otherDetails);
        return patientRepository.save(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
    }
}