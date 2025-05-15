package com.example.userBalanceApp.exception;

import com.example.userBalanceApp.constant.ExceptionMessages;
import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends ServiceException {

    public ExpiredTokenException() {
        super(ExceptionMessages.EXPIRED_TOKEN.getValue());
        this.statusCode = HttpStatus.UNAUTHORIZED;
    }

}
