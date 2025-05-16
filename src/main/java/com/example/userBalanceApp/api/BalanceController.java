package com.example.userBalanceApp.api;

import com.example.userBalanceApp.dto.TransferDto;
import com.example.userBalanceApp.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/balance")
@Tag(name = "Баланс", description = "Операции с балансом аккаунта")
public class BalanceController {

    private final BalanceService balanceService;

    @PutMapping("/transfer")
    @Operation(summary = "Перевод средств", description = "Позволяет произвести перевод средств с одного аккаунта на другой")
    public void transferValueToClient(@RequestBody @Valid TransferDto dto) {
        balanceService.transferValueToClient(dto);
    }

}
