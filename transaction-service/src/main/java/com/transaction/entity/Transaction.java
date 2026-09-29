package com.transaction.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CARD_TRANSACTION")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRANSACTION_ID")
	private Long transactionId;

	@Column(name = "CARD_ID", nullable = false)
	private Long cardId;

	@Column(name = "CUSTOMER_ID", nullable = false)
	private Long customerId;

	@Column(name = "MERCHANT_ID")
	private Long merchantId;

	@Column(name = "TRANSACTION_TYPE", nullable = false, length = 10)
	private String transactionType;

	@Column(name = "AMOUNT", nullable = false, precision = 12, scale = 2)
	private BigDecimal amount;

	@Column(name = "TRANSACTION_DATE", nullable = false)
	private LocalDateTime transactionDate;

	@Column(name = "TRANSACTION_STATUS", nullable = false, length = 10)
	private String transactionStatus;

	@Column(name = "FAILURE_REASON", length = 250)
	private String failureReason;

	public Transaction() {
	}

	public Transaction(Long transactionId, Long cardId, Long customerId, Long merchantId, String transactionType,
			BigDecimal amount, LocalDateTime transactionDate, String transactionStatus, String failureReason) {

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
}