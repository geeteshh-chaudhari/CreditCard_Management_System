package com.creditcard.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CREDIT_CARD")
public class CreditCard {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "CARD_ID")
private Long cardId;

@Column(name = "CUSTOMER_ID", nullable = false)
private Long customerId;

@Column(name = "CARD_NUMBER", nullable = false, unique = true, length = 19)
private String cardNumber;

@Column(name = "CARD_TYPE", nullable = false, length = 10)
private String cardType;

@Column(name = "CREDIT_LIMIT", nullable = false, precision = 12, scale = 2)
private BigDecimal creditLimit;

@Column(name = "AVAILABLE_CREDIT", nullable = false, precision = 12, scale = 2)
private BigDecimal availableCredit;

@Column(name = "OUTSTANDING_AMOUNT", nullable = false, precision = 12, scale = 2)
private BigDecimal outstandingAmount;

@Column(name = "EXPIRY_DATE", nullable = false)
private LocalDate expiryDate;

@Column(name = "CARD_STATUS", nullable = false, length = 10)
private String cardStatus;

@Column(name = "ISSUED_AT", nullable = false, insertable = false, updatable = false)
private LocalDateTime issuedAt;

public CreditCard() {
}

public CreditCard(
        Long cardId,
        Long customerId,
        String cardNumber,
        String cardType,
        BigDecimal creditLimit,
        BigDecimal availableCredit,
        BigDecimal outstandingAmount,
        LocalDate expiryDate,
        String cardStatus,
        LocalDateTime issuedAt) {

    this.cardId = cardId;
    this.customerId = customerId;
    this.cardNumber = cardNumber;
    this.cardType = cardType;
    this.creditLimit = creditLimit;
    this.availableCredit = availableCredit;
    this.outstandingAmount = outstandingAmount;
    this.expiryDate = expiryDate;
    this.cardStatus = cardStatus;
    this.issuedAt = issuedAt;
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

public String getCardNumber() {
    return cardNumber;
}

public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
}

public String getCardType() {
    return cardType;
}

public void setCardType(String cardType) {
    this.cardType = cardType;
}

public BigDecimal getCreditLimit() {
    return creditLimit;
}

public void setCreditLimit(BigDecimal creditLimit) {
    this.creditLimit = creditLimit;
}

public BigDecimal getAvailableCredit() {
    return availableCredit;
}

public void setAvailableCredit(BigDecimal availableCredit) {
    this.availableCredit = availableCredit;
}

public BigDecimal getOutstandingAmount() {
    return outstandingAmount;
}

public void setOutstandingAmount(BigDecimal outstandingAmount) {
    this.outstandingAmount = outstandingAmount;
}

public LocalDate getExpiryDate() {
    return expiryDate;
}

public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
}

public String getCardStatus() {
    return cardStatus;
}

public void setCardStatus(String cardStatus) {
    this.cardStatus = cardStatus;
}

public LocalDateTime getIssuedAt() {
    return issuedAt;
}

public void setIssuedAt(LocalDateTime issuedAt) {
    this.issuedAt = issuedAt;
}

@Override
public String toString() {
    return "CreditCard{" +
            "cardId=" + cardId +
            ", customerId=" + customerId +
            ", cardNumber='" + cardNumber + '\'' +
            ", cardType='" + cardType + '\'' +
            ", creditLimit=" + creditLimit +
            ", availableCredit=" + availableCredit +
            ", outstandingAmount=" + outstandingAmount +
            ", expiryDate=" + expiryDate +
            ", cardStatus='" + cardStatus + '\'' +
            ", issuedAt=" + issuedAt +
            '}';
}


}
