package com.isnoc.medicalcenter.controller;

import com.isnoc.medicalcenter.dto.ResponseDTO;
import com.isnoc.medicalcenter.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory-statistics")
public class InventoryStatisticsController {

    private final MedicineService medicineService;

    @Autowired
    public InventoryStatisticsController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getInventoryStatistics() {
        // Get total count of medicines
        int totalMedicines = medicineService.getAllMedicines().size();
        
        // Get low stock medicines count
        int lowStockCount = medicineService.getLowStockMedicines().size();
        
        // Get expiring medicines count (within 30 days)
        int expiringCount = medicineService.getExpiringMedicines(30).size();
        
        // Calculate total inventory value
        BigDecimal totalInventoryValue = calculateTotalInventoryValue();
        
        // Create statistics response
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalMedicines", totalMedicines);
        statistics.put("lowStockCount", lowStockCount);
        statistics.put("expiringCount", expiringCount);
        statistics.put("totalInventoryValue", totalInventoryValue);
        
        // Get category distribution
        Map<String, Integer> categoryDistribution = calculateCategoryDistribution();
        statistics.put("categoryDistribution", categoryDistribution);
        
        return ResponseEntity.ok(ResponseDTO.success(statistics));
    }
    
    private BigDecimal calculateTotalInventoryValue() {
        return medicineService.getAllMedicines().stream()
                .map(medicine -> medicine.getUnitPrice().multiply(BigDecimal.valueOf(medicine.getStockQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private Map<String, Integer> calculateCategoryDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        
        medicineService.getAllMedicines().forEach(medicine -> {
            String category = medicine.getCategory();
            distribution.put(category, distribution.getOrDefault(category, 0) + 1);
        });
        
        return distribution;
    }
}
