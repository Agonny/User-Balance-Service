package com.example.userBalanceApp.service.impl;

import com.example.userBalanceApp.dao.postgres.*;
import com.example.userBalanceApp.dto.UserDto;
import com.example.userBalanceApp.dto.UserUpdateDto;
import com.example.userBalanceApp.filter.UserFilter;
import com.example.userBalanceApp.mapper.UserMapper;
import com.example.userBalanceApp.model.EmailData;
import com.example.userBalanceApp.model.PhoneData;
import com.example.userBalanceApp.model.UniqueData;
import com.example.userBalanceApp.model.User;
import com.example.userBalanceApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final EmailDataRepository emailDataRepository;

    private final PhoneDataRepository phoneDataRepository;

    private final UserDynamicQuerySelector userSelector;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateUserData(UserUpdateDto dto) {
        Long id = 1L;
        User user = userRepository.findById(id).orElseThrow();

        user.setEmailData(Set.copyOf(mergeData(list -> {
            for(String string : dto.getEmailData()) {
                list.add(new EmailData(null, string, user));
            }
        }, user.getEmailData().stream().toList(), emailDataRepository)));

        user.setPhoneData(Set.copyOf(mergeData(list -> {
            for(String string : dto.getPhoneData()) {
                list.add(new PhoneData(null, string, user));
            }
        }, user.getPhoneData().stream().toList(), phoneDataRepository)));

        userRepository.save(user);
    }

    private <T extends UniqueData, R extends UniqueDataRepository> List<T> mergeData(Consumer<List<T>> operation, List<T> oldData, R repository) {
        List<T> newDataList = new ArrayList<>();
        List<T> deleteData = new ArrayList<>();

        operation.accept(newDataList);

        for(T data : oldData) {
            T newData = findSameData(data.getQualifier(), newDataList);
            if(newData == null) {
                deleteData.add(data);
            } else {
                newDataList.add(newDataList.indexOf(newData), data);
            }
        }

        repository.deleteAll(deleteData);

        return newDataList;
    }

    private <T extends UniqueData> T findSameData(String qualifier, List<T> dataList) {
        for(T data : dataList) if(data.isDataEquals(qualifier)) return data;

        return null;
    }

    public Page<UserDto> getUsersByFilter(Pageable pageable, UserFilter filter) {
        return userMapper.toPageDto(userSelector.getUsersByFilter(filter, pageable));
    }

    public User getCurrentUser() {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        return userRepository.findById(id).orElseThrow();
    }

//    @Override
//    public UserDetailsService userDetailsService() {
//        return this::getUserById();
//    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

}
