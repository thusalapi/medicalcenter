package com.isnoc.medicalcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillItemDTO {
    private Long billItemId;
    private String itemDescription;
    private BigDecimal amount;
}