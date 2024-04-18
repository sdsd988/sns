package com.fast.sns.fixture;

import com.fast.sns.model.entity.PostEntity;
import com.fast.sns.model.entity.UserEntity;

public class PostEntityFixture {

    public static PostEntity get(String username,Integer postId) {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUserName(username);

        PostEntity result = new PostEntity();
        result.setUser(user);
        result.setId(postId);

        return result;
    }

}
