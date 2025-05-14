package com.isnoc.medicalcenter.dto;

import com.isnoc.medicalcenter.entity.MedicineCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MedicineCategoryDTO {
    private Long categoryId;
    private String name;
    private String description;
    
    // Constructors
    public MedicineCategoryDTO() {}
    
    public MedicineCategoryDTO(Long categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }
    
    // Getters and setters
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
      public static MedicineCategoryDTO fromEntity(MedicineCategory category) {
        MedicineCategoryDTO dto = new MedicineCategoryDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }public MedicineCategory toEntity() {
        MedicineCategory category = new MedicineCategory();
        category.setCategoryId(this.categoryId);
        category.setName(this.name);
        category.setDescription(this.description);
        return category;
    }
}
