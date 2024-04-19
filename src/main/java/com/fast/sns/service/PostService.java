package com.fast.sns.service;

import com.fast.sns.exception.ErrorCode;
import com.fast.sns.exception.SnsApplicationException;
import com.fast.sns.model.AlarmArgs;
import com.fast.sns.model.AlarmType;
import com.fast.sns.model.Comment;
import com.fast.sns.model.Post;
import com.fast.sns.model.entity.*;
import com.fast.sns.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;

    @Transactional
    public void create(String title, String body, String username) {
        UserEntity userEntity = getUserEntityOrException(username);

        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        postEntityRepository.save(postEntity);
    }

    @Transactional
    public Post modify(String title, String body, String username, Integer postId) {

        UserEntity userEntity = getUserEntityOrException(username);

        // post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        // post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %s", username, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String username, Integer postId) {

        UserEntity userEntity = getUserEntityOrException(username);

        PostEntity postEntity = getPostEntityOrException(postId);

        // post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %s", username, postId));
        }

        likeEntityRepository.deleteAllByPost(postEntity);
        commentEntityRepository.deleteAllByPost(postEntity);
        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String username, Pageable pageable) {

        UserEntity userEntity = getUserEntityOrException(username);


        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String username) {


        PostEntity postEntity = getPostEntityOrException(postId);

        UserEntity userEntity = getUserEntityOrException(username);

        likeEntityRepository.findByUserAndPost(userEntity,postEntity).ifPresent(it->{
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED,
                    String.format("userName %s already like post %d", username, postId));
        });

        //like.save
        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));

        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(),
                AlarmType.NEW_LIKE_ON_POST,
                new AlarmArgs(userEntity.getId(),
                        postEntity.getId())));
    }

    public Long likeCount(Integer postId) {
        PostEntity postEntity = getPostEntityOrException(postId);


        //count like method1
//        List<LikeEntity> likeEntities = likeEntityRepository.findAllByPost(postEntity);
//        return likeEntities.size();

        //count like method2
        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Integer postId, String username, String comment) {

        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserEntityOrException(username);

        //comment.save
        commentEntityRepository.save(CommentEntity.of(userEntity, postEntity, comment));

        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(),
                AlarmType.NEW_COMMENT_ON_POST,
                new AlarmArgs(userEntity.getId(),
                postEntity.getId())));
    }

    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        PostEntity postEntity = getPostEntityOrException(postId);
        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }

    // post exist
    private PostEntity getPostEntityOrException(Integer postId) {
        return postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not Founded", postId)));
    }

    //user exist
    private UserEntity getUserEntityOrException(String username) {
        return userEntityRepository.findByUserName(username)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                        String.format("userName is %s", username)
                ));

    }


}
