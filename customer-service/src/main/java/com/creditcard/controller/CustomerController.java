package com.creditcard.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.creditcard.dto.CustomerRequest;
import com.creditcard.dto.CustomerResponse;
import com.creditcard.service.CustomerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
//	1. Create Customer

	@PostMapping
	public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {

		CustomerResponse response = customerService.createCustomer(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
//	2. Get Customer by ID

	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long customerId) {

		return ResponseEntity.ok(customerService.getCustomerById(customerId));
	}
	
//	3. Get All Customers

	@GetMapping
	public ResponseEntity<List<CustomerResponse>> getAllCustomers() {

		return ResponseEntity.ok(customerService.getAllCustomers());
	}
	
//	4. Update Entire Customer by ID

	@PutMapping("/{customerId}")
	public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long customerId,
			@Valid @RequestBody CustomerRequest request) {

		return ResponseEntity.ok(customerService.updateCustomer(customerId, request));
	}
	
//	5. Delete Customer by ID

	@DeleteMapping("/{customerId}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {

		customerService.deleteCustomer(customerId);

		return ResponseEntity.noContent().build();
	}
	
//	6. Search Entire Customer by Name

	@GetMapping("/search")
	public ResponseEntity<List<CustomerResponse>> searchCustomers(@RequestParam String keyword) {

		return ResponseEntity.ok(customerService.searchCustomers(keyword));
	}
}