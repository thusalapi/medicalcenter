package com.isnoc.medicalcenter.controller;

import com.isnoc.medicalcenter.dto.CreatePatientRequest;
import com.isnoc.medicalcenter.dto.PatientDTO;
import com.isnoc.medicalcenter.dto.UpdatePatientRequest;
import com.isnoc.medicalcenter.entity.Patient;
import com.isnoc.medicalcenter.service.PatientService;
import com.isnoc.medicalcenter.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/lookup")
    public ResponseEntity<PatientDTO> lookupPatient(@RequestParam String phoneNumber) {
        Optional<Patient> patient = patientService.findPatientByPhoneNumber(phoneNumber);
        return patient.map(p -> ResponseEntity.ok(MapperUtils.mapPatientToDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody CreatePatientRequest request) {
        Patient patient = patientService.createPatient(
                request.getPhoneNumber(),
                request.getName(),
                request.getOtherDetails()
        );
        return new ResponseEntity<>(MapperUtils.mapPatientToDTO(patient), HttpStatus.CREATED);
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<PatientDTO> updatePatient(
            @PathVariable Long patientId,
            @RequestBody UpdatePatientRequest request) {
        Patient updatedPatient = patientService.updatePatient(
                patientId,
                request.getName(),
                request.getOtherDetails()
        );
        return ResponseEntity.ok(MapperUtils.mapPatientToDTO(updatedPatient));
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        return ResponseEntity.ok(MapperUtils.mapPatientToDTO(patient));
    }
}
