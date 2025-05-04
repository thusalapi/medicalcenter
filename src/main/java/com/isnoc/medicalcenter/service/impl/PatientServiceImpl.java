package com.isnoc.medicalcenter.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.entity.Patient;
import com.isnoc.medicalcenter.exception.ResourceNotFoundException;
import com.isnoc.medicalcenter.repository.PatientRepository;
import com.isnoc.medicalcenter.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Optional<Patient> findPatientByPhoneNumber(String phoneNumber) {
        return patientRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    @Transactional
    public Patient createPatient(String phoneNumber, String name, JsonNode otherDetails) {
        // Check if patient already exists with this phone number
        if (patientRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new IllegalArgumentException("Patient with phone number " + phoneNumber + " already exists");
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
        Patient patient = getPatientById(patientId);
        
        // Update fields
        if (name != null) {
            patient.setName(name);
        }
        
        if (otherDetails != null) {
            patient.setOtherDetails(otherDetails);
        }
        
        return patientRepository.save(patient);
    }

    @Override
    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
    }
}