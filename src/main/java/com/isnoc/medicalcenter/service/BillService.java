package com.isnoc.medicalcenter.service;

import com.isnoc.medicalcenter.entity.Bill;
import com.isnoc.medicalcenter.entity.BillItem;

import java.math.BigDecimal;
import java.util.List;

public interface BillService {
    Bill createBill(Long visitId);
    Bill getBillById(Long billId);
    Bill getBillByVisitId(Long visitId);
    BillItem addItemToBill(Long billId, String description, BigDecimal amount);
    List<BillItem> getBillItems(Long billId);
    void removeBillItem(Long billId, Long billItemId);
    Bill recalculateBillTotal(Long billId);
    byte[] generateBillPdf(Long billId);
}
