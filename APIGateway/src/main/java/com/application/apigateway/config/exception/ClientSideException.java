package com.application.apigateway.config.exception;

public class ClientSideException extends RuntimeException {
    public ClientSideException(String message) {
        super(message);
    }

    public ClientSideException(String message, Throwable cause) {
    }

}
