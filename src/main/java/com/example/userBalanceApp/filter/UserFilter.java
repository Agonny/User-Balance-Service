package com.example.userBalanceApp.filter;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserFilter {

    private LocalDate dateOfBirth;

    private String phone;

    private String name;

    private String email;

}
