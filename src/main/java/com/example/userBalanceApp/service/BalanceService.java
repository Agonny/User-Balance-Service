package com.example.userBalanceApp.service;

import com.example.userBalanceApp.dto.TransferDto;

public interface BalanceService {

    void transferValueToClient(TransferDto dto);

}
