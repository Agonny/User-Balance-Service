package com.example.userBalanceApp;

import com.example.userBalanceApp.dao.AccountRepository;
import com.example.userBalanceApp.dao.UserRepository;
import com.example.userBalanceApp.model.Account;
import com.example.userBalanceApp.model.EmailData;
import com.example.userBalanceApp.model.PhoneData;
import com.example.userBalanceApp.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class RepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @Order(value = 1)
    @Transactional
    public void checkUserSaving() {
        BigDecimal firstBalance = BigDecimal.valueOf(210.14);
        BigDecimal secondBalance = BigDecimal.valueOf(258.941);

        User user1 = User.builder()
                .name("user1")
                .dateOfBirth(LocalDate.now())
                .password("some_password").build();

        EmailData emailData1 = EmailData.builder().email("some_email@g.com").user(user1).build();
        EmailData emailData2 = EmailData.builder().email("some_new_email@g.com").user(user1).build();

        PhoneData phoneData1 = PhoneData.builder().phone("499494949").user(user1).build();

        Account account1 = Account.builder()
                .initialBalance(firstBalance)
                .balance(firstBalance)
                .user(user1).build();

        user1.setEmailData(new LinkedHashSet<>(List.of(emailData1, emailData2)));
        user1.setPhoneData(new LinkedHashSet<>(List.of(phoneData1)));
        user1.setAccount(account1);

        User user2 = User.builder()
                .name("user2")
                .dateOfBirth(LocalDate.now())
                .password("some_new_password").build();

        EmailData emailData3 = EmailData.builder().email("another_email@g.com").user(user2).build();
        EmailData emailData4 = EmailData.builder().email("another_new_email@g.com").user(user2).build();

        PhoneData phoneData2 = PhoneData.builder().phone("9494949494").user(user2).build();

        Account account2 = Account.builder()
                .initialBalance(secondBalance)
                .balance(secondBalance)
                .user(user2).build();

        user2.setEmailData(new LinkedHashSet<>(List.of(emailData3, emailData4)));
        user2.setPhoneData(new LinkedHashSet<>(List.of(phoneData2)));
        user2.setAccount(account2);

        List<User> persistedUsers = userRepository.saveAll(List.of(user1, user2));

        Assertions.assertNotNull(persistedUsers);
        Assertions.assertNotNull(persistedUsers.get(0));
        Assertions.assertNotNull(persistedUsers.get(1));

        Assertions.assertEquals(accountRepository.findById(1L).get().getBalance(), firstBalance);
        Assertions.assertEquals(accountRepository.findById(2L).get().getBalance(), secondBalance);
    }

}
