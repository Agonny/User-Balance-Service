package com.example.userBalanceApp.exception;

import com.example.userBalanceApp.constant.ExceptionMessages;
import org.springframework.http.HttpStatus;

public class InvalidReceiverException extends ServiceException {

    public InvalidReceiverException() {
        super(ExceptionMessages.INVALID_RECEIVER.getValue());
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
