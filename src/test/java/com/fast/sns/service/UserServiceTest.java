package com.fast.sns.service;



import com.fast.sns.exception.SnsApplicationException;
import com.fast.sns.fixture.UserEntityFixture;
import com.fast.sns.model.entity.UserEntity;
import com.fast.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    void signup_success() {
        String username = "username";
        String password = "password";

        //mocking
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.empty());
        when(userEntityRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(username, password)));

        assertDoesNotThrow(() -> userService.join(username,password));

    }

    @Test
    void signup_fail_duplicated_username() {
        String username = "username";
        String password = "password";
        UserEntity fixture = UserEntityFixture.get(username, password);


        //mocking
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(fixture));
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        assertThrows(SnsApplicationException.class,() -> userService.join(username,password));

    }
    @Test
    void login_success() {
        String username = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(username, password);

        //mocking
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(fixture));
        when(userEntityRepository.save(any())).thenReturn(Optional.of(mock(UserEntity.class)));

        assertDoesNotThrow(() -> userService.join(username,password));

    }

    @Test
    void login_fail_notFound_user() {
        String username = "username";
        String password = "password";

        //mocking
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.empty());

        assertThrows(SnsApplicationException.class,() -> userService.login(username,password));

    }

    @Test
    void login_fail_password_not_correspond() {
        String username = "username";
        String password = "password";
        String wrongPassword = "wrong_password";
        UserEntity fixture = UserEntityFixture.get(username, password);

        //mocking
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(fixture));

        assertThrows(SnsApplicationException.class,() -> userService.login(username,wrongPassword));

    }
}
