package com.isnoc.medicalcenter.entity;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.isnoc.medicalcenter.util.JsonNodeConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY) // Fetch type info when needed
    @JoinColumn(name = "report_type_id", nullable = false)
    private ReportType reportType;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode reportData; // Data entered by Receptionist

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
}