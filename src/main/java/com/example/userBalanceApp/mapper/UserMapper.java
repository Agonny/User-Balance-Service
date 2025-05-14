package com.example.userBalanceApp.mapper;

import com.example.userBalanceApp.dto.UserDto;
import com.example.userBalanceApp.model.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(builder = @Builder(disableBuilder = true))
public interface UserMapper {

    UserDto toDto(User entity);

    Page<UserDto> toPageDto(Page<User> userList);

}
