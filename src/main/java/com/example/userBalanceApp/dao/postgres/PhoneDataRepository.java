package com.example.userBalanceApp.dao.postgres;

import com.example.userBalanceApp.model.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
}
