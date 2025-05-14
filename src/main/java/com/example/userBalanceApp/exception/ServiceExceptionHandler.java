package com.example.userBalanceApp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.example.userBalanceApp.constant.ExceptionMessages.UNEXPECTED_ERROR;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    ResponseEntity<Object> handleCustomConflict(ServiceException ex, WebRequest request) {
        return super.handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), ex.getStatusCode(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        return super.handleExceptionInternal(ex, UNEXPECTED_ERROR,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
