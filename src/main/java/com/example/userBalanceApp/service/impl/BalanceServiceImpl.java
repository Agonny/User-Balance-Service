package com.example.userBalanceApp.service.impl;

import com.example.userBalanceApp.dao.postgres.AccountRepository;
import com.example.userBalanceApp.dto.TransferDto;
import com.example.userBalanceApp.exception.InvalidReceiverException;
import com.example.userBalanceApp.exception.NegativeBalanceException;
import com.example.userBalanceApp.exception.NegativeTransferException;
import com.example.userBalanceApp.model.Account;
import com.example.userBalanceApp.service.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class BalanceServiceImpl implements BalanceService {

    private final AccountRepository accountRepository;

    private final long INCREASE_DELAY = 30000;

    private final double MAXIMUM_DEPOSIT_INCREASE = 2.07;

    private final double PERIODIC_DEPOSIT_INCREASE = 1.10;

    @Async
    @Override
    public void transferValueToClient(TransferDto dto) {
        if(dto.getValue().doubleValue() <= 0) throw new NegativeTransferException();

        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        log.info(id.toString());
        Account accountFrom = accountRepository.findById(id).orElseThrow();
        Account accountTo = accountRepository.findById(dto.getTransferTo()).orElseThrow(InvalidReceiverException::new);

        BigDecimal newBalance = accountFrom.getBalance().subtract(dto.getValue());
        if(newBalance.doubleValue() <= 0) throw new NegativeBalanceException();

        accountFrom.setBalance(accountFrom.getBalance().subtract(dto.getValue()));
        accountTo.setBalance(accountTo.getBalance().add(dto.getValue()));

        accountRepository.saveAll(List.of(accountFrom, accountTo));
    }

    @Scheduled(fixedDelay = INCREASE_DELAY)
    public void periodicBalanceIncrease() {
        Set<Account> updatedAccounts = Collections.synchronizedSet(new HashSet<>());
        List<Account> accounts = accountRepository.findAll();

        for(Account account : accounts) {
            if(account.getMaxIncrementBalance() == null) {
                account.setMaxIncrementBalance(account.getInitialBalance().multiply(BigDecimal.valueOf(MAXIMUM_DEPOSIT_INCREASE)));

                updatedAccounts.add(account);
            }

            if(account.getMaxIncrementBalance().min(account.getBalance()).equals(account.getBalance())){
                BigDecimal newBalance = account.getBalance().multiply(BigDecimal.valueOf(PERIODIC_DEPOSIT_INCREASE));

                if(newBalance.max(account.getMaxIncrementBalance()).equals(newBalance)) {
                    newBalance = BigDecimal.valueOf(account.getMaxIncrementBalance().doubleValue());
                }
                account.setBalance(newBalance);

                updatedAccounts.add(account);
            }
        }

        accountRepository.saveAll(updatedAccounts);
    }

}
