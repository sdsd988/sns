package com.fast.sns.fixture;

import com.fast.sns.model.entity.PostEntity;
import com.fast.sns.model.entity.UserEntity;

public class PostEntityFixture {

    public static PostEntity get(String username,Integer postId,Integer userId) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUserName(username);

        PostEntity result = new PostEntity();
        result.setUser(user);
        result.setId(postId);

        return result;
    }

}
