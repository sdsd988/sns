package com.fast.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "user name is duplicated"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"Password is invalid"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "post not found"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED,"Permission is invalid"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error"),
    ALREADY_LIKED(HttpStatus.CONFLICT, "user already liked the post"),
    ALARM_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Connecting alarm occurs error"),
    ;

    private HttpStatus status;
    private String message;
}
