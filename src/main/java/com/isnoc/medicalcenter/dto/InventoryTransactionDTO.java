package com.isnoc.medicalcenter.dto;

import com.isnoc.medicalcenter.entity.InventoryTransaction;
import com.isnoc.medicalcenter.entity.Medicine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class InventoryTransactionDTO {
    private Long transactionId;
    private Long medicineId;
    private String medicineName;
    private Integer quantity;
    private InventoryTransaction.TransactionType transactionType;
    private String notes;
    private LocalDate transactionDate;
    private LocalDate createdAt;
    
    // Constructors
    public InventoryTransactionDTO() {}
    
    public InventoryTransactionDTO(Long transactionId, Long medicineId, String medicineName, 
                                Integer quantity, InventoryTransaction.TransactionType transactionType, 
                                String notes, LocalDate transactionDate, LocalDate createdAt) {
        this.transactionId = transactionId;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.transactionType = transactionType;
        this.notes = notes;
        this.transactionDate = transactionDate;
        this.createdAt = createdAt;
    }
    
    // Getters and setters
    public Long getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
    
    public Long getMedicineId() {
        return medicineId;
    }
    
    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }
    
    public String getMedicineName() {
        return medicineName;
    }
    
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public InventoryTransaction.TransactionType getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(InventoryTransaction.TransactionType transactionType) {
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
      public static InventoryTransactionDTO fromEntity(InventoryTransaction transaction) {
        InventoryTransactionDTO dto = new InventoryTransactionDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setMedicineId(transaction.getMedicine().getMedicineId());
        dto.setMedicineName(transaction.getMedicine().getName());
        dto.setQuantity(transaction.getQuantity());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setNotes(transaction.getNotes());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }
      public InventoryTransaction toEntity(Medicine medicine) {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTransactionId(this.transactionId);
        transaction.setMedicine(medicine);
        transaction.setQuantity(this.quantity);
        transaction.setTransactionType(this.transactionType);
        transaction.setNotes(this.notes);
        transaction.setTransactionDate(this.transactionDate);
        transaction.setCreatedAt(this.createdAt);
        return transaction;
    }
}
