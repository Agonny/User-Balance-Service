package com.example.userBalanceApp.api;

import com.example.userBalanceApp.dto.UserDto;
import com.example.userBalanceApp.dto.UserUpdateDto;
import com.example.userBalanceApp.filter.UserFilter;
import com.example.userBalanceApp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PutMapping
    public void updateUserData(@RequestBody @Valid UserUpdateDto dto) {
        userService.updateUserData(dto);
    }

    @GetMapping
    public Page<UserDto> getUsersByFilter(Pageable pageable, @Valid UserFilter filter) {
        return userService.getUsersByFilter(pageable, filter);
    }

}
