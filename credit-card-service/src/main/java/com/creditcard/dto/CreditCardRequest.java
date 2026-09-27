package com.creditcard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreditCardRequest {


@NotNull(message = "Customer ID is required")
private Long customerId;

@NotBlank(message = "Card number is required")
@Pattern(
    regexp = "^[0-9]{13,19}$",
    message = "Card number must contain 13 to 19 digits"
)
private String cardNumber;

@NotBlank(message = "Card type is required")
@Pattern(
    regexp = "^(SILVER|GOLD|PLATINUM)$",
    message = "Card type must be SILVER, GOLD, or PLATINUM"
)
private String cardType;

@NotNull(message = "Credit limit is required")
@DecimalMin(
    value = "0.00",
    inclusive = false,
    message = "Credit limit must be greater than 0"
)
private BigDecimal creditLimit;

@NotNull(message = "Available credit is required")
@DecimalMin(
    value = "0.00",
    inclusive = true,
    message = "Available credit cannot be negative"
)
private BigDecimal availableCredit;

@NotNull(message = "Outstanding amount is required")
@DecimalMin(
    value = "0.00",
    inclusive = true,
    message = "Outstanding amount cannot be negative"
)
private BigDecimal outstandingAmount;

@NotNull(message = "Expiry date is required")
private LocalDate expiryDate;

@NotBlank(message = "Card status is required")
@Pattern(
    regexp = "^(ACTIVE|BLOCKED|CLOSED)$",
    message = "Card status must be ACTIVE, BLOCKED, or CLOSED"
)
private String cardStatus;

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


}
