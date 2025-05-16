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
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "userCache")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final EmailDataRepository emailDataRepository;

    private final PhoneDataRepository phoneDataRepository;

    private final UserDynamicQuerySelector userSelector;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @CacheEvict(cacheNames = "userDtos", allEntries = true)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateUserData(UserUpdateDto dto) {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(id).orElseThrow();
        List<EmailData> oldEmailData = user.getEmailData().stream().toList();
        List<PhoneData> oldPhoneData = user.getPhoneData().stream().toList();

        user.setEmailData(new LinkedHashSet<>(mergeData(list -> {
            for(String string : dto.getEmailData()) {
                EmailData oldData = findSameData(string, oldEmailData);

                list.add(Objects.requireNonNullElseGet(oldData, () -> new EmailData(null, string, user)));
            }
        }, oldEmailData, emailDataRepository)));

        user.setPhoneData(new LinkedHashSet<>(mergeData(list -> {
            for(String string : dto.getPhoneData()) {
                PhoneData oldData = findSameData(string, oldPhoneData);

                list.add(Objects.requireNonNullElseGet(oldData, () -> new PhoneData(null, string, user)));
            }
        }, oldPhoneData, phoneDataRepository)));

        userRepository.save(user);
        log.info("User with ID [{}] successfully updated", id);
    }

    private <T extends UniqueData, R extends UniqueDataRepository> List<T> mergeData(Consumer<List<T>> operation, List<T> oldData, R repository) {
        List<T> newDataList = new ArrayList<>();
        List<T> deleteData = new ArrayList<>();

        operation.accept(newDataList);

        for(T data : oldData) {
            T newData = findSameData(data.getQualifier(), newDataList);
            if(newData == null) {
                data.dropUser();
                deleteData.add(data);
            }
        }

        repository.deleteAll(deleteData);

        return newDataList;
    }

    private <T extends UniqueData> T findSameData(String qualifier, List<T> dataList) {
        for(T data : dataList) if(data.isDataEquals(qualifier)) return data;

        return null;
    }

    @CacheEvict(cacheNames = "userDtos", allEntries = true)
    public Page<UserDto> getUsersByFilter(Pageable pageable, UserFilter filter) {
        Page<User> users = userSelector.getUsersByFilter(filter, pageable);

        return new PageImpl<>(userMapper.toListDto(users.toList()), pageable, users.getTotalElements());
    }

    public User getCurrentUser() {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> optional = userRepository.findById(Long.parseLong(username));
        if (optional.isEmpty()) throw new UsernameNotFoundException(username);

        return userMapper.toAuthUser(optional.get());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

}
