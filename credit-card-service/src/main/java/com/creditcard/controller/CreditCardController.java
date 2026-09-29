package com.creditcard.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.creditcard.dto.CreditCardRequest;
import com.creditcard.dto.CreditCardResponse;
import com.creditcard.service.CreditCardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cards")
public class CreditCardController {

	private final CreditCardService creditCardService;

	public CreditCardController(CreditCardService creditCardService) {

		this.creditCardService = creditCardService;
	}

	// 1. Issue a new credit card
	@PostMapping
	public ResponseEntity<CreditCardResponse> issueCard(@Valid @RequestBody CreditCardRequest request) {

		CreditCardResponse savedCard = creditCardService.issueCard(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedCard);
	}

	// 2. Get credit card by ID
	@GetMapping("/{cardId}")
	public ResponseEntity<CreditCardResponse> getCardById(@PathVariable Long cardId) {

		return ResponseEntity.ok(creditCardService.getCardById(cardId));
	}

	// 3. Get credit card by card number
	@GetMapping("/number/{cardNumber}")
	public ResponseEntity<CreditCardResponse> getCardByCardNumber(@PathVariable String cardNumber) {

		return ResponseEntity.ok(creditCardService.getCardByCardNumber(cardNumber));
	}

	// 4. Get all credit cards
	// Reports MS can use this endpoint.
	@GetMapping
	public ResponseEntity<List<CreditCardResponse>> getAllCards() {

		return ResponseEntity.ok(creditCardService.getAllCards());
	}

	// 5. Get all cards belonging to a customer
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<CreditCardResponse>> getCardsByCustomerId(@PathVariable Long customerId) {

		return ResponseEntity.ok(creditCardService.getCardsByCustomerId(customerId));
	}

	// 6. Update credit card
	@PutMapping("/{cardId}")
	public ResponseEntity<CreditCardResponse> updateCard(@PathVariable Long cardId,
			@Valid @RequestBody CreditCardRequest request) {

		return ResponseEntity.ok(creditCardService.updateCard(cardId, request));
	}

	// 7. Block credit card
	@PatchMapping("/{cardId}/block")
	public ResponseEntity<CreditCardResponse> blockCard(@PathVariable Long cardId) {

		return ResponseEntity.ok(creditCardService.blockCard(cardId));
	}

	// 8. Unblock credit card
	@PatchMapping("/{cardId}/unblock")
	public ResponseEntity<CreditCardResponse> unblockCard(@PathVariable Long cardId) {

		return ResponseEntity.ok(creditCardService.unblockCard(cardId));
	}

	// 9. Check card status
	@GetMapping("/{cardId}/status")
	public ResponseEntity<String> checkCardStatus(@PathVariable Long cardId) {

		return ResponseEntity.ok(creditCardService.checkCardStatus(cardId));
	}

	// 10. Check available credit
	@GetMapping("/{cardId}/credit")
	public ResponseEntity<BigDecimal> checkAvailableCredit(@PathVariable Long cardId) {

		return ResponseEntity.ok(creditCardService.checkAvailableCredit(cardId));
	}

	// 11. Update available credit and outstanding amount
	// Used by Transaction MS.
	@PatchMapping("/{cardId}/balance")
	public ResponseEntity<CreditCardResponse> updateBalance(@PathVariable Long cardId,
			@RequestParam BigDecimal availableCredit, @RequestParam BigDecimal outstandingAmount) {

		return ResponseEntity.ok(creditCardService.updateBalance(cardId, availableCredit, outstandingAmount));
	}
}