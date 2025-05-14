package com.example.userBalanceApp.api;

import com.example.userBalanceApp.dto.TransferDto;
import com.example.userBalanceApp.service.BalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/balance")
public class BalanceController {

    private final BalanceService balanceService;

    @PutMapping("/transfer")
    public void transferValueToClient(@RequestBody @Valid TransferDto dto) {
        balanceService.transferValueToClient(dto);
    }

}
