package com.example.userBalanceApp.exception;

import com.example.userBalanceApp.constant.ExceptionMessages;
import org.springframework.http.HttpStatus;

public class MissingTokenException extends ServiceException {

    public MissingTokenException() {
        super(ExceptionMessages.MISSING_TOKEN.getValue());
        this.statusCode = HttpStatus.UNAUTHORIZED;
    }

}
