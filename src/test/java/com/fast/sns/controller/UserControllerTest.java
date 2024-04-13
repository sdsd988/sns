package com.fast.sns.controller;

import com.fast.sns.controller.request.UserJoinRequest;
import com.fast.sns.controller.request.UserLoginRequest;
import com.fast.sns.exception.SnsApplicationException;
import com.fast.sns.model.User;
import com.fast.sns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void signup_success() throws Exception {
        String username = "username";
        String password = "password";

        //TODO : mocking
        when(userService.join(username,password)).thenReturn(mock(User.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        //TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username, password)))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void signup_fail_duplicated_username() throws Exception {

        String username = "username";
        String password = "password";

        //TODO: mocking
        when(userService.join(username,password)).thenThrow(new SnsApplicationException());


        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        //TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void login() throws Exception {

        String username = "username";
        String password = "password";

        //TODO: mocking
        when(userService.login(username,password)).thenReturn("test_token");


        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        //TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    void login_fail_notFound_username() throws Exception {

        String username = "username";
        String password = "password";

        //TODO: mocking

        when(userService.login(username,password)).thenThrow(new SnsApplicationException());

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        //TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    void login_fail_notFound_password() throws Exception {

        String username = "username";
        String password = "password";

        //TODO: mocking
        when(userService.login(username,password)).thenThrow(new SnsApplicationException());


        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        //TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
