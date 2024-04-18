package com.fast.sns.service;

import com.fast.sns.exception.ErrorCode;
import com.fast.sns.exception.SnsApplicationException;
import com.fast.sns.model.entity.PostEntity;
import com.fast.sns.model.entity.UserEntity;
import com.fast.sns.repository.PostEntityRepository;
import com.fast.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    @Transactional
    public void create(String title, String body, String username) {
        UserEntity userEntity = userEntityRepository.findByUserName(username)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", username)));
        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        postEntityRepository.save(postEntity);
    }

    public void modify(String title, String body, String username,Integer postId) {
        UserEntity userEntity = userEntityRepository.findByUserName(username)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", username)));

        // post exist



        // post permission
    }
}