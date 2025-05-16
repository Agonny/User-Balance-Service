package com.example.userBalanceApp.service;

import com.example.userBalanceApp.dto.UserDto;
import com.example.userBalanceApp.dto.UserUpdateDto;
import com.example.userBalanceApp.filter.UserFilter;
import com.example.userBalanceApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void updateUserData(UserUpdateDto dto);

    Page<UserDto> getUsersByFilter(Pageable pageable, UserFilter filter);

    User getUserById(Long id);

    User getCurrentUser();

}
