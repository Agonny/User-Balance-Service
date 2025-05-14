package com.example.userBalanceApp.dao.postgres;

import com.example.userBalanceApp.model.UniqueData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniqueDataRepository <T extends UniqueData> extends JpaRepository<T, Long> {

}
