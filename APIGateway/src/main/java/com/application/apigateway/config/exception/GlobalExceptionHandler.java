package com.application.apigateway.config.exception;


import com.application.apigateway.dto.CommonResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<CommonResponse> badCredentialsException(BadCredentialsException ex) {
        printException(ex);
        CommonResponse commonResponse = new CommonResponse(400, false, ex.getMessage(), null);
        return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<CommonResponse> userAlreadyExistsException(UserAlreadyExistsException ex) {
        printException(ex);
        CommonResponse commonResponse = new CommonResponse(400, false, ex.getMessage(), null);
        return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ClientSideException.class)
    public ResponseEntity<CommonResponse> clientCommonException(ClientSideException ex) {
        printException(ex);
        CommonResponse commonResponse = new CommonResponse(400, false, ex.getMessage(), null);
        return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<CommonResponse> commonException(Exception ex) {
        printException(ex);
        return checkCommonException(ex);
    }

    private ResponseEntity<CommonResponse> checkCommonException(Exception exception) {
        CommonResponse commonResponse = new CommonResponse(500, false, "Unknown internal server error.. Please try again later.", null);

        if (exception instanceof TooManyReqException) {
            commonResponse = new CommonResponse(HttpStatus.TOO_MANY_REQUESTS.value(), false, HttpStatus.TOO_MANY_REQUESTS.name(), null);
            return new ResponseEntity<>(commonResponse, HttpStatus.TOO_MANY_REQUESTS);
        }

        if (exception instanceof BadCredentialsException) {
            commonResponse = new CommonResponse(401, false, "The username or password is incorrect.", null);
            return new ResponseEntity<>(commonResponse, HttpStatus.UNAUTHORIZED);
        }

        if (exception instanceof AccountStatusException) {
            commonResponse = new CommonResponse(403, false, "The account is locked.", null);
            return new ResponseEntity<>(commonResponse, HttpStatus.FORBIDDEN);
        }

        if (exception instanceof AccessDeniedException) {
            commonResponse = new CommonResponse(403, false, "You are not authorized to access this resource.", null);
            return new ResponseEntity<>(commonResponse, HttpStatus.FORBIDDEN);
        }

        if (exception instanceof SignatureException) {
            commonResponse = new CommonResponse(403, false, "The JWT signature is invalid.", null);
            return new ResponseEntity<>(commonResponse, HttpStatus.FORBIDDEN);
        }

        if (exception instanceof ExpiredJwtException) {
            commonResponse = new CommonResponse(403, false, "The JWT token has expired.", null);
            return new ResponseEntity<>(commonResponse, HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(commonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void printException(Exception ex) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        printWriter.flush();
        String stackTrace = writer.toString();
        log.error(stackTrace);
    }
}
