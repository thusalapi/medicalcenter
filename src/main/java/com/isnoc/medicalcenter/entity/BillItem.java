package com.isnoc.medicalcenter.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bill_items")
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill; // Link back to the Bill

    @Column(nullable = false)
    private String itemDescription;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    // Constructors
    public BillItem() {}
    
    public BillItem(Long billItemId, Bill bill, String itemDescription, BigDecimal amount) {
        this.billItemId = billItemId;
        this.bill = bill;
        this.itemDescription = itemDescription;
        this.amount = amount;
    }
    
    // Getters and setters
    public Long getBillItemId() {
        return billItemId;
    }
    
    public void setBillItemId(Long billItemId) {
        this.billItemId = billItemId;
    }
    
    public Bill getBill() {
        return bill;
    }
    
    public void setBill(Bill bill) {
        this.bill = bill;
    }
    
    public String getItemDescription() {
        return itemDescription;
    }
    
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}