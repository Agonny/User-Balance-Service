package com.example.userBalanceApp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferDto {

    private BigDecimal value;

    private Long transferTo;

}
