package com.isnoc.medicalcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {
    private Long billId;
    private Long visitId;
    private String patientName;
    private LocalDateTime billDate;
    private BigDecimal totalAmount;
    private String status;
    private List<BillItemDTO> items;
}