package com.isnoc.medicalcenter.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal; // Use BigDecimal for monetary values
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    @OneToOne(fetch = FetchType.LAZY) // One bill per visit
    @JoinColumn(name = "visit_id", nullable = false, unique = true)
    private Visit visit;

    @Column(precision = 10, scale = 2) // Example precision
    private BigDecimal totalAmount;

    private LocalDateTime billDate = LocalDateTime.now();

    // If using detailed BillItems:
    // @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // private List<BillItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BillItem> items = new ArrayList<>(); // Initialize the list

    // Helper method to add items and manage the bidirectional relationship
    public void addItem(BillItem item) {
        items.add(item);
        item.setBill(this);
    }

    public void removeItem(BillItem item) {
        items.remove(item);
        item.setBill(null);
    }

    // Add helper method to calculate total amount from items
    public void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(BillItem::getAmount)
                .filter(java.util.Objects::nonNull) // Ensure amount is not null
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}