package com.example.userBalanceApp.exception;

import com.example.userBalanceApp.constant.ExceptionMessages;
import org.springframework.http.HttpStatus;

public class NegativeBalanceException extends ServiceException {

    public NegativeBalanceException() {
        super(ExceptionMessages.NEGATIVE_BALANCE.getValue());
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
