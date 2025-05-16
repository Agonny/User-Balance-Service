package com.example.userBalanceApp.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Параметры для фильтрации пользователей")
public class UserFilter {

    @Schema(description = "Дата рождения", example = " 01.07.1998")
    private LocalDate dateOfBirth;

    @Size(max = 13, message = "Длина номера телефона не может превышать 13 символов")
    @Schema(description = "Номер телефона", example = "79207865432")
    private String phone;

    @Size(max = 500, message = "Длина имени не может превышать 500 символов")
    @Schema(description = "Имя пользователя", example = "newuser")
    private String name;

    @Size(max = 200, message = "Длина адреса почты не может превышать 200 символов")
    @Schema(description = "Адрес почты", example = "my_email@mail.ru")
    private String email;

}
