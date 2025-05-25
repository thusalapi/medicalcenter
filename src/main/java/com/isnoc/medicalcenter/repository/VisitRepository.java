package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.Visit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    @Query("SELECT v FROM Visit v LEFT JOIN FETCH v.patient WHERE v.patient.patientId = :patientId ORDER BY v.visitDate DESC")
    List<Visit> findByPatientPatientIdOrderByVisitDateDesc(@Param("patientId") Long patientId);
    
    // Find recent visits for dashboard
    @Query("SELECT v FROM Visit v ORDER BY v.visitDate DESC")
    List<Visit> findRecentVisits(Pageable pageable);
    
    // Find recent visits with patient data eagerly fetched
    @Query("SELECT v FROM Visit v LEFT JOIN FETCH v.patient ORDER BY v.visitDate DESC")
    List<Visit> findRecentVisitsWithPatient(Pageable pageable);
    
    // Find visits with patient data eagerly fetched for search and filtering
    @Query("SELECT v FROM Visit v LEFT JOIN FETCH v.patient ORDER BY v.visitDate DESC")
    List<Visit> findAllWithPatient();
    
    // Find a specific visit with patient data eagerly fetched
    @Query("SELECT v FROM Visit v LEFT JOIN FETCH v.patient WHERE v.visitId = :visitId")
    java.util.Optional<Visit> findByIdWithPatient(@Param("visitId") Long visitId);
    
    // Count visits for today
    Long countByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}