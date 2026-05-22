package com.isnoc.medicalcenter.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory_transactions")
public class InventoryTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    
    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    
    private String notes;
    
    @Column(nullable = false)
    private LocalDate transactionDate;
    
    @Column(nullable = false)
    private LocalDate createdAt;
    
    // Constructors
    public InventoryTransaction() {}
    
    public InventoryTransaction(Long transactionId, Medicine medicine, Integer quantity, 
                              TransactionType transactionType, String notes, 
                              LocalDate transactionDate, LocalDate createdAt) {
        this.transactionId = transactionId;
        this.medicine = medicine;
        this.quantity = quantity;
        this.transactionType = transactionType;
        this.notes = notes;
        this.transactionDate = transactionDate;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
    
    public Medicine getMedicine() {
        return medicine;
    }
    
    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public TransactionType getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDate getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        if (transactionDate == null) {
            transactionDate = LocalDate.now();
        }
    }
    
    public enum TransactionType {
        PURCHASE,
        SALE,
        ADJUSTMENT,
        RETURN,
        EXPIRED
    }
}
