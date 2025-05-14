package com.isnoc.medicalcenter.controller;

import com.isnoc.medicalcenter.dto.InventoryTransactionDTO;
import com.isnoc.medicalcenter.dto.ResponseDTO;
import com.isnoc.medicalcenter.service.InventoryTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inventory-transactions")
public class InventoryTransactionController {

    private final InventoryTransactionService transactionService;

    @Autowired
    public InventoryTransactionController(InventoryTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<InventoryTransactionDTO>>> getTransactions(
            @RequestParam(required = false) Long medicineId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<InventoryTransactionDTO> transactions;
        
        if (medicineId != null && startDate != null && endDate != null) {
            transactions = transactionService.getTransactionsForMedicineBetweenDates(medicineId, startDate, endDate);
        } else if (medicineId != null) {
            transactions = transactionService.getTransactionsForMedicine(medicineId);
        } else if (startDate != null && endDate != null) {
            transactions = transactionService.getTransactionsBetweenDates(startDate, endDate);
        } else {
            transactions = transactionService.getAllTransactions();
        }
        
        return ResponseEntity.ok(ResponseDTO.success(transactions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<InventoryTransactionDTO>> getTransactionById(@PathVariable Long id) {
        InventoryTransactionDTO transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(ResponseDTO.success(transaction));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<InventoryTransactionDTO>> recordTransaction(
            @RequestBody InventoryTransactionDTO transactionDTO) {
        InventoryTransactionDTO recordedTransaction = transactionService.recordTransaction(transactionDTO);
        return new ResponseEntity<>(
                ResponseDTO.success("Transaction recorded successfully", recordedTransaction),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/paginated")
    public ResponseEntity<ResponseDTO<Page<InventoryTransactionDTO>>> getTransactionsPaginated(
            Pageable pageable,
            @RequestParam(required = false) Long medicineId) {
        
        Page<InventoryTransactionDTO> transactions;
        
        if (medicineId != null) {
            transactions = transactionService.getTransactionsForMedicinePaginated(medicineId, pageable);
        } else {
            transactions = transactionService.getTransactionsPaginated(pageable);
        }
        
        return ResponseEntity.ok(ResponseDTO.success(transactions));
    }
}
