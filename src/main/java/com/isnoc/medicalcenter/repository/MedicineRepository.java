package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    
    List<Medicine> findByNameContainingIgnoreCase(String name);
    
    List<Medicine> findByCategory(String category);
    
    @Query("SELECT m FROM Medicine m WHERE m.stockQuantity <= m.reorderLevel")
    List<Medicine> findLowStockMedicines();
    
    @Query("SELECT m FROM Medicine m WHERE m.expiryDate IS NOT NULL AND m.expiryDate <= :expiryThreshold")
    List<Medicine> findExpiringMedicines(LocalDate expiryThreshold);
}
