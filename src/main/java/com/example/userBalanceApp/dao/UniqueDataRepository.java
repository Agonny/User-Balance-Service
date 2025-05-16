package com.example.userBalanceApp.dao;

import com.example.userBalanceApp.model.UniqueData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UniqueDataRepository <T extends UniqueData> extends JpaRepository<T, Long> {

}
