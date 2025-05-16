package com.example.userBalanceApp;

import org.springframework.boot.SpringApplication;

public class TestUserBalanceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(UserBalanceServiceApplication::main)
                .with(TestcontainersConfiguration.class).run(args);
    }

}
