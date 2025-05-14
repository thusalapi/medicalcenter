package com.isnoc.medicalcenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class CreateBillItemRequest {
    
    @NotBlank(message = "Item description cannot be blank")
    private String itemDescription;
    
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;
    
    // Constructors
    public CreateBillItemRequest() {}
    
    public CreateBillItemRequest(String itemDescription, BigDecimal amount) {
        this.itemDescription = itemDescription;
        this.amount = amount;
    }
    
    // Getters and setters
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