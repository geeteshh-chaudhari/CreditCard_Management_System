package com.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transaction.client.CardServiceClient;
import com.transaction.client.MerchantServiceClient;
import com.transaction.dto.CreditCardResponse;
import com.transaction.dto.TransactionRequest;
import com.transaction.dto.TransactionResponse;
import com.transaction.entity.Transaction;
import com.transaction.exception.InsufficientBalance;
import com.transaction.exception.TransactionNotFound;
import com.transaction.repository.TransactionRepository;

@Service
@Transactional
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final CardServiceClient cardServiceClient;
	private final MerchantServiceClient merchantServiceClient;

	public TransactionService(TransactionRepository transactionRepository, CardServiceClient cardServiceClient,
			MerchantServiceClient merchantServiceClient) {

		this.transactionRepository = transactionRepository;
		this.cardServiceClient = cardServiceClient;
		this.merchantServiceClient = merchantServiceClient;
	}

	// 1. Make a purchase
	public TransactionResponse makePurchase(TransactionRequest request) {

		CreditCardResponse card;

		try {
			card = cardServiceClient.getCardById(request.getCardId());
		} catch (Exception ex) {

			saveFailedTransaction(request, "Credit card not found");

			throw new IllegalArgumentException("Credit card not found");
		}

		// Check whether card belongs to customer
		if (!card.getCustomerId().equals(request.getCustomerId())) {

			saveFailedTransaction(request, "Card does not belong to customer");

			throw new IllegalArgumentException("Card does not belong to customer");
		}

		// Purchase allowed only on ACTIVE card
		if (!"ACTIVE".equalsIgnoreCase(card.getCardStatus())) {

			saveFailedTransaction(request, "Credit card is not ACTIVE");

			throw new IllegalArgumentException("Purchases are allowed only on ACTIVE cards");
		}

		// Merchant is required for purchase
		if (request.getMerchantId() == null) {

			saveFailedTransaction(request, "Merchant ID is required for purchase");

			throw new IllegalArgumentException("Merchant ID is required for purchase");
		}

		// Validate merchant exists
		try {

			merchantServiceClient.getMerchantById(request.getMerchantId());

		} catch (Exception ex) {

			saveFailedTransaction(request, "Merchant not found");

			throw new IllegalArgumentException("Merchant not found");
		}

		// Check available credit
		if (card.getAvailableCredit().compareTo(request.getAmount()) < 0) {

			saveFailedTransaction(request, "Insufficient available credit");

			throw new InsufficientBalance("Insufficient available credit");
		}

		BigDecimal newAvailableCredit = card.getAvailableCredit().subtract(request.getAmount());

		BigDecimal newOutstandingAmount = card.getOutstandingAmount().add(request.getAmount());

		// Update balance in Credit Card MS
		try {

			cardServiceClient.updateBalance(card.getCardId(), newAvailableCredit, newOutstandingAmount);

		} catch (Exception ex) {

			saveFailedTransaction(request, "Unable to update credit card balance");

			throw new IllegalArgumentException("Unable to update credit card balance");
		}

		// Record successful purchase
		Transaction transaction = createTransaction(request, "PURCHASE", "SUCCESS", null);

		Transaction savedTransaction = transactionRepository.save(transaction);

		return mapToResponse(savedTransaction);
	}

	// 2. Make payment
	public TransactionResponse makePayment(TransactionRequest request) {

		CreditCardResponse card;

		try {

			card = cardServiceClient.getCardById(request.getCardId());

		} catch (Exception ex) {

			saveFailedTransaction(request, "Credit card not found");

			throw new IllegalArgumentException("Credit card not found");
		}

		// Check whether card belongs to customer
		if (!card.getCustomerId().equals(request.getCustomerId())) {

			saveFailedTransaction(request, "Card does not belong to customer");

			throw new IllegalArgumentException("Card does not belong to customer");
		}

		// Payment cannot exceed outstanding amount
		if (request.getAmount().compareTo(card.getOutstandingAmount()) > 0) {

			saveFailedTransaction(request, "Payment amount exceeds outstanding balance");

			throw new InsufficientBalance("Payment amount cannot exceed outstanding balance");
		}

		BigDecimal newOutstandingAmount = card.getOutstandingAmount().subtract(request.getAmount());

		BigDecimal newAvailableCredit = card.getAvailableCredit().add(request.getAmount());

		// Update card balance
		try {

			cardServiceClient.updateBalance(card.getCardId(), newAvailableCredit, newOutstandingAmount);

		} catch (Exception ex) {

			saveFailedTransaction(request, "Unable to update credit card balance");

			throw new IllegalArgumentException("Unable to update credit card balance");
		}

		// Record successful payment
		Transaction transaction = createTransaction(request, "PAYMENT", "SUCCESS", null);

		Transaction savedTransaction = transactionRepository.save(transaction);

		return mapToResponse(savedTransaction);
	}

	// 3. Get transaction by ID
	@Transactional(readOnly = true)
	public TransactionResponse getTransactionById(Long transactionId) {

		Transaction transaction = transactionRepository.findById(transactionId)
				.orElseThrow(() -> new TransactionNotFound("Transaction not found with ID: " + transactionId));

		return mapToResponse(transaction);
	}

	// 4. Get complete transaction history
	// Reports MS can consume this endpoint.
	@Transactional(readOnly = true)
	public List<TransactionResponse> getAllTransactions() {

		return transactionRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	// Create transaction entity
	private Transaction createTransaction(TransactionRequest request, String transactionType, String transactionStatus,
			String failureReason) {

		Transaction transaction = new Transaction();

		transaction.setCardId(request.getCardId());

		transaction.setCustomerId(request.getCustomerId());

		transaction.setMerchantId(request.getMerchantId());

		transaction.setTransactionType(transactionType);

		transaction.setAmount(request.getAmount());

		transaction.setTransactionDate(LocalDateTime.now());

		transaction.setTransactionStatus(transactionStatus);

		transaction.setFailureReason(failureReason);

		return transaction;
	}

	// Save failed transaction
	private void saveFailedTransaction(TransactionRequest request, String failureReason) {

		Transaction transaction = createTransaction(request, request.getTransactionType(), "FAILED", failureReason);

		transactionRepository.save(transaction);
	}

	// Entity -> Response DTO
	private TransactionResponse mapToResponse(Transaction transaction) {

		TransactionResponse response = new TransactionResponse();

		response.setTransactionId(transaction.getTransactionId());

		response.setCardId(transaction.getCardId());

		response.setCustomerId(transaction.getCustomerId());

		response.setMerchantId(transaction.getMerchantId());

		response.setTransactionType(transaction.getTransactionType());

		response.setAmount(transaction.getAmount());

		response.setTransactionDate(transaction.getTransactionDate());

		response.setTransactionStatus(transaction.getTransactionStatus());

		response.setFailureReason(transaction.getFailureReason());

		return response;
	}
}