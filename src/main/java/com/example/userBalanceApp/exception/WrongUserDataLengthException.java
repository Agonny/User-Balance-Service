package com.example.userBalanceApp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WrongUserDataLengthException extends ServiceException {

    public WrongUserDataLengthException(String message) {
        super(message);
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
