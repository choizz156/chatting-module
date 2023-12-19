package me.choizz.apimodule.auth.service;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

@Component
public class LoginUsers {

    private final BoundSetOperations<String, Object> loginUsers;

    public LoginUsers(RedisOperations<String, Object> redisOperations) {
        this.loginUsers = redisOperations.boundSetOps("LoginUsers");
    }

    public void remove(final String email) {
        loginUsers.remove(email);
    }

    public void addLoginUser(final String email) {
        loginUsers.add(email);
    }

    public boolean contains(final String email) {
        if (loginUsers != null) {
            return Boolean.TRUE.equals(loginUsers.isMember(email));
        }
        return false;
    }
}
