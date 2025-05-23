package com.example.userBalanceApp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель перевода")
public class TransferDto {

    @Schema(description = "Величина перевода", example = "my_email@mail.ru")
    @NotNull(message = "Величина перевода не может быть пустой")
    private BigDecimal value;

    @Schema(description = "ID получателя", example = "4")
    @NotNull(message = "ID получателя не может быть пустым")
    private Long transferTo;

}
