package com.application.apigateway.config.exception;

public class TokenMissingException extends RuntimeException {
    public TokenMissingException() {
    }

    public TokenMissingException(String message) {
        super(message);
    }
}
