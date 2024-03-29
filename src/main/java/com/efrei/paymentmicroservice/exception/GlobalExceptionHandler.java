package com.efrei.paymentmicroservice.exception;

import com.efrei.paymentmicroservice.exception.custom.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JWTException.class)
    protected ResponseEntity<Object> handleJWTException(
            JWTException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(WrongUserRoleException.class)
    protected ResponseEntity<Object> handleWrongUserRole(
            WrongUserRoleException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }
    @ExceptionHandler(PaymentAttemptNotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(
            PaymentAttemptNotFoundException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({
            EmailSenderMicroserviceException.class,
            UserMicroserviceException.class,
            SessionMicroserviceException.class
    })
    protected ResponseEntity<Object> handleMicroserviceCallError(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage() + "\n" + ex.getCause();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
