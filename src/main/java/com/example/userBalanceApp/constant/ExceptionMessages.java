package com.example.userBalanceApp.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {

    INVALID_RECEIVER("Can't found receiver with current ID"),
    NEGATIVE_BALANCE("Transfer sum should be lesser than the balance of sender"),
    NEGATIVE_TRANSFER("Transfer sum should be a positive number"),
    UNEXPECTED_ERROR("An unexpected error occurred");

    private String value;

}
