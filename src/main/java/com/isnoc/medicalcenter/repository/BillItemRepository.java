package com.isnoc.medicalcenter.repository;

import com.isnoc.medicalcenter.entity.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillItemRepository extends JpaRepository<BillItem, Long> {
    // Add custom query methods if needed, e.g., findByBillBillId
}