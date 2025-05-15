package com.example.userBalanceApp.security;

import com.example.userBalanceApp.exception.MissingTokenException;
import com.example.userBalanceApp.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(AUTHORIZATION_HEADER);

        if(token == null || token.substring(BEARER_PREFIX.length()).isEmpty()) throw new MissingTokenException();

        authService.authorize(request, response, token.substring(BEARER_PREFIX.length()));

        filterChain.doFilter(request, response);
    }
}
