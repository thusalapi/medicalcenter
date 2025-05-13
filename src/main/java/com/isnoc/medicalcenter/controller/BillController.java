package com.isnoc.medicalcenter.controller;

import com.isnoc.medicalcenter.dto.BillDTO;
import com.isnoc.medicalcenter.dto.BillItemDTO;
import com.isnoc.medicalcenter.dto.CreateBillItemRequest;
import com.isnoc.medicalcenter.dto.CreateBillRequest;
import com.isnoc.medicalcenter.dto.StatisticsDTO;
import com.isnoc.medicalcenter.entity.Bill;
import com.isnoc.medicalcenter.entity.BillItem;
import com.isnoc.medicalcenter.service.BillService;
import com.isnoc.medicalcenter.service.StatisticsService;
import com.isnoc.medicalcenter.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillService billService;
    private final StatisticsService statisticsService;

    @Autowired
    public BillController(BillService billService, StatisticsService statisticsService) {
        this.billService = billService;
        this.statisticsService = statisticsService;
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getBillingStatistics() {
        StatisticsDTO stats = statisticsService.getBillingStatistics();
        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", stats.getTotalRevenue());
        response.put("monthlyRevenue", stats.getMonthlyRevenue());
        response.put("todayRevenue", stats.getTodayRevenue());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BillDTO> createBill(@Valid @RequestBody CreateBillRequest request) {
        Bill bill = billService.createBill(request.getVisitId());
        return new ResponseEntity<>(MapperUtils.mapBillToDTO(bill), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable("id") Long billId) {
        Bill bill = billService.getBillById(billId);
        return ResponseEntity.ok(MapperUtils.mapBillToDTO(bill));
    }

    @GetMapping("/visit/{visitId}")
    public ResponseEntity<BillDTO> getBillByVisitId(@PathVariable Long visitId) {
        Bill bill = billService.getBillByVisitId(visitId);
        return ResponseEntity.ok(MapperUtils.mapBillToDTO(bill));
    }

    @PostMapping("/{billId}/items")
    public ResponseEntity<BillItemDTO> addItemToBill(
            @PathVariable Long billId, 
            @Valid @RequestBody CreateBillItemRequest request) {
        BillItem billItem = billService.addItemToBill(
                billId, 
                request.getItemDescription(), 
                request.getAmount()
        );
        return new ResponseEntity<>(MapperUtils.mapBillItemToDTO(billItem), HttpStatus.CREATED);
    }

    @GetMapping("/{billId}/items")
    public ResponseEntity<List<BillItemDTO>> getBillItems(@PathVariable Long billId) {
        List<BillItem> items = billService.getBillItems(billId);
        List<BillItemDTO> itemDTOs = items.stream()
                .map(MapperUtils::mapBillItemToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemDTOs);
    }

    @DeleteMapping("/{billId}/items/{itemId}")
    public ResponseEntity<Void> removeBillItem(@PathVariable Long billId, @PathVariable Long itemId) {
        billService.removeBillItem(billId, itemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{billId}/recalculate")
    public ResponseEntity<BillDTO> recalculateBillTotal(@PathVariable Long billId) {
        Bill updatedBill = billService.recalculateBillTotal(billId);
        return ResponseEntity.ok(MapperUtils.mapBillToDTO(updatedBill));
    }
    
    @GetMapping("/{billId}/pdf")
    public ResponseEntity<byte[]> generateBillPdf(@PathVariable Long billId) {
        byte[] pdfContent = billService.generateBillPdf(billId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "bill-" + billId + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }
}