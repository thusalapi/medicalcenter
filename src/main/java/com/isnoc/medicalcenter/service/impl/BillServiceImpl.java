package com.isnoc.medicalcenter.service.impl;

import lombok.RequiredArgsConstructor;
import com.isnoc.medicalcenter.dto.CreateBillItemRequest;
import com.isnoc.medicalcenter.dto.CreateBillRequest;
import com.isnoc.medicalcenter.entity.Bill;
import com.isnoc.medicalcenter.entity.BillItem;
import com.isnoc.medicalcenter.entity.Visit;
import com.isnoc.medicalcenter.exception.ResourceNotFoundException;
import com.isnoc.medicalcenter.repository.BillRepository;
import com.isnoc.medicalcenter.repository.VisitRepository;
import com.isnoc.medicalcenter.service.BillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService { // Assuming BillService interface exists

    private final BillRepository billRepository;
    private final VisitRepository visitRepository;
    // No need for BillItemRepository here if using cascade persist from Bill

    @Override
    @Transactional
    public Bill createBill(CreateBillRequest request) {
        Visit visit = visitRepository.findById(request.getVisitId())
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + request.getVisitId()));

        // Optional: Check if a bill already exists for this visit
        if (billRepository.findByVisitVisitId(visit.getVisitId()).isPresent()) {
            throw new IllegalArgumentException("A bill already exists for visit ID: " + visit.getVisitId());
        }

        Bill bill = new Bill();
        bill.setVisit(visit);
        // billDate defaults to now()

        // Create and add BillItems
        for (CreateBillItemRequest itemRequest : request.getItems()) {
            BillItem billItem = new BillItem();
            billItem.setItemDescription(itemRequest.getItemDescription());
            billItem.setAmount(itemRequest.getAmount());
            // The helper method sets the relationship: billItem.setBill(bill);
            bill.addItem(billItem);
        }

        // Calculate total amount *after* adding all items
        bill.calculateTotalAmount();

        return billRepository.save(bill); // Cascade will save associated BillItems
    }

    @Override
    @Transactional(readOnly = true)
    public Bill getBillById(Long billId) {
        // Consider fetching items eagerly if always needed with the bill
        // Option 1: Use FetchType.EAGER on Bill.items (simpler, but less performant if items not always needed)
        // Option 2: Add a repository method with JOIN FETCH
        // Option 3: Initialize items here:
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + billId));
        // Initialize lazy collection if needed before returning
        // Hibernate.initialize(bill.getItems()); // Or simply access it: bill.getItems().size();
        return bill;
        // Or directly use a repository method that fetches items
        // return billRepository.findByIdFetchingItems(billId)
        //        .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + billId));

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bill> getBillByVisitId(Long visitId) {
        // Similar consideration for fetching items as getBillById
        Optional<Bill> billOpt = billRepository.findByVisitVisitId(visitId);
        // billOpt.ifPresent(bill -> Hibernate.initialize(bill.getItems())); // Initialize if found
        return billOpt;
        // Or use a specific repository method:
        // return billRepository.findByVisitVisitIdFetchingItems(visitId);
    }

    // Implement update/delete if necessary
}

// Interface BillService would declare these methods

// Example Repository method with JOIN FETCH (in BillRepository)
// public interface BillRepository extends JpaRepository<Bill, Long> {
//     Optional<Bill> findByVisitVisitId(Long visitId);
//
//     @Query("SELECT b FROM Bill b LEFT JOIN FETCH b.items WHERE b.billId = :billId")
//     Optional<Bill> findByIdFetchingItems(@Param("billId") Long billId);
//
//     @Query("SELECT b FROM Bill b LEFT JOIN FETCH b.items WHERE b.visit.visitId = :visitId")
//     Optional<Bill> findByVisitVisitIdFetchingItems(@Param("visitId") Long visitId);
// }