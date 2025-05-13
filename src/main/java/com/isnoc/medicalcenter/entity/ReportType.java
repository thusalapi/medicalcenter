package com.isnoc.medicalcenter.entity;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.isnoc.medicalcenter.util.JsonNodeConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportTypeId;

    @Column(unique = true, nullable = false)
    private String reportName;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode reportTemplate; // Structure defined by Admin UI
    
    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
}