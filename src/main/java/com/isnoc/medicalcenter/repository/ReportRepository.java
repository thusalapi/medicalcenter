package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByVisitVisitId(Long visitId);
}
