package com.creditcard.exception;

public class MerchantNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MerchantNotFoundException(Long merchantId) {
		super("Merchant not found with ID: " + merchantId);
	}
}