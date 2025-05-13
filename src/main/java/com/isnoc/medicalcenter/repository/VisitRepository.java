package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.Visit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPatientPatientIdOrderByVisitDateDesc(Long patientId);
    
    // Find recent visits for dashboard
    @Query("SELECT v FROM Visit v ORDER BY v.visitDate DESC")
    List<Visit> findRecentVisits(Pageable pageable);
    
    // Count visits for today
    Long countByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}