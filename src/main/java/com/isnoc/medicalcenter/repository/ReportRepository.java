package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    /**
     * Find all reports for a specific patient
     * 
     * @param patientId ID of the patient
     * @return List of reports for the patient
     */
    @Query("SELECT r FROM Report r WHERE r.visit.patient.patientId = :patientId")
    List<Report> findByPatientId(@Param("patientId") Long patientId);
    
    /**
     * Find all reports for a specific visit
     * 
     * @param visitId ID of the visit
     * @return List of reports for the visit
     */
    @Query("SELECT r FROM Report r WHERE r.visit.visitId = :visitId")
    List<Report> findByVisitId(@Param("visitId") Long visitId);
    
    /**
     * Find all reports for a specific visit by visit's ID
     * 
     * @param visitId ID of the visit
     * @return List of reports for the visit
     */
    List<Report> findByVisitVisitId(Long visitId);
    
    /**
     * Find all reports for a specific report type
     * 
     * @param reportTypeId ID of the report type
     * @return List of reports of the specified type
     */
    List<Report> findByReportTypeReportTypeId(Long reportTypeId);
    
    /**
     * Find reports by multiple visit IDs
     * 
     * @param visitIds List of visit IDs
     * @return List of reports for the specified visits
     */
    @Query("SELECT r FROM Report r WHERE r.visit.visitId IN :visitIds")
    List<Report> findByVisitIdIn(@Param("visitIds") List<Long> visitIds);
    
    /**
     * Count reports created after a specific date
     * 
     * @param date The date after which to count reports
     * @return Count of reports
     */
    Long countByCreatedDateAfter(LocalDateTime date);
}
