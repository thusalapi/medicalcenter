package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPatientPatientIdOrderByVisitDateDesc(Long patientId); // Get patient history
}