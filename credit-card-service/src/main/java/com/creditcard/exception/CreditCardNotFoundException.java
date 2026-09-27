package com.creditcard.exception;

public class CreditCardNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CreditCardNotFoundException(String message) {
        super(message);
    }
}