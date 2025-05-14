package com.example.userBalanceApp.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    private List<String> emailData;

    private List<String> phoneData;

}
