package com.isnoc.medicalcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {
    private Long billId;
    private Long visitId;
    private LocalDateTime billDate;
    private BigDecimal totalAmount;
    private List<BillItemDTO> items;
}