package com.isnoc.medicalcenter.entity;


import jakarta.persistence.*;

import java.math.BigDecimal; // Use BigDecimal for monetary values
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
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

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BillItem> items = new ArrayList<>(); // Initialize the list
    
    // Constructors
    public Bill() {}
    
    public Bill(Long billId, Visit visit, BigDecimal totalAmount, LocalDateTime billDate, List<BillItem> items) {
        this.billId = billId;
        this.visit = visit;
        this.totalAmount = totalAmount;
        this.billDate = billDate;
        this.items = items;
    }
    
    // Getters and setters
    public Long getBillId() {
        return billId;
    }
    
    public void setBillId(Long billId) {
        this.billId = billId;
    }
    
    public Visit getVisit() {
        return visit;
    }
    
    public void setVisit(Visit visit) {
        this.visit = visit;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public LocalDateTime getBillDate() {
        return billDate;
    }
    
    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }
    
    public List<BillItem> getItems() {
        return items;
    }
    
    public void setItems(List<BillItem> items) {
        this.items = items;
    }

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