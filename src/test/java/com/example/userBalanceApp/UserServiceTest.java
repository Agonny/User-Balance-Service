package com.example.userBalanceApp;

import com.example.userBalanceApp.dao.postgres.EmailDataRepository;
import com.example.userBalanceApp.dao.postgres.PhoneDataRepository;
import com.example.userBalanceApp.dao.postgres.UserRepository;
import com.example.userBalanceApp.dto.UserUpdateDto;
import com.example.userBalanceApp.mapper.UserMapper;
import com.example.userBalanceApp.model.EmailData;
import com.example.userBalanceApp.model.PhoneData;
import com.example.userBalanceApp.model.User;
import com.example.userBalanceApp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneDataRepository phoneDataRepository;

    @Mock
    private EmailDataRepository emailDataRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Test
    public void checkUserUpdate() {
        User user1 = User.builder()
                .id(1L)
                .name("user1")
                .dateOfBirth(LocalDate.now())
                .password("some_password").build();

        EmailData emailData1 = EmailData.builder().id(1L).email("some_email@g.com").user(user1).build();
        EmailData emailData2 = EmailData.builder().id(2L).email("some_new_email@g.com").user(user1).build();

        PhoneData phoneData1 = PhoneData.builder().id(1L).phone("499494949").user(user1).build();

        user1.setEmailData(new LinkedHashSet<>(List.of(emailData1, emailData2)));
        user1.setPhoneData(new LinkedHashSet<>(List.of(phoneData1)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);
        UserUpdateDto dto = new UserUpdateDto(List.of("some_email@g.com", "some_new_email@g.com", "another_email@g.com"),
                List.of("9494949494"));

        UserDetails userDetails = userMapper.toAuthUser(user1);
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        userService.updateUserData(dto);

        Assertions.assertEquals(user1.getEmailData().size(), 3);
        Assertions.assertEquals(user1.getPhoneData().size(), 1);
        Assertions.assertFalse(user1.getPhoneData().contains(phoneData1));

        Assertions.assertTrue(user1.getEmailData().contains(emailData2));
        Assertions.assertTrue(user1.emailDataList().contains(dto.getEmailData().get(2)));
    }

}
