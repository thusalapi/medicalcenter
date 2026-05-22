package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.InventoryTransaction;
import com.isnoc.medicalcenter.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    
    List<InventoryTransaction> findByMedicine(Medicine medicine);
    
    Page<InventoryTransaction> findByMedicine(Medicine medicine, Pageable pageable);
    
    List<InventoryTransaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    
    Page<InventoryTransaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    List<InventoryTransaction> findByMedicineAndTransactionDateBetween(
            Medicine medicine, LocalDate startDate, LocalDate endDate);
}
