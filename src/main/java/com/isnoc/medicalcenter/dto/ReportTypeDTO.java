package com.isnoc.medicalcenter.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportTypeDTO {
    private Long reportTypeId;
    private String reportName;
    private JsonNode reportTemplate;
}