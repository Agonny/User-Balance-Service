package com.example.userBalanceApp.service.impl;

import com.example.userBalanceApp.dao.postgres.AccountRepository;
import com.example.userBalanceApp.dto.TransferDto;
import com.example.userBalanceApp.exception.InvalidReceiverException;
import com.example.userBalanceApp.exception.NegativeBalanceException;
import com.example.userBalanceApp.exception.NegativeTransferException;
import com.example.userBalanceApp.model.Account;
import com.example.userBalanceApp.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final AccountRepository accountRepository;

    private final int MAXIMUM_DEPOSIT_INCREASE = 207;

    private final int PERIODIC_DEPOSIT_INCREASE = 110;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transferValueToClient(TransferDto dto) {
        if(dto.getValue().doubleValue() <= 0) throw new NegativeTransferException();

        Long id = 4L;
        Account accountFrom = accountRepository.findById(id).orElseThrow();
        Account accountTo = accountRepository.findById(dto.getTransferTo()).orElseThrow(InvalidReceiverException::new);

        BigDecimal newBalance = accountFrom.getBalance().subtract(dto.getValue());
        if(newBalance.doubleValue() <= 0) throw new NegativeBalanceException();

        accountFrom.setBalance(accountFrom.getBalance().subtract(dto.getValue()));
        accountTo.setBalance(accountTo.getBalance().add(dto.getValue()));

        accountRepository.saveAll(List.of(accountFrom, accountTo));
    }

}
