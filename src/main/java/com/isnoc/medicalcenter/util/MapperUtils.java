package com.isnoc.medicalcenter.util;


import com.isnoc.medicalcenter.dto.BillDTO;
import com.isnoc.medicalcenter.dto.BillItemDTO;
import com.isnoc.medicalcenter.dto.PatientDTO;
import com.isnoc.medicalcenter.dto.ReportDTO;
import com.isnoc.medicalcenter.dto.ReportTypeDTO;
import com.isnoc.medicalcenter.dto.VisitDTO;
import com.isnoc.medicalcenter.entity.Bill;
import com.isnoc.medicalcenter.entity.BillItem;
import com.isnoc.medicalcenter.entity.Patient;
import com.isnoc.medicalcenter.entity.Report;
import com.isnoc.medicalcenter.entity.ReportType;
import com.isnoc.medicalcenter.entity.Visit;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component // Make it a Spring bean so it can be injected
public class MapperUtils {

    // Patient Mappers
    public PatientDTO toPatientDTO(Patient patient) {
        if (patient == null) return null;
        return new PatientDTO(
                patient.getPatientId(),
                patient.getPhoneNumber(),
                patient.getName(),
                patient.getOtherDetails()
        );
    }

    // Visit Mappers
    public VisitDTO toVisitDTO(Visit visit) {
        if (visit == null) return null;
        return new VisitDTO(
                visit.getVisitId(),
                visit.getPatient().getPatientId(), // Assuming patient is fetched or available
                visit.getPatient().getName(), // Assuming patient is fetched or available
                visit.getVisitDate()
        );
    }

    // ReportType Mappers
    public ReportTypeDTO toReportTypeDTO(ReportType reportType) {
        if (reportType == null) return null;
        return new ReportTypeDTO(
                reportType.getReportTypeId(),
                reportType.getReportName(),
                reportType.getReportTemplate()
        );
    }

    // Report Mappers
    public ReportDTO toReportDTO(Report report) {
        if (report == null) return null;
        return new ReportDTO(
                report.getReportId(),
                report.getVisit().getVisitId(), // Assuming visit is available
                report.getReportType().getReportTypeId(), // Assuming reportType is available
                report.getReportType().getReportName(), // Assuming reportType is available
                report.getReportData(),
                report.getCreatedDate(),
                report.getLastModifiedDate()
        );
    }

    // BillItem Mappers
    public BillItemDTO toBillItemDTO(BillItem billItem) {
        if (billItem == null) return null;
        return new BillItemDTO(
                billItem.getBillItemId(),
                billItem.getItemDescription(),
                billItem.getAmount()
        );
    }

    // Bill Mappers
    public BillDTO toBillDTO(Bill bill) {
        if (bill == null) return null;
        // *** Important consideration for Lazy Loading ***
        // Ensure bill.getItems() is loaded before mapping.
        // This might happen automatically if fetched eagerly,
        // or you might need to initialize it in the service layer
        // before calling this mapper, e.g., by calling bill.getItems().size()
        // or using JOIN FETCH in a repository query.
        List<BillItemDTO> itemDTOs = bill.getItems() != null ?
                bill.getItems().stream()
                        .map(this::toBillItemDTO)
                        .collect(Collectors.toList()) :
                Collections.emptyList(); // Handle null list

        return new BillDTO(
                bill.getBillId(),
                bill.getVisit() != null ? bill.getVisit().getVisitId() : null, // Handle null visit if possible
                bill.getBillDate(),
                bill.getTotalAmount(),
                itemDTOs
        );
    }
}