package com.isnoc.medicalcenter.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBillRequest {
    @NotNull(message = "Visit ID cannot be null")
    private Long visitId;

    @NotEmpty(message = "Bill must have at least one item")
    @Valid // Validate elements within the list
    private List<CreateBillItemRequest> items;

    // totalAmount might be calculated on backend or sent from frontend
//    @PositiveOrZero(message = "Total amount must be zero or positive")
//    private BigDecimal totalAmount; // Optional: could be calculated
}