package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.ReportTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReportTemplateRepository extends JpaRepository<ReportTemplate, Long> {
    
    List<ReportTemplate> findByIsActiveTrue();
    
    List<ReportTemplate> findByCategoryAndIsActiveTrue(String category);
    
    Optional<ReportTemplate> findByTemplateNameAndIsActiveTrue(String templateName);
    
    @Query("SELECT rt FROM ReportTemplate rt WHERE rt.templateName LIKE %:name% AND rt.isActive = true")
    List<ReportTemplate> findByTemplateNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String name);
    
    @Query("SELECT DISTINCT rt.category FROM ReportTemplate rt WHERE rt.isActive = true ORDER BY rt.category")
    List<String> findDistinctCategories();
}
