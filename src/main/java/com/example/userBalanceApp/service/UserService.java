package com.example.userBalanceApp.service;

import com.example.userBalanceApp.dto.UserDto;
import com.example.userBalanceApp.dto.UserUpdateDto;
import com.example.userBalanceApp.filter.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void updateUserData(UserUpdateDto dto);

    Page<UserDto> getUsersByFilter(Pageable pageable, UserFilter filter);

}
