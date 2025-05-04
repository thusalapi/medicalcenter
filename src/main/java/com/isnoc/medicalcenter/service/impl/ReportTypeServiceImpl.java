package com.isnoc.medicalcenter.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.entity.ReportType;
import com.isnoc.medicalcenter.exception.DuplicateResourceException;
import com.isnoc.medicalcenter.exception.ResourceNotFoundException;
import com.isnoc.medicalcenter.repository.ReportTypeRepository;
import com.isnoc.medicalcenter.service.ReportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportTypeServiceImpl implements ReportTypeService {

    private final ReportTypeRepository reportTypeRepository;

    @Autowired
    public ReportTypeServiceImpl(ReportTypeRepository reportTypeRepository) {
        this.reportTypeRepository = reportTypeRepository;
    }

    @Override
    public List<ReportType> getAllReportTypes() {
        return reportTypeRepository.findAll();
    }

    @Override
    public ReportType getReportTypeById(Long reportTypeId) {
        return reportTypeRepository.findById(reportTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Report type not found with id: " + reportTypeId));
    }

    @Override
    public Optional<ReportType> findByReportName(String reportName) {
        return reportTypeRepository.findByReportName(reportName);
    }

    @Override
    @Transactional
    public ReportType createReportType(String reportName, String description, JsonNode reportTemplate) {
        // Check if report type with the same name already exists
        reportTypeRepository.findByReportName(reportName).ifPresent(existingType -> {
            throw new DuplicateResourceException("Report type with name '" + reportName + "' already exists");
        });

        ReportType reportType = new ReportType();
        reportType.setReportName(reportName);
        reportType.setDescription(description);
        reportType.setReportTemplate(reportTemplate);
        reportType.setCreatedDate(LocalDateTime.now());
        reportType.setLastModifiedDate(LocalDateTime.now());

        return reportTypeRepository.save(reportType);
    }

    @Override
    @Transactional
    public ReportType updateReportType(Long reportTypeId, String reportName, String description, JsonNode reportTemplate) {
        ReportType reportType = getReportTypeById(reportTypeId);

        // Check if another report type with the same name exists (excluding current one)
        reportTypeRepository.findByReportName(reportName)
                .ifPresent(existingType -> {
                    if (!existingType.getReportTypeId().equals(reportTypeId)) {
                        throw new DuplicateResourceException("Report type with name '" + reportName + "' already exists");
                    }
                });

        reportType.setReportName(reportName);
        reportType.setDescription(description);
        reportType.setReportTemplate(reportTemplate);
        reportType.setLastModifiedDate(LocalDateTime.now());

        return reportTypeRepository.save(reportType);
    }

    @Override
    @Transactional
    public void deleteReportType(Long reportTypeId) {
        // Check if the report type exists first
        ReportType reportType = getReportTypeById(reportTypeId);
        reportTypeRepository.delete(reportType);
    }
}