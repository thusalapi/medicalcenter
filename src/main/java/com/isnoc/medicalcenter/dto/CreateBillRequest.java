package com.isnoc.medicalcenter.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBillRequest {
    @NotNull(message = "Visit ID cannot be null")
    private Long visitId;
}