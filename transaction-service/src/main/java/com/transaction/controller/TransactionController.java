package com.transaction.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.dto.TransactionRequest;
import com.transaction.dto.TransactionResponse;
import com.transaction.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {

		this.transactionService = transactionService;
	}

	// 1. Make a purchase
	@PostMapping("/purchase")
	public ResponseEntity<TransactionResponse> makePurchase(@Valid @RequestBody TransactionRequest request) {

		TransactionResponse response = transactionService.makePurchase(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// 2. Make a payment
	@PostMapping("/payment")
	public ResponseEntity<TransactionResponse> makePayment(@Valid @RequestBody TransactionRequest request) {

		TransactionResponse response = transactionService.makePayment(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// 3. Get transaction by ID
	@GetMapping("/{transactionId}")
	public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long transactionId) {

		return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
	}

	// 4. Get complete transaction history
	// Reports MS can also use this endpoint.
	@GetMapping
	public ResponseEntity<List<TransactionResponse>> getAllTransactions() {

		return ResponseEntity.ok(transactionService.getAllTransactions());
	}
}