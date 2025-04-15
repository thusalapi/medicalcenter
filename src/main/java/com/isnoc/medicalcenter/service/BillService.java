package com.isnoc.medicalcenter.service;

import com.isnoc.medicalcenter.dto.CreateBillRequest;
import com.isnoc.medicalcenter.entity.Bill;

import java.util.Optional;

public interface BillService {
    Bill createBill(CreateBillRequest request);
    Bill getBillById(Long billId);
    Optional<Bill> getBillByVisitId(Long visitId);
}
