package com.isnoc.medicalcenter.service;

import com.isnoc.medicalcenter.dto.MedicineDTO;
import com.isnoc.medicalcenter.entity.Medicine;
import com.isnoc.medicalcenter.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;

    @Autowired
    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public List<MedicineDTO> getAllMedicines() {
        return medicineRepository.findAll().stream()
                .map(MedicineDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public MedicineDTO getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Medicine not found with id: " + id));
        return MedicineDTO.fromEntity(medicine);
    }

    @Transactional
    public MedicineDTO createMedicine(MedicineDTO medicineDTO) {
        Medicine medicine = medicineDTO.toEntity();
        medicine.setMedicineId(null); // Ensure we're creating a new entity
        medicine.setCreatedAt(LocalDate.now());
        medicine = medicineRepository.save(medicine);
        return MedicineDTO.fromEntity(medicine);
    }

    @Transactional
    public MedicineDTO updateMedicine(Long id, MedicineDTO medicineDTO) {
        Medicine existingMedicine = medicineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Medicine not found with id: " + id));

        // Update fields from DTO
        existingMedicine.setName(medicineDTO.getName());
        existingMedicine.setDescription(medicineDTO.getDescription());
        existingMedicine.setCategory(medicineDTO.getCategory());
        existingMedicine.setUnit(medicineDTO.getUnit());
        existingMedicine.setUnitPrice(medicineDTO.getUnitPrice());
        existingMedicine.setStockQuantity(medicineDTO.getStockQuantity());
        existingMedicine.setReorderLevel(medicineDTO.getReorderLevel());
        existingMedicine.setManufacturer(medicineDTO.getManufacturer());
        existingMedicine.setBatchNumber(medicineDTO.getBatchNumber());
        existingMedicine.setExpiryDate(medicineDTO.getExpiryDate());
        existingMedicine.setUpdatedAt(LocalDate.now());

        Medicine updatedMedicine = medicineRepository.save(existingMedicine);
        return MedicineDTO.fromEntity(updatedMedicine);
    }

    @Transactional
    public void deleteMedicine(Long id) {
        if (!medicineRepository.existsById(id)) {
            throw new NoSuchElementException("Medicine not found with id: " + id);
        }
        medicineRepository.deleteById(id);
    }

    public List<MedicineDTO> searchMedicines(String query) {
        return medicineRepository.findByNameContainingIgnoreCase(query).stream()
                .map(MedicineDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getLowStockMedicines() {
        return medicineRepository.findLowStockMedicines().stream()
                .map(MedicineDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> getExpiringMedicines(int days) {
        LocalDate threshold = LocalDate.now().plusDays(days);
        return medicineRepository.findExpiringMedicines(threshold).stream()
                .map(MedicineDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
