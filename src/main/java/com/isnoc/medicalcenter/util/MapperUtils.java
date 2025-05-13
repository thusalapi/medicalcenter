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
    // Static instance for utility methods
    private static MapperUtils instance;

    public MapperUtils(JsonConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
        instance = this;
    }

    /**
     * Convert Patient entity to PatientDTO
     */
    public PatientDTO toPatientDTO(Patient patient) {
        if (patient == null) return null;
        
        PatientDTO dto = new PatientDTO();
        dto.setPatientId(patient.getPatientId());
        dto.setName(patient.getName());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setOtherDetails(patient.getOtherDetails());
        return dto;
    }

    /**
     * Static method for converting Patient entity to PatientDTO
     */
    public static PatientDTO mapPatientToDTO(Patient patient) {
        return instance.toPatientDTO(patient);
    }

    /**
     * Convert Visit entity to VisitDTO
     */
    public VisitDTO toVisitDTO(Visit visit) {
        if (visit == null) return null;
        
        VisitDTO dto = new VisitDTO();
        dto.setVisitId(visit.getVisitId());
        dto.setVisitDate(visit.getVisitDate());
        
        // Set patient info if available
        if (visit.getPatient() != null) {
            dto.setPatientId(visit.getPatient().getPatientId());
            dto.setPatientName(visit.getPatient().getName());
        }
        
        return dto;
    }

    /**
     * Static method for converting Visit entity to VisitDTO
     */
    public static VisitDTO mapVisitToDTO(Visit visit) {
        return instance.toVisitDTO(visit);
    }

    /**
     * Convert ReportType entity to ReportTypeDTO
     */
    public ReportTypeDTO toReportTypeDTO(ReportType reportType) {
        if (reportType == null) return null;
        
        ReportTypeDTO dto = new ReportTypeDTO();
        dto.setReportTypeId(reportType.getReportTypeId());
        dto.setReportName(reportType.getReportName());
          // Set template if available
        if (reportType.getReportTemplate() != null) {
            dto.setReportTemplate(reportType.getReportTemplate());
        }
        
        return dto;
    }

    /**
     * Static method for converting ReportType entity to ReportTypeDTO
     */
    public static ReportTypeDTO mapReportTypeToDTO(ReportType reportType) {
        return instance.toReportTypeDTO(reportType);
    }

    /**
     * Convert Report entity to ReportDTO
     */
    public ReportDTO toReportDTO(Report report) {
        if (report == null) return null;
        
        ReportDTO dto = new ReportDTO();
        dto.setReportId(report.getReportId());
        dto.setReportData(report.getReportData());
        
        // Set visit information if available
        if (report.getVisit() != null) {
            dto.setVisitId(report.getVisit().getVisitId());
        }
        
        // Set report type information if available
        if (report.getReportType() != null) {
            dto.setReportTypeId(report.getReportType().getReportTypeId());
            dto.setReportTypeName(report.getReportType().getReportName());
        }
        
        dto.setCreatedDate(report.getCreatedDate());
        dto.setLastModifiedDate(report.getLastModifiedDate());
        
        return dto;
    }

    /**
     * Static method for converting Report entity to ReportDTO
     */
    public static ReportDTO mapReportToDTO(Report report) {
        return instance.toReportDTO(report);
    }

    /**
     * Convert Bill entity to BillDTO
     */
    public BillDTO toBillDTO(Bill bill) {
        if (bill == null) return null;
        
        BillDTO dto = new BillDTO();
        dto.setBillId(bill.getBillId());
        if (bill.getVisit() != null) {
            dto.setVisitId(bill.getVisit().getVisitId());
        }
        dto.setBillDate(bill.getBillDate());
        dto.setTotalAmount(bill.getTotalAmount());

        if (bill.getItems() != null) {
            dto.setItems(bill.getItems().stream()
                    .map(this::toBillItemDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    /**
     * Static method for converting Bill entity to BillDTO
     */
    public static BillDTO mapBillToDTO(Bill bill) {
        return instance.toBillDTO(bill);
    }

    /**
     * Convert BillItem entity to BillItemDTO
     */
    public BillItemDTO toBillItemDTO(BillItem billItem) {
        if (billItem == null) return null;
        
        BillItemDTO dto = new BillItemDTO();
        dto.setBillItemId(billItem.getBillItemId());
        dto.setItemDescription(billItem.getItemDescription());
        dto.setAmount(billItem.getAmount());
        
        return dto;
    }

    /**
     * Static method for converting BillItem entity to BillItemDTO
     */
    public static BillItemDTO mapBillItemToDTO(BillItem billItem) {
        return instance.toBillItemDTO(billItem);
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