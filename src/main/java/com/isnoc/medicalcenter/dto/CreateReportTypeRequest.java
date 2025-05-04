package com.isnoc.medicalcenter.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReportTypeRequest {

    @NotBlank(message = "Report name cannot be blank")
    private String reportName;

    @NotNull(message = "Report template cannot be null")
    private JsonNode reportTemplate; // This will contain the template structure including field positions
}