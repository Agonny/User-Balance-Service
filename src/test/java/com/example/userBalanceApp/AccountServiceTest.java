package com.example.userBalanceApp;

import com.example.userBalanceApp.dao.AccountRepository;
import com.example.userBalanceApp.dto.TransferDto;
import com.example.userBalanceApp.exception.InvalidReceiverException;
import com.example.userBalanceApp.exception.NegativeBalanceException;
import com.example.userBalanceApp.exception.NegativeTransferException;
import com.example.userBalanceApp.mapper.UserMapper;
import com.example.userBalanceApp.model.Account;
import com.example.userBalanceApp.model.User;
import com.example.userBalanceApp.service.impl.BalanceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BalanceServiceImpl balanceService;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    private final BigDecimal firstBalance = BigDecimal.valueOf(210.14);

    private final BigDecimal secondBalance = BigDecimal.valueOf(258.941);

    @BeforeEach
    public void prepareRepository() {
        User user1 = User.builder()
                .id(1L)
                .name("user1")
                .dateOfBirth(LocalDate.now())
                .password("some_password").build();

        UserDetails userDetails = userMapper.toAuthUser(user1);
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
    }

    @Test
    @Order(1)
    public void checkTransferToClient() {
        Account account1 = Account.builder()
                .initialBalance(firstBalance)
                .balance(new BigDecimal(firstBalance.doubleValue()))
                .build();

        Account account2 = Account.builder()
                .initialBalance(secondBalance)
                .balance(new BigDecimal(secondBalance.doubleValue()))
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

        TransferDto dto = new TransferDto(BigDecimal.valueOf(50), 2L);

        balanceService.transferValueToClient(dto);
        assertEquals(account1.getBalance().doubleValue(), firstBalance.doubleValue() - 50);
        assertEquals(account2.getBalance().doubleValue(), secondBalance.doubleValue() + 50);
    }

    @Test
    @Order(2)
    public void checkNegativeTransfer() {
        TransferDto dto = new TransferDto(BigDecimal.valueOf(-50), 2L);

        Assertions.assertEquals(assertThrows(NegativeTransferException.class,
                () -> balanceService.transferValueToClient(dto)).getClass(),
                NegativeTransferException.class);
    }

    @Test
    @Order(3)
    public void checkEnormousBigTransfer() {
        Account account1 = Account.builder()
                .initialBalance(firstBalance)
                .balance(new BigDecimal(firstBalance.doubleValue()))
                .build();

        Account account2 = Account.builder()
                .initialBalance(secondBalance)
                .balance(new BigDecimal(secondBalance.doubleValue()))
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

        TransferDto dto = new TransferDto(BigDecimal.valueOf(450.11), 2L);

        Assertions.assertEquals(assertThrows(NegativeBalanceException.class,
                        () -> balanceService.transferValueToClient(dto)).getClass(),
                NegativeBalanceException.class);
    }

    @Test
    @Order(4)
    public void checkTransferWithWrongClientId() {
        Account account1 = Account.builder()
                .initialBalance(firstBalance)
                .balance(new BigDecimal(firstBalance.doubleValue()))
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(3L)).thenReturn(Optional.empty());

        TransferDto dto = new TransferDto(BigDecimal.valueOf(50.11), 3L);

        Assertions.assertEquals(assertThrows(InvalidReceiverException.class,
                        () -> balanceService.transferValueToClient(dto)).getClass(),
                InvalidReceiverException.class);
    }

}
