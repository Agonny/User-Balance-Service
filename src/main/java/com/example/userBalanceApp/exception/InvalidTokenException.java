package com.example.userBalanceApp.exception;

import com.example.userBalanceApp.constant.ExceptionMessages;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ServiceException{

    public InvalidTokenException() {
        super(ExceptionMessages.INVALID_CREDENTIALS.getValue());
        this.statusCode = HttpStatus.UNAUTHORIZED;
    }

}
