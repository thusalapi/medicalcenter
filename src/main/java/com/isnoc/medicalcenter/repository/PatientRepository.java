package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPhoneNumber(String phoneNumber);
    List<Patient> findByNameContainingIgnoreCaseOrPhoneNumberContaining(String name, String phone);
}