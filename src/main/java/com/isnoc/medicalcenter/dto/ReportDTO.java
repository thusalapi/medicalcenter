package com.isnoc.medicalcenter.dto;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private Long reportId;
    private Long visitId;
    private Long reportTypeId;
    private String reportTypeName; // For display
    private JsonNode reportData;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}