package com.isnoc.medicalcenter.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bill_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill; // Link back to the Bill

    @Column(nullable = false)
    private String itemDescription;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
}