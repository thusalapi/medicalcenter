package com.isnoc.medicalcenter.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.entity.Report;
import com.isnoc.medicalcenter.exception.ResourceNotFoundException;
import com.isnoc.medicalcenter.repository.ReportRepository;
import com.isnoc.medicalcenter.service.ReportService;
import com.isnoc.medicalcenter.util.PdfGenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PdfGenerationUtil pdfGenerationUtil;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, PdfGenerationUtil pdfGenerationUtil) {
        this.reportRepository = reportRepository;
        this.pdfGenerationUtil = pdfGenerationUtil;
    }

    @Override
    public Report getReportById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
    }

    @Override
    @Transactional
    public Report updateReport(Long reportId, JsonNode reportData) {
        Report report = getReportById(reportId);
        report.setReportData(reportData);
        report.setLastModifiedDate(LocalDateTime.now());
        return reportRepository.save(report);
    }

    @Override
    @Transactional
    public void deleteReport(Long reportId) {
        Report report = getReportById(reportId);
        reportRepository.delete(report);
    }

    @Override
    public List<Report> getReportsForPatient(Long patientId) {
        return reportRepository.findByPatientId(patientId);
    }

    @Override
    public byte[] generateReportPdf(Long reportId) throws IOException {
        Report report = getReportById(reportId);
        return pdfGenerationUtil.generateReportPdf(report);
    }
}