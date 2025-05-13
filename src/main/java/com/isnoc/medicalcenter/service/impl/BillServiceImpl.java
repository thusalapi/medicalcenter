package com.isnoc.medicalcenter.service.impl;

import com.isnoc.medicalcenter.entity.Bill;
import com.isnoc.medicalcenter.entity.BillItem;
import com.isnoc.medicalcenter.entity.Visit;
import com.isnoc.medicalcenter.exception.ResourceNotFoundException;
import com.isnoc.medicalcenter.repository.BillItemRepository;
import com.isnoc.medicalcenter.repository.BillRepository;
import com.isnoc.medicalcenter.repository.VisitRepository;
import com.isnoc.medicalcenter.service.BillService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final VisitRepository visitRepository;

    @Autowired
    public BillServiceImpl(BillRepository billRepository, BillItemRepository billItemRepository, 
                          VisitRepository visitRepository) {
        this.billRepository = billRepository;
        this.billItemRepository = billItemRepository;
        this.visitRepository = visitRepository;
    }

    @Override
    @Transactional
    public Bill createBill(Long visitId) {
        // Check if visit exists
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id: " + visitId));
        
        // Check if bill already exists for visit
        if (billRepository.findByVisitVisitId(visitId).isPresent()) {
            throw new IllegalStateException("Bill already exists for visit with id: " + visitId);
        }
        
        Bill bill = new Bill();
        bill.setVisit(visit);
        bill.setBillDate(LocalDateTime.now());
        bill.setTotalAmount(BigDecimal.ZERO); // Initially zero, will be calculated when items are added
        
        return billRepository.save(bill);
    }

    @Override
    public Bill getBillById(Long billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + billId));
    }

    @Override
    public Bill getBillByVisitId(Long visitId) {
        return billRepository.findByVisitVisitId(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found for visit with id: " + visitId));
    }

    @Override
    @Transactional
    public BillItem addItemToBill(Long billId, String description, BigDecimal amount) {
        Bill bill = getBillById(billId);
        
        BillItem item = new BillItem();
        item.setBill(bill);
        item.setItemDescription(description);
        item.setAmount(amount);
        
        BillItem savedItem = billItemRepository.save(item);
        
        // Update bill's total
        bill.addItem(savedItem);
        bill.calculateTotalAmount();
        billRepository.save(bill);
        
        return savedItem;
    }

    @Override
    public List<BillItem> getBillItems(Long billId) {
        Bill bill = getBillById(billId);
        return bill.getItems();
    }

    @Override
    @Transactional
    public void removeBillItem(Long billId, Long billItemId) {
        Bill bill = getBillById(billId);
        
        BillItem item = billItemRepository.findById(billItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill item not found with id: " + billItemId));
        
        // Verify item belongs to this bill
        if (!item.getBill().getBillId().equals(billId)) {
            throw new IllegalArgumentException("Bill item with id " + billItemId + " does not belong to bill with id " + billId);
        }
        
        bill.removeItem(item);
        billItemRepository.delete(item);
        
        // Recalculate bill total
        bill.calculateTotalAmount();
        billRepository.save(bill);
    }

    @Override
    @Transactional
    public Bill recalculateBillTotal(Long billId) {
        Bill bill = getBillById(billId);
        bill.calculateTotalAmount();
        return billRepository.save(bill);
    }

    @Override
    public byte[] generateBillPdf(Long billId) {
        Bill bill = getBillById(billId);
        Visit visit = bill.getVisit();
        List<BillItem> items = bill.getItems();
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            float rowHeight = 20f;
              // Format currency
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
            currencyFormat.setCurrency(Currency.getInstance("USD"));
            
            // Track which item we're processing across pages
            int itemIndex = 0;
            
            // First page content
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Title
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Medical Center Bill");
                contentStream.endText();
                yPosition -= 30;
                
                // Bill details
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Bill Number: " + bill.getBillId());
                contentStream.endText();
                yPosition -= rowHeight;
                
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Date: " + 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(bill.getBillDate()));
                contentStream.endText();
                yPosition -= rowHeight;
                
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Patient: " + visit.getPatient().getName());
                contentStream.endText();
                yPosition -= rowHeight;
                
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Patient ID: " + visit.getPatient().getPatientId());
                contentStream.endText();
                yPosition -= rowHeight * 1.5f;
                
                // Table header
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Description");
                contentStream.endText();
                
                contentStream.beginText();
                contentStream.newLineAtOffset(margin + tableWidth - 100, yPosition);
                contentStream.showText("Amount");
                contentStream.endText();
                
                yPosition -= 15; 
                
                // Line below header
                contentStream.setLineWidth(0.5f);
                contentStream.moveTo(margin, yPosition);
                contentStream.lineTo(margin + tableWidth, yPosition);
                contentStream.stroke();
                
                yPosition -= 15;
                
                // Bill items - display as many as will fit on first page
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                
                while (itemIndex < items.size() && yPosition >= 100) {
                    BillItem item = items.get(itemIndex);
                    
                    // Description
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(item.getItemDescription());
                    contentStream.endText();
                    
                    // Amount
                    String formattedAmount = currencyFormat.format(item.getAmount());
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(margin + tableWidth - 100, yPosition);
                    contentStream.showText(formattedAmount);
                    contentStream.endText();
                    
                    yPosition -= rowHeight;
                    itemIndex++;
                }
                
                // If all items fit on the first page with room for the total
                if (itemIndex >= items.size() && yPosition >= 50) {
                    // Draw line above total
                    yPosition -= 10;
                    contentStream.setLineWidth(0.5f);
                    contentStream.moveTo(margin, yPosition);
                    contentStream.lineTo(margin + tableWidth, yPosition);
                    contentStream.stroke();
                    yPosition -= 15;
                    
                    // Total
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(margin + 50, yPosition);
                    contentStream.showText("Total Amount: ");
                    contentStream.endText();
                    
                    String formattedTotal = currencyFormat.format(bill.getTotalAmount());
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(margin + tableWidth - 100, yPosition);
                    contentStream.showText(formattedTotal);
                    contentStream.endText();
                }
            }
            
            // If we need additional pages for remaining items or for the total
            while (itemIndex < items.size() || (itemIndex >= items.size() && yPosition < 50)) {
                // Create new page
                PDPage newPage = new PDPage(PDRectangle.A4);
                document.addPage(newPage);
                yPosition = yStart;
                
                try (PDPageContentStream contentStream = new PDPageContentStream(document, newPage)) {
                    // If we still have items to display
                    while (itemIndex < items.size() && yPosition >= 100) {
                        BillItem item = items.get(itemIndex);
                        
                        // Description
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText(item.getItemDescription());
                        contentStream.endText();
                        
                        // Amount
                        String formattedAmount = currencyFormat.format(item.getAmount());
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.newLineAtOffset(margin + tableWidth - 100, yPosition);
                        contentStream.showText(formattedAmount);
                        contentStream.endText();
                        
                        yPosition -= rowHeight;
                        itemIndex++;
                    }
                    
                    // If all items have been displayed and we have room for the total
                    if (itemIndex >= items.size() && yPosition >= 50) {
                        // Draw line above total
                        yPosition -= 10;
                        contentStream.setLineWidth(0.5f);
                        contentStream.moveTo(margin, yPosition);
                        contentStream.lineTo(margin + tableWidth, yPosition);
                        contentStream.stroke();
                        yPosition -= 15;
                        
                        // Total
                        contentStream.beginText();                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.newLineAtOffset(margin + 50, yPosition);
                        contentStream.showText("Total Amount: ");
                        contentStream.endText();
                        
                        String formattedTotal = currencyFormat.format(bill.getTotalAmount());
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.newLineAtOffset(margin + tableWidth - 100, yPosition);
                        contentStream.showText(formattedTotal);
                        contentStream.endText();
                        
                        // Exit the while loop since we've displayed everything
                        break;
                    }
                }
            }
            
            // Convert document to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate bill PDF", e);
        }
    }
}