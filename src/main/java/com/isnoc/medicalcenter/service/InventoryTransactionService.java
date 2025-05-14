package com.isnoc.medicalcenter.service;

import com.isnoc.medicalcenter.dto.InventoryTransactionDTO;
import com.isnoc.medicalcenter.entity.InventoryTransaction;
import com.isnoc.medicalcenter.entity.Medicine;
import com.isnoc.medicalcenter.repository.InventoryTransactionRepository;
import com.isnoc.medicalcenter.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class InventoryTransactionService {

    private final InventoryTransactionRepository transactionRepository;
    private final MedicineRepository medicineRepository;

    @Autowired
    public InventoryTransactionService(
            InventoryTransactionRepository transactionRepository,
            MedicineRepository medicineRepository) {
        this.transactionRepository = transactionRepository;
        this.medicineRepository = medicineRepository;
    }

    public List<InventoryTransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(InventoryTransactionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public InventoryTransactionDTO getTransactionById(Long id) {
        InventoryTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found with id: " + id));
        return InventoryTransactionDTO.fromEntity(transaction);
    }

    @Transactional
    public InventoryTransactionDTO recordTransaction(InventoryTransactionDTO transactionDTO) {
        Medicine medicine = medicineRepository.findById(transactionDTO.getMedicineId())
                .orElseThrow(() -> new NoSuchElementException("Medicine not found with id: " + transactionDTO.getMedicineId()));        // Create the transaction
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setMedicine(medicine);
        transaction.setQuantity(transactionDTO.getQuantity());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setNotes(transactionDTO.getNotes());
        transaction.setTransactionDate(transactionDTO.getTransactionDate() != null ? 
                                      transactionDTO.getTransactionDate() : LocalDate.now());

        // Update medicine stock quantity
        int newQuantity = medicine.getStockQuantity() + transactionDTO.getQuantity();
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Insufficient stock. Current: " + medicine.getStockQuantity() + ", Requested: " + Math.abs(transactionDTO.getQuantity()));
        }
        medicine.setStockQuantity(newQuantity);
        medicine.setUpdatedAt(LocalDate.now());

        // Save both transaction and updated medicine
        medicineRepository.save(medicine);
        InventoryTransaction savedTransaction = transactionRepository.save(transaction);

        return InventoryTransactionDTO.fromEntity(savedTransaction);
    }

    public List<InventoryTransactionDTO> getTransactionsForMedicine(Long medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NoSuchElementException("Medicine not found with id: " + medicineId));

        return transactionRepository.findByMedicine(medicine).stream()
                .map(InventoryTransactionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<InventoryTransactionDTO> getTransactionsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate).stream()
                .map(InventoryTransactionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<InventoryTransactionDTO> getTransactionsForMedicineBetweenDates(
            Long medicineId, LocalDate startDate, LocalDate endDate) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NoSuchElementException("Medicine not found with id: " + medicineId));

        return transactionRepository.findByMedicineAndTransactionDateBetween(medicine, startDate, endDate).stream()
                .map(InventoryTransactionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<InventoryTransactionDTO> getTransactionsPaginated(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map(InventoryTransactionDTO::fromEntity);
    }

    public Page<InventoryTransactionDTO> getTransactionsForMedicinePaginated(Long medicineId, Pageable pageable) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NoSuchElementException("Medicine not found with id: " + medicineId));

        return transactionRepository.findByMedicine(medicine, pageable)
                .map(InventoryTransactionDTO::fromEntity);
    }
}
