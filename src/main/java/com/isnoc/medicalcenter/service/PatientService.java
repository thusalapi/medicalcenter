package com.isnoc.medicalcenter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.entity.Patient;

import java.util.Optional;

public interface PatientService {
    Optional<Patient> findPatientByPhoneNumber(String phoneNumber);
    Patient createPatient(String phoneNumber, String name, JsonNode otherDetails);
    Patient updatePatient(Long patientId, String name, JsonNode otherDetails); // Phone number likely shouldn't be updated easily
    Patient getPatientById(Long patientId); // Helper
}