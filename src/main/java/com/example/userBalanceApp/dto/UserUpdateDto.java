package com.example.userBalanceApp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель обновления информации пользователя")
public class UserUpdateDto {

    @NotNull(message = "Необходим список адресов почт")
    @NotEmpty(message = "Необходим список адресов почт")
    @Schema(description = "Список всех адресов почт", example = "{'my_email@mail.ru', 'my_new_email@mail.ru'}")
    private List<String> emailData;

    @NotNull(message = "Необходим список номеров")
    @NotEmpty(message = "Необходим список номеров")
    @Schema(description = "Список всех номеров", example = "{'79207865432', '79347865430'}")
    private List<String> phoneData;

}
