package com.transaction.exception;

public class TransactionNotFound extends RuntimeException{

	 private static final long serialVersionUID = 1L;

	    public TransactionNotFound(String message) {
	        super(message);
	    }
}
