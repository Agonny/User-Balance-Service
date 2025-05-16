package com.example.userBalanceApp.exception;

import com.example.userBalanceApp.constant.ExceptionMessages;
import org.springframework.security.core.AuthenticationException;

public class MissingTokenException extends AuthenticationException {

    public MissingTokenException() {
        super(ExceptionMessages.MISSING_TOKEN.getValue());
    }

}
