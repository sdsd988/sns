package com.fast.sns.repository;

import com.fast.sns.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, User> userRedisTemplate;
    private final static Duration USER_CACHE_TIL = Duration.ofDays(3);

    public void setUser(User user) {
        String key = getKey(user.getUsername());
        log.info("Set User to Redis {}:{}",key,user);
        userRedisTemplate.opsForValue().set(key, user);
    }

    public Optional<User> getUser(String username) {
        String key = getKey(username);
        User user = userRedisTemplate.opsForValue().get(key);
        log.info("Get data from Redis {} , {}", key, user);
        return Optional.ofNullable(user);
    }

    //User DB I/O가 가장 많이 발생하는 곳은 ? - Filter
    //redis key 값은 프리픽스를 사용하는 것이 일반적
    private String getKey(String username) {
        return "USER:"+username;
    }


}
