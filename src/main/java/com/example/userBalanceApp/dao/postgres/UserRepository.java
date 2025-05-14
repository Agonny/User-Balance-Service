package com.example.userBalanceApp.dao.postgres;

import com.example.userBalanceApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {



}
