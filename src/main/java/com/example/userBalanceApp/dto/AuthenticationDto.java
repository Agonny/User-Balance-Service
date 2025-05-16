package com.example.userBalanceApp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на аутентификацию")
public class AuthenticationDto {

    @Schema(description = "Данные пользователя", example = "my_email@mail.ru")
    @Size(max = 200, message = "Длина не может превышать 200 символов")
    @NotBlank(message = "Данные пользователя не могут быть пустыми")
    private String userData;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = 8, max = 500, message = "Длина пароля должна быть от 8 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

}
