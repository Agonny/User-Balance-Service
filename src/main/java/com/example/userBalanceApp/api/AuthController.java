package com.example.userBalanceApp.api;

import com.example.userBalanceApp.dto.AuthenticationDto;
import com.example.userBalanceApp.dto.JwtAuthenticationDto;
import com.example.userBalanceApp.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Авторизация", description = "Содержит операции, связанные с верификацией пользователей")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/authenticate")
    @Operation(summary = "Аутентификация", description = "Позволяет получить доступ в систему")
    public JwtAuthenticationDto authenticate(@RequestBody @Valid AuthenticationDto dto) {
        return authService.authenticate(dto);
    }

}
