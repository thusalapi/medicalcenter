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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MapperUtils {

    // Static methods for easier usage in controllers
    public static PatientDTO mapPatientToDTO(Patient patient) {
        if (patient == null) return null;
        return new PatientDTO(
                patient.getPatientId(),
                patient.getPhoneNumber(),
                patient.getName(),
                patient.getOtherDetails()
        );
    }

    public static VisitDTO mapVisitToDTO(Visit visit) {
        if (visit == null) return null;
        return new VisitDTO(
                visit.getVisitId(),
                visit.getPatient().getPatientId(),
                visit.getPatient().getName(),
                visit.getVisitDate()
        );
    }

    public static ReportTypeDTO mapReportTypeToDTO(ReportType reportType) {
        if (reportType == null) return null;
        return new ReportTypeDTO(
                reportType.getReportTypeId(),
                reportType.getReportName(),
                reportType.getReportTemplate()
        );
    }

    public static ReportDTO mapReportToDTO(Report report) {
        if (report == null) return null;
        return new ReportDTO(
                report.getReportId(),
                report.getVisit().getVisitId(),
                report.getReportType().getReportTypeId(),
                report.getReportType().getReportName(),
                report.getReportData(),
                report.getCreatedDate(),
                report.getLastModifiedDate()
        );
    }

    public static BillItemDTO mapBillItemToDTO(BillItem billItem) {
        if (billItem == null) return null;
        return new BillItemDTO(
                billItem.getBillItemId(),
                billItem.getItemDescription(),
                billItem.getAmount()
        );
    }

    public static BillDTO mapBillToDTO(Bill bill) {
        if (bill == null) return null;
        List<BillItemDTO> itemDTOs = bill.getItems() != null ?
                bill.getItems().stream()
                        .map(MapperUtils::mapBillItemToDTO)
                        .collect(Collectors.toList()) :
                Collections.emptyList();

        return new BillDTO(
                bill.getBillId(),
                bill.getVisit() != null ? bill.getVisit().getVisitId() : null,
                bill.getBillDate(),
                bill.getTotalAmount(),
                itemDTOs
        );
    }
}