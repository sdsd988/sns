package com.fast.sns.repository;

import com.fast.sns.model.entity.LikeEntity;
import com.fast.sns.model.entity.PostEntity;
import com.fast.sns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {

    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

    //likeEntity에 있는 post를 param post와 비교하여 count를 조회한다.
//    @Query(value = "SELECT COUNT(*) FROM LikeEntity entity WHERE entity.post =:post")
//    Integer countByPost(@Param("post") PostEntity post);

    long countByPost(PostEntity post);
    List<LikeEntity> findAllByPost(PostEntity postEntity);
    @Transactional
    @Query("UPDATE LikeEntity entity SET removed_at = NOW() where entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity postEntity);

}
