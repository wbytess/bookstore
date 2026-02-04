package com.bnp.bookstore.exception;

public class EmptyCartException extends RuntimeException {
    private static final long serialVersionUID = -9132795217142388494L;

	public EmptyCartException(String message) {
        super(message);
    }
}

