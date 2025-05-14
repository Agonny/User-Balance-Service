package com.example.userBalanceApp.dao.postgres;

import com.example.userBalanceApp.model.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
}
