package com.creditcard.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.creditcard.client.CustomerServiceClient;
import com.creditcard.dto.CreditCardRequest;
import com.creditcard.dto.CreditCardResponse;
import com.creditcard.entity.CreditCard;
import com.creditcard.exception.CreditCardNotFoundException;
import com.creditcard.exception.CustomerNotFoundException;
import com.creditcard.repository.CreditCardRepository;

import feign.FeignException;

@Service
@Transactional
public class CreditCardService {

	private final CreditCardRepository creditCardRepository;
	private final CustomerServiceClient customerServiceClient;

	public CreditCardService(CreditCardRepository creditCardRepository, CustomerServiceClient customerServiceClient) {

		this.creditCardRepository = creditCardRepository;
		this.customerServiceClient = customerServiceClient;
	}

	// 1. Issue new credit card
	public CreditCardResponse issueCard(CreditCardRequest request) {

		if (creditCardRepository.existsByCardNumber(request.getCardNumber())) {

			throw new IllegalArgumentException("Card number already exists");
		}

		// Check whether customer exists
		customerServiceClient.checkCustomerExists(request.getCustomerId());

		validateBalances(request.getCreditLimit(), request.getAvailableCredit(), request.getOutstandingAmount());

		CreditCard creditCard = new CreditCard();

		creditCard.setCustomerId(request.getCustomerId());
		creditCard.setCardNumber(request.getCardNumber());
		creditCard.setCardType(request.getCardType());
		creditCard.setCreditLimit(request.getCreditLimit());
		creditCard.setAvailableCredit(request.getAvailableCredit());
		creditCard.setOutstandingAmount(request.getOutstandingAmount());
		creditCard.setExpiryDate(request.getExpiryDate());
		creditCard.setCardStatus(request.getCardStatus());

		CreditCard savedCard = creditCardRepository.save(creditCard);

		return mapToResponse(savedCard);
	}

	// 2. Get card by ID
	@Transactional(readOnly = true)
	public CreditCardResponse getCardById(Long cardId) {

		CreditCard card = getEntityById(cardId);

		return mapToResponse(card);
	}

	// 3. Get card by card number
	@Transactional(readOnly = true)
	public CreditCardResponse getCardByCardNumber(String cardNumber) {

		CreditCard card = creditCardRepository.findByCardNumber(cardNumber)
				.orElseThrow(() -> new CreditCardNotFoundException("Credit card not found with number: " + cardNumber));

		return mapToResponse(card);
	}

	// 4. Get all cards
	@Transactional(readOnly = true)
	public List<CreditCardResponse> getAllCards() {

		return creditCardRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	// 5. Get cards belonging to a customer
	@Transactional(readOnly = true)
	public List<CreditCardResponse> getCardsByCustomerId(Long customerId) {

		return creditCardRepository.findByCustomerId(customerId).stream().map(this::mapToResponse).toList();
	}

	// 6. Update card
	public CreditCardResponse updateCard(Long cardId, CreditCardRequest request) {

		CreditCard existingCard = getEntityById(cardId);

		if (!existingCard.getCustomerId().equals(request.getCustomerId())) {

			customerServiceClient.checkCustomerExists(request.getCustomerId());

			existingCard.setCustomerId(request.getCustomerId());
		}

		validateBalances(request.getCreditLimit(), request.getAvailableCredit(), request.getOutstandingAmount());

		existingCard.setCardType(request.getCardType());

		existingCard.setCreditLimit(request.getCreditLimit());

		existingCard.setAvailableCredit(request.getAvailableCredit());

		existingCard.setOutstandingAmount(request.getOutstandingAmount());

		existingCard.setExpiryDate(request.getExpiryDate());

		existingCard.setCardStatus(request.getCardStatus());

		CreditCard updatedCard = creditCardRepository.save(existingCard);

		return mapToResponse(updatedCard);
	}

	// 7. Block card
	public CreditCardResponse blockCard(Long cardId) {

		CreditCard card = getEntityById(cardId);

		card.setCardStatus("BLOCKED");

		CreditCard updatedCard = creditCardRepository.save(card);

		return mapToResponse(updatedCard);
	}

	// 8. Unblock card
	public CreditCardResponse unblockCard(Long cardId) {

		CreditCard card = getEntityById(cardId);

		if ("CLOSED".equals(card.getCardStatus())) {

			throw new IllegalArgumentException("A CLOSED credit card cannot be unblocked");
		}

		card.setCardStatus("ACTIVE");

		CreditCard updatedCard = creditCardRepository.save(card);

		return mapToResponse(updatedCard);
	}

	// 9. Check card status
	@Transactional(readOnly = true)
	public String checkCardStatus(Long cardId) {

		CreditCard card = getEntityById(cardId);

		return card.getCardStatus();
	}

	// 10. Check available credit
	@Transactional(readOnly = true)
	public BigDecimal checkAvailableCredit(Long cardId) {

		CreditCard card = getEntityById(cardId);

		return card.getAvailableCredit();
	}

	// 11. Update available credit and outstanding amount
	// This will be called by Transaction MS.
	public CreditCardResponse updateBalance(Long cardId, BigDecimal availableCredit, BigDecimal outstandingAmount) {

		CreditCard card = getEntityById(cardId);

		validateBalances(card.getCreditLimit(), availableCredit, outstandingAmount);

		card.setAvailableCredit(availableCredit);
		card.setOutstandingAmount(outstandingAmount);

		CreditCard updatedCard = creditCardRepository.save(card);

		return mapToResponse(updatedCard);
	}

	// Internal card lookup
	private CreditCard getEntityById(Long cardId) {

		return creditCardRepository.findById(cardId)
				.orElseThrow(() -> new CreditCardNotFoundException("Credit card not found with ID: " + cardId));
	}

	// Validate card balances
	private void validateBalances(BigDecimal creditLimit, BigDecimal availableCredit, BigDecimal outstandingAmount) {

		if (creditLimit == null || availableCredit == null || outstandingAmount == null) {

			throw new IllegalArgumentException(
					"Credit limit, available credit, " + "and outstanding amount are required");
		}

		if (creditLimit.compareTo(BigDecimal.ZERO) <= 0) {

			throw new IllegalArgumentException("Credit limit must be greater than 0");
		}

		if (availableCredit.compareTo(BigDecimal.ZERO) < 0) {

			throw new IllegalArgumentException("Available credit cannot be negative");
		}

		if (outstandingAmount.compareTo(BigDecimal.ZERO) < 0) {

			throw new IllegalArgumentException("Outstanding amount cannot be negative");
		}

		BigDecimal total = availableCredit.add(outstandingAmount);

		if (total.compareTo(creditLimit) != 0) {

			throw new IllegalArgumentException("Available credit + outstanding amount " + "must equal credit limit");
		}
	}

	// Entity -> Response DTO
	private CreditCardResponse mapToResponse(CreditCard creditCard) {

		CreditCardResponse response = new CreditCardResponse();

		response.setCardId(creditCard.getCardId());

		response.setCustomerId(creditCard.getCustomerId());

		response.setCardNumber(creditCard.getCardNumber());

		response.setCardType(creditCard.getCardType());

		response.setCreditLimit(creditCard.getCreditLimit());

		response.setAvailableCredit(creditCard.getAvailableCredit());

		response.setOutstandingAmount(creditCard.getOutstandingAmount());

		response.setExpiryDate(creditCard.getExpiryDate());

		response.setCardStatus(creditCard.getCardStatus());

		response.setIssuedAt(creditCard.getIssuedAt());

		return response;
	}
}