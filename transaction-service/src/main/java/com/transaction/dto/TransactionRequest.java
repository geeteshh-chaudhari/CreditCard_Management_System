package com.transaction.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class TransactionRequest {

    @NotNull(message = "Card ID is required")
    private Long cardId;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    // Required for PURCHASE, not required for PAYMENT
    private Long merchantId;

    @NotNull(message = "Amount is required")
    @DecimalMin(
        value = "0.01",
        inclusive = true,
        message = "Amount must be greater than 0"
    )
    private BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    @Pattern(
        regexp = "^(PURCHASE|PAYMENT)$",
        message = "Transaction type must be PURCHASE or PAYMENT"
    )
    private String transactionType;

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}