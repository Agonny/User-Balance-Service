package com.example.userBalanceApp.mapper;

import com.example.userBalanceApp.dto.UserDto;
import com.example.userBalanceApp.model.User;
import com.example.userBalanceApp.security.AuthUser;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "emailData", expression = "java(entity.emailDataList())")
    @Mapping(target = "phoneData", expression = "java(entity.phoneDataList())")
    UserDto toDto(User entity);

    AuthUser toAuthUser(User entity);

    List<UserDto> toListDto(List<User> userList);

}
