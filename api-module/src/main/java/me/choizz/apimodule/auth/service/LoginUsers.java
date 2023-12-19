package me.choizz.apimodule.auth.service;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

@Component
public class LoginUsers {

    private final BoundSetOperations<String, Object> loginUserStore;

    public LoginUsers(RedisOperations<String, Object> redisOperations) {
        this.loginUserStore = redisOperations.boundSetOps("LoginUsers");
    }

    public void removeValue(final String email) {
        loginUserStore.remove(email);
    }

    public void addLoginUser(final String email) {
        loginUserStore.add(email);
    }

    public boolean contains(final String email) {
        if (loginUserStore != null) {
            return Boolean.TRUE.equals(loginUserStore.isMember(email));
        }
        return false;
    }
}
