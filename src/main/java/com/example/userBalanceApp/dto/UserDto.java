package com.example.userBalanceApp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Schema(description = "Модель информации о пользователе")
public class UserDto {

    @Schema(description = "Имя пользователя", example = "newuser")
    private String name;

    @Schema(description = "Дата рождения", example = " 01.07.1998")
    private LocalDate dateOfBirth;

    @Schema(description = "Список всех адресов почт", example = "{'my_email@mail.ru', 'my_new_email@mail.ru'}")
    private List<String> emailData;

    @Schema(description = "Список всех номеров", example = "{'79207865432', '79347865430'}")
    private List<String> PhoneData;

}
