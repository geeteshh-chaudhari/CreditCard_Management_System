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
import com.creditcard.repository.CreditCardRepository;

@Service
@Transactional
public class CreditCardService {


private final CreditCardRepository creditCardRepository;
private final CustomerServiceClient customerServiceClient;

public CreditCardService(
        CreditCardRepository creditCardRepository,
        CustomerServiceClient customerServiceClient) {

    this.creditCardRepository = creditCardRepository;
    this.customerServiceClient = customerServiceClient;
}

// 1. Issue a new credit card
public CreditCardResponse issueCard(CreditCardRequest request) {

    // Check whether the card number already exists
    if (creditCardRepository.existsByCardNumber(request.getCardNumber())) {
        throw new IllegalArgumentException("Card number already exists");
    }

    // Validate customer through Customer Service
    customerServiceClient.checkCustomerExists(request.getCustomerId());

    // Validate balance equation
    validateBalances(
            request.getCreditLimit(),
            request.getAvailableCredit(),
            request.getOutstandingAmount()
    );

    CreditCard creditCard = new CreditCard();

    creditCard.setCustomerId(request.getCustomerId());
    creditCard.setCardNumber(request.getCardNumber());
    creditCard.setCardType(request.getCardType());
    creditCard.setCreditLimit(request.getCreditLimit());
    creditCard.setAvailableCredit(request.getAvailableCredit());
    creditCard.setOutstandingAmount(request.getOutstandingAmount());
    creditCard.setExpiryDate(request.getExpiryDate());

    // Database schema has DEFAULT 'ACTIVE'
    if (request.getCardStatus() == null || request.getCardStatus().isBlank()) {
        creditCard.setCardStatus("ACTIVE");
    } else {
        creditCard.setCardStatus(request.getCardStatus());
    }

    CreditCard savedCard = creditCardRepository.save(creditCard);

    return mapToResponse(savedCard);
}

// 2. Get credit card by ID
@Transactional(readOnly = true)
public CreditCardResponse getCardById(Long cardId) {

    CreditCard creditCard = creditCardRepository.findById(cardId)
            .orElseThrow(() ->
                    new CreditCardNotFoundException(
                            "Credit card not found with ID: " + cardId));

    return mapToResponse(creditCard);
}

// 3. Get credit card by card number
@Transactional(readOnly = true)
public CreditCardResponse getCardByCardNumber(String cardNumber) {

    CreditCard creditCard = creditCardRepository.findByCardNumber(cardNumber)
            .orElseThrow(() ->
                    new CreditCardNotFoundException(
                            "Credit card not found with number: " + cardNumber));

    return mapToResponse(creditCard);
}

// 4. Get all credit cards
@Transactional(readOnly = true)
public List<CreditCardResponse> getAllCards() {

    return creditCardRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .toList();
}

// 5. Get all cards belonging to a customer
@Transactional(readOnly = true)
public List<CreditCardResponse> getCardsByCustomerId(Long customerId) {

    return creditCardRepository.findByCustomerId(customerId)
            .stream()
            .map(this::mapToResponse)
            .toList();
}

// 6. Update credit card
public CreditCardResponse updateCard(
        Long cardId,
        CreditCardRequest request) {

    CreditCard existingCard = creditCardRepository.findById(cardId)
            .orElseThrow(() ->
                    new CreditCardNotFoundException(
                            "Credit card not found with ID: " + cardId));

    // Validate customer if customer ID is changed
    if (!existingCard.getCustomerId().equals(request.getCustomerId())) {
        customerServiceClient.checkCustomerExists(request.getCustomerId());
        existingCard.setCustomerId(request.getCustomerId());
    }

    // Card number is kept unchanged because it is the unique card identity.
    // We therefore intentionally do not update cardNumber here.

    validateBalances(
            request.getCreditLimit(),
            request.getAvailableCredit(),
            request.getOutstandingAmount()
    );

    existingCard.setCardType(request.getCardType());
    existingCard.setCreditLimit(request.getCreditLimit());
    existingCard.setAvailableCredit(request.getAvailableCredit());
    existingCard.setOutstandingAmount(request.getOutstandingAmount());
    existingCard.setExpiryDate(request.getExpiryDate());

    if (request.getCardStatus() == null || request.getCardStatus().isBlank()) {
        existingCard.setCardStatus("ACTIVE");
    } else {
        existingCard.setCardStatus(request.getCardStatus());
    }

    CreditCard updatedCard = creditCardRepository.save(existingCard);

    return mapToResponse(updatedCard);
}

// 7. Block credit card
public CreditCardResponse blockCard(Long cardId) {

    CreditCard card = getEntityById(cardId);

    card.setCardStatus("BLOCKED");

    return mapToResponse(creditCardRepository.save(card));
}

// 8. Unblock credit card
public CreditCardResponse unblockCard(Long cardId) {

    CreditCard card = getEntityById(cardId);

    // Only BLOCKED cards should normally be unblocked
    if ("CLOSED".equals(card.getCardStatus())) {
        throw new IllegalArgumentException(
                "A CLOSED credit card cannot be unblocked");
    }

    card.setCardStatus("ACTIVE");

    return mapToResponse(creditCardRepository.save(card));
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

// Find entity internally
private CreditCard getEntityById(Long cardId) {

    return creditCardRepository.findById(cardId)
            .orElseThrow(() ->
                    new CreditCardNotFoundException(
                            "Credit card not found with ID: " + cardId));
}

// Validate database balance constraint
private void validateBalances(
        BigDecimal creditLimit,
        BigDecimal availableCredit,
        BigDecimal outstandingAmount) {

    if (creditLimit == null
            || availableCredit == null
            || outstandingAmount == null) {

        throw new IllegalArgumentException(
                "Credit limit, available credit, and outstanding amount are required");
    }

    if (creditLimit.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException(
                "Credit limit must be greater than 0");
    }

    if (availableCredit.compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException(
                "Available credit cannot be negative");
    }

    if (outstandingAmount.compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException(
                "Outstanding amount cannot be negative");
    }

    BigDecimal total = availableCredit.add(outstandingAmount);

    if (total.compareTo(creditLimit) != 0) {
        throw new IllegalArgumentException(
                "Available credit + outstanding amount must equal credit limit");
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
