package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReportTypeRepository extends JpaRepository<ReportType, Long> {
    Optional<ReportType> findByReportName(String reportName);
}