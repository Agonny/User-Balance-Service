package com.example.userBalanceApp.dao.postgres;

import com.example.userBalanceApp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
