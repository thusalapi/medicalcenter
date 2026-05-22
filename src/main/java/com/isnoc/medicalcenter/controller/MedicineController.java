package com.isnoc.medicalcenter.controller;

import com.isnoc.medicalcenter.dto.MedicineDTO;
import com.isnoc.medicalcenter.dto.ResponseDTO;
import com.isnoc.medicalcenter.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    @Autowired
    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<MedicineDTO>>> getAllMedicines() {
        List<MedicineDTO> medicines = medicineService.getAllMedicines();
        return ResponseEntity.ok(ResponseDTO.success(medicines));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<MedicineDTO>> getMedicineById(@PathVariable Long id) {
        MedicineDTO medicine = medicineService.getMedicineById(id);
        return ResponseEntity.ok(ResponseDTO.success(medicine));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<MedicineDTO>> createMedicine(@RequestBody MedicineDTO medicineDTO) {
        MedicineDTO createdMedicine = medicineService.createMedicine(medicineDTO);
        return new ResponseEntity<>(
                ResponseDTO.success("Medicine created successfully", createdMedicine),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<MedicineDTO>> updateMedicine(
            @PathVariable Long id, 
            @RequestBody MedicineDTO medicineDTO) {
        MedicineDTO updatedMedicine = medicineService.updateMedicine(id, medicineDTO);
        return ResponseEntity.ok(
                ResponseDTO.success("Medicine updated successfully", updatedMedicine)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.ok(
                ResponseDTO.success("Medicine deleted successfully", null)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<MedicineDTO>>> searchMedicines(@RequestParam String query) {
        List<MedicineDTO> medicines = medicineService.searchMedicines(query);
        return ResponseEntity.ok(ResponseDTO.success(medicines));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ResponseDTO<List<MedicineDTO>>> getLowStockMedicines() {
        List<MedicineDTO> medicines = medicineService.getLowStockMedicines();
        return ResponseEntity.ok(ResponseDTO.success(medicines));
    }

    @GetMapping("/expiring")
    public ResponseEntity<ResponseDTO<List<MedicineDTO>>> getExpiringMedicines(
            @RequestParam(defaultValue = "30") int days) {
        List<MedicineDTO> medicines = medicineService.getExpiringMedicines(days);
        return ResponseEntity.ok(ResponseDTO.success(medicines));
    }
}
