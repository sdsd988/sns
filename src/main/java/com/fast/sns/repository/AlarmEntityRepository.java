package com.fast.sns.repository;

import com.fast.sns.model.entity.AlarmEntity;
import com.fast.sns.model.entity.LikeEntity;
import com.fast.sns.model.entity.PostEntity;
import com.fast.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Integer> {
    /**
     *  굳이 UserEntity 받을 필요가 있나?
     */
//    Page<AlarmEntity> findAllByUser(UserEntity user, Pageable pageable);
    Page<AlarmEntity> findAllByUserId(Integer userId, Pageable pageable);

}
