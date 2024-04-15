package com.fast.sns.service;

import com.fast.sns.exception.ErrorCode;
import com.fast.sns.exception.SnsApplicationException;
import com.fast.sns.model.User;
import com.fast.sns.model.entity.UserEntity;
import com.fast.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    //TODO: implement
    public User join(String username, String password){

        //회원가입하려는 username으로 회원가입된 user가 있는지
       userEntityRepository.findByUserName(username).ifPresent(it->{
           throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", username));
       });


        //회원가입 진행 = user 등록
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(username, password));

        return User.fromEntity(userEntity);
    }


    public String login(String username, String password) {
        //회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUserName(username).orElseThrow(() -> new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        //비밀번호 체크
        if (!userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, "");
        }

        //토큰 생성
        return "";
    }
}
