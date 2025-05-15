package com.example.userBalanceApp.exception;

import com.example.userBalanceApp.constant.ExceptionMessages;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ServiceException {

    public InvalidCredentialsException() {
        super(ExceptionMessages.INVALID_CREDENTIALS.getValue());
        this.statusCode = HttpStatus.UNAUTHORIZED;
    }

}
