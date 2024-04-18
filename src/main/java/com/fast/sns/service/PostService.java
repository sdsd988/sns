package com.fast.sns.service;

import com.fast.sns.exception.ErrorCode;
import com.fast.sns.exception.SnsApplicationException;
import com.fast.sns.model.Post;
import com.fast.sns.model.entity.LikeEntity;
import com.fast.sns.model.entity.PostEntity;
import com.fast.sns.model.entity.UserEntity;
import com.fast.sns.repository.LikeEntityRepository;
import com.fast.sns.repository.PostEntityRepository;
import com.fast.sns.repository.UserEntityRepository;
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
    @Transactional
    public void create(String title, String body, String username) {
        UserEntity userEntity = userEntityRepository.findByUserName(username)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", username)));
        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        postEntityRepository.save(postEntity);
    }

    @Transactional
    public Post modify(String title, String body, String username, Integer postId) {
        UserEntity userEntity = userEntityRepository.findByUserName(username)
                .orElseThrow(()
                        -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", username)
                ));

        // post exist
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not Founded", postId)));

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

        UserEntity userEntity = userEntityRepository.findByUserName(username)
                .orElseThrow(()
                        -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", username)
                ));

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not Founded", postId)));

        // post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %s", username, postId));
        }

        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String username, Pageable pageable) {

        UserEntity userEntity = userEntityRepository.findByUserName(username)
                .orElseThrow(()
                        -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", username)
                ));


        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String username) {

        // post exist
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not Founded", postId)));

        UserEntity userEntity = userEntityRepository.findByUserName(username)
                .orElseThrow(()
                        -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", username)
                ));

        // check liked -> throw
        likeEntityRepository.findByUserAndPost(userEntity,postEntity).ifPresent(it->{
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED,
                    String.format("userName %s already like post %d", username, postId));
        });

        //like.save
        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
    }

    public Integer likeCount(Integer postId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not Founded", postId)));

        //count like method1
//        List<LikeEntity> likeEntities = likeEntityRepository.findAllByPost(postEntity);
//        return likeEntities.size();

        //count like method2
        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Integer postId, String username) {

    }
}
