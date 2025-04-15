package com.isnoc.medicalcenter.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitId;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetch is often better performance
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime visitDate = LocalDateTime.now(); // Default to now

    // Relationships (optional, depending if you navigate from Visit)
    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Report> reports = new ArrayList<>();

    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Bill bill; // Assuming one bill per visit for simplicity
}