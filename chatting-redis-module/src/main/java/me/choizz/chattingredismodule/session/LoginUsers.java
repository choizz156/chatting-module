package me.choizz.chattingredismodule.session;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import me.choizz.chattingredismodule.dto.LoginUser;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

@Component
public class LoginUsers {

    private final BoundSetOperations<String, Object> loginUserStore;

    public LoginUsers(RedisOperations<String, Object> redisOperations) {
        this.loginUserStore = redisOperations.boundSetOps("loginUsers");
    }

    public void addLoginUser(final LoginUser loginUser) {
        loginUserStore.add(loginUser);
    }

    public void removeValue(final LoginUser loginUser) {
        loginUserStore.remove(loginUser);
    }

    public void removeValue(final String key) {
        Set<Object> members = loginUserStore.members();
        if (members != null) {
            members.stream().map(o -> (LoginUser) o)
                .filter(o -> o.sessionId().equals(key))
                .findAny()
                .ifPresent(this::removeValue);
        }
    }

    public boolean contains(final String key) {
        Set<Object> members = loginUserStore.members();
        return
            members != null &&
                members.stream()
                    .map(o -> (LoginUser) o)
                    .anyMatch(o -> o.sessionId().equals(key));
    }

    public Set<LoginUser> get() {
        Set<Object> members = loginUserStore.members();
        if (members != null) {
            return members
                .stream()
                .map(o -> (LoginUser) o)
                .collect(Collectors.toUnmodifiableSet());
        }
        return new HashSet<>();
    }
}
