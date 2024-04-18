package com.fast.sns.fixture;

import com.fast.sns.model.entity.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String username, String password,Integer userId) {
        UserEntity result = new UserEntity();
        result.setId(userId);
        result.setUserName(username);
        result.setPassword(password);

        return result;
    }

}
