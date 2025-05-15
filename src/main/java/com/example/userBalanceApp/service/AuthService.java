package com.example.userBalanceApp.service;

import com.example.userBalanceApp.dto.AuthenticationDto;
import com.example.userBalanceApp.dto.JwtAuthenticationDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    void authorize(HttpServletRequest request, HttpServletResponse response, String token);

    JwtAuthenticationDto authenticate(AuthenticationDto dto);

}
