package com.fast.sns.service;

import com.fast.sns.exception.ErrorCode;
import com.fast.sns.exception.SnsApplicationException;
import com.fast.sns.fixture.PostEntityFixture;
import com.fast.sns.fixture.UserEntityFixture;
import com.fast.sns.model.entity.PostEntity;
import com.fast.sns.model.entity.UserEntity;
import com.fast.sns.repository.PostEntityRepository;
import com.fast.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService postService;

    @MockBean
    UserEntityRepository userEntityRepository;

    @MockBean
    PostEntityRepository postEntityRepository;

    @Test
    void 포스트_생성시_정상동작한다() {

        String title = "title";
        String body = "body";
        String username = "username";

        //mocking
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        Assertions.assertDoesNotThrow(() -> postService.create(title, body, username));


    }


    @Test
    void 포스트작성시_요청한유저가_존재하지않는경우() {
        String title = "title";
        String body = "body";
        String username = "username";

        //mocking
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.empty());
        when(userEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.create(title, body, username));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 포스트수정이_성공한경우(){
        String title = "title";
        String body = "body";
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId,1);
        UserEntity userEntity = postEntity.getUser();
        //mocking

        PostEntity mockPostEntity = mock(PostEntity.class);
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        Assertions.assertDoesNotThrow(() -> postService.modify(title, body, username,postId));

    }

    @Test
    void 포스트수정시_포스트가_존재하지않는_경우(){
        String title = "title";
        String body = "body";
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId,1);
        UserEntity userEntity = postEntity.getUser();
        //mocking

        PostEntity mockPostEntity = mock(PostEntity.class);
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(title, body, username, postId));

        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());

    }

    @Test
    void 포스트수정시_권한이_없는_경우(){
        String title = "title";
        String body = "body";
        String username = "username";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(username, postId,1);
        UserEntity userEntity = postEntity.getUser();
        UserEntity writer = UserEntityFixture.get("username1", "password",2);
        //mocking

        PostEntity mockPostEntity = mock(PostEntity.class);
        when(userEntityRepository.findByUserName(username)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(title, body, username, postId));

        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());

    }
}
