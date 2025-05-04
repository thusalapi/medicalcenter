package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType, Long> {
    /**
     * Find a report type by its name
     * 
     * @param reportName the name of the report type to find
     * @return Optional containing the ReportType if found
     */
    Optional<ReportType> findByReportName(String reportName);
}