package com.isnoc.medicalcenter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> getAllPatients(String search);
    Optional<Patient> findPatientByPhoneNumber(String phoneNumber);
    Patient createPatient(String phoneNumber, String name, JsonNode otherDetails);
    Patient updatePatient(Long patientId, String name, JsonNode otherDetails);
    Patient getPatientById(Long patientId);
}