package com.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long transactionId;
    private Long cardId;
    private Long customerId;
    private Long merchantId;
    private String transactionType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String transactionStatus;
    private String failureReason;

    public TransactionResponse() {
    }

    public TransactionResponse(
            Long transactionId,
            Long cardId,
            Long customerId,
            Long merchantId,
            String transactionType,
            BigDecimal amount,
            LocalDateTime transactionDate,
            String transactionStatus,
            String failureReason) {

        this.transactionId = transactionId;
        this.cardId = cardId;
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.transactionStatus = transactionStatus;
        this.failureReason = failureReason;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "transactionId=" + transactionId +
                ", cardId=" + cardId +
                ", customerId=" + customerId +
                ", merchantId=" + merchantId +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }
}