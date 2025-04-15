package com.isnoc.medicalcenter.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.isnoc.medicalcenter.util.JsonNodeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @Column(unique = true, nullable = false)
    private String phoneNumber; // Lookup key

    private String name;
    // Add other fixed/searchable fields if needed

    @Column(columnDefinition = "TEXT") // Store as TEXT in SQLite
    @Convert(converter = JsonNodeConverter.class) // Use our converter
    private JsonNode otherDetails; // Flexible JSON data
}