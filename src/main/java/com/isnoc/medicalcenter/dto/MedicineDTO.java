package com.isnoc.medicalcenter.dto;

import com.isnoc.medicalcenter.entity.Medicine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicineDTO {
    private Long medicineId;
    private String name;
    private String description;
    private String category;
    private String unit;
    private BigDecimal unitPrice;
    private Integer stockQuantity;
    private Integer reorderLevel;
    private String manufacturer;
    private String batchNumber;
    private LocalDate expiryDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    
    // Constructors
    public MedicineDTO() {}
    
    public MedicineDTO(Long medicineId, String name, String description, String category, 
                     String unit, BigDecimal unitPrice, Integer stockQuantity, 
                     Integer reorderLevel, String manufacturer, String batchNumber, 
                     LocalDate expiryDate, LocalDate createdAt, LocalDate updatedAt) {
        this.medicineId = medicineId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.manufacturer = manufacturer;
        this.batchNumber = batchNumber;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and setters
    public Long getMedicineId() {
        return medicineId;
    }
    
    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public Integer getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public Integer getReorderLevel() {
        return reorderLevel;
    }
    
    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public String getBatchNumber() {
        return batchNumber;
    }
    
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDate getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
      public static MedicineDTO fromEntity(Medicine medicine) {
        MedicineDTO dto = new MedicineDTO();
        dto.setMedicineId(medicine.getMedicineId());
        dto.setName(medicine.getName());
        dto.setDescription(medicine.getDescription());
        dto.setCategory(medicine.getCategory());
        dto.setUnit(medicine.getUnit());
        dto.setUnitPrice(medicine.getUnitPrice());
        dto.setStockQuantity(medicine.getStockQuantity());
        dto.setReorderLevel(medicine.getReorderLevel());
        dto.setManufacturer(medicine.getManufacturer());
        dto.setBatchNumber(medicine.getBatchNumber());
        dto.setExpiryDate(medicine.getExpiryDate());
        dto.setCreatedAt(medicine.getCreatedAt());
        dto.setUpdatedAt(medicine.getUpdatedAt());
        return dto;
    }public Medicine toEntity() {
        Medicine medicine = new Medicine();
        medicine.setMedicineId(this.medicineId);
        medicine.setName(this.name);
        medicine.setDescription(this.description);
        medicine.setCategory(this.category);
        medicine.setUnit(this.unit);
        medicine.setUnitPrice(this.unitPrice);
        medicine.setStockQuantity(this.stockQuantity);
        medicine.setReorderLevel(this.reorderLevel);
        medicine.setManufacturer(this.manufacturer);
        medicine.setBatchNumber(this.batchNumber);
        medicine.setExpiryDate(this.expiryDate);
        medicine.setCreatedAt(this.createdAt);
        medicine.setUpdatedAt(this.updatedAt);
        return medicine;
    }
}
