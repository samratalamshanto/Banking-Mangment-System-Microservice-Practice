package com.application.apigateway.config.exception;

public class TooManyReqException extends RuntimeException {
    public TooManyReqException(String message) {
        super(message);
    }
}
