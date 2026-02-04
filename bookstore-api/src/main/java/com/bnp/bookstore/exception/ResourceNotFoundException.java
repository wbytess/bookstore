package com.bnp.bookstore.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4673348848118774036L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
