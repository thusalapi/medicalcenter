package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPatientPatientIdOrderByVisitDateDesc(Long patientId);
}