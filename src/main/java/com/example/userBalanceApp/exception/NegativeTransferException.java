package com.example.userBalanceApp.exception;

import com.example.userBalanceApp.constant.ExceptionMessages;
import org.springframework.http.HttpStatus;

public class NegativeTransferException extends ServiceException {

    public NegativeTransferException() {
        super(ExceptionMessages.NEGATIVE_TRANSFER.getValue());
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
