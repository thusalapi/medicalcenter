package com.isnoc.medicalcenter.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.dto.*;
import com.isnoc.medicalcenter.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between entities and DTOs
 */
@Component
public class MapperUtils {

    private final JsonConverter jsonConverter;

    public MapperUtils(JsonConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    /**
     * Convert Patient entity to PatientDTO
     */
    public PatientDTO toPatientDTO(Patient patient) {
        if (patient == null) return null;
        
        return PatientDTO.builder()
                .id(patient.getId())
                .patientId(patient.getPatientId())
                .fullName(patient.getFullName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .contactNumber(patient.getContactNumber())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .createdDate(patient.getCreatedDate())
                .lastModifiedDate(patient.getLastModifiedDate())
                .build();
    }

    /**
     * Convert Visit entity to VisitDTO
     */
    public VisitDTO toVisitDTO(Visit visit) {
        if (visit == null) return null;
        
        VisitDTO dto = VisitDTO.builder()
                .id(visit.getId())
                .visitDate(visit.getVisitDate())
                .reason(visit.getReason())
                .notes(visit.getNotes())
                .patient(toPatientDTO(visit.getPatient()))
                .status(visit.getStatus())
                .createdDate(visit.getCreatedDate())
                .lastModifiedDate(visit.getLastModifiedDate())
                .build();
        
        return dto;
    }

    /**
     * Convert ReportType entity to ReportTypeDTO
     */
    public ReportTypeDTO toReportTypeDTO(ReportType reportType) {
        if (reportType == null) return null;
        
        return ReportTypeDTO.builder()
                .id(reportType.getId())
                .reportName(reportType.getReportName())
                .schema(reportType.getSchema())
                .template(reportType.getTemplate())
                .active(reportType.isActive())
                .createdDate(reportType.getCreatedDate())
                .lastModifiedDate(reportType.getLastModifiedDate())
                .build();
    }

    /**
     * Convert Report entity to ReportDTO
     */
    public ReportDTO toReportDTO(Report report) {
        if (report == null) return null;
        
        return ReportDTO.builder()
                .id(report.getId())
                .reportData(report.getReportData())
                .visit(toVisitDTO(report.getVisit()))
                .reportType(toReportTypeDTO(report.getReportType()))
                .createdDate(report.getCreatedDate())
                .lastModifiedDate(report.getLastModifiedDate())
                .build();
    }

    /**
     * Convert Bill entity to BillDTO
     */
    public BillDTO toBillDTO(Bill bill) {
        if (bill == null) return null;
        
        BillDTO dto = BillDTO.builder()
                .id(bill.getId())
                .billNumber(bill.getBillNumber())
                .issuedDate(bill.getIssuedDate())
                .dueDate(bill.getDueDate())
                .totalAmount(bill.getTotalAmount())
                .status(bill.getStatus())
                .patient(toPatientDTO(bill.getPatient()))
                .visit(toVisitDTO(bill.getVisit()))
                .paymentMethod(bill.getPaymentMethod())
                .createdDate(bill.getCreatedDate())
                .lastModifiedDate(bill.getLastModifiedDate())
                .build();

        if (bill.getBillItems() != null) {
            dto.setBillItems(bill.getBillItems().stream()
                    .map(this::toBillItemDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    /**
     * Convert BillItem entity to BillItemDTO
     */
    public BillItemDTO toBillItemDTO(BillItem billItem) {
        if (billItem == null) return null;
        
        return BillItemDTO.builder()
                .id(billItem.getId())
                .description(billItem.getDescription())
                .quantity(billItem.getQuantity())
                .unitPrice(billItem.getUnitPrice())
                .amount(billItem.getAmount())
                .itemType(billItem.getItemType())
                .build();
    }

    /**
     * Convert List of entities to List of DTOs
     */
    public List<PatientDTO> toPatientDTOList(List<Patient> patients) {
        return patients.stream().map(this::toPatientDTO).collect(Collectors.toList());
    }
    
    public List<VisitDTO> toVisitDTOList(List<Visit> visits) {
        return visits.stream().map(this::toVisitDTO).collect(Collectors.toList());
    }
    
    public List<ReportTypeDTO> toReportTypeDTOList(List<ReportType> reportTypes) {
        return reportTypes.stream().map(this::toReportTypeDTO).collect(Collectors.toList());
    }
    
    public List<ReportDTO> toReportDTOList(List<Report> reports) {
        return reports.stream().map(this::toReportDTO).collect(Collectors.toList());
    }
    
    public List<BillDTO> toBillDTOList(List<Bill> bills) {
        return bills.stream().map(this::toBillDTO).collect(Collectors.toList());
    }
}