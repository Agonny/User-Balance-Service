package com.example.userBalanceApp.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FieldName {

    ID("id"),
    DATE_OF_BIRTH("dateOfBirth"),
    NAME("name"),
    EMAIL("email"),
    EMAIL_DATA("emailData"),
    PHONE("phone"),
    PHONE_DATA("phoneData");

    private String value;

}
