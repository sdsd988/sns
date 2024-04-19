package com.fast.sns.controller;

import com.fast.sns.controller.request.UserJoinRequest;
import com.fast.sns.controller.request.UserLoginRequest;
import com.fast.sns.controller.response.AlarmResponse;
import com.fast.sns.controller.response.Response;
import com.fast.sns.controller.response.UserJoinResponse;
import com.fast.sns.controller.response.UserLoginResponse;
import com.fast.sns.exception.ErrorCode;
import com.fast.sns.exception.SnsApplicationException;
import com.fast.sns.model.User;
import com.fast.sns.service.UserService;
import com.fast.sns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {

        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR,"Casting to User class failed"));
//        User user = (User) authentication.getPrincipal();
        return Response.success(userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm));


    }
}
