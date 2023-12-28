package me.choizz.chattingredismodule;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import me.choizz.chattingredismodule.config.TestRedisConfig;
import me.choizz.chattingredismodule.dto.LoginUser;
import me.choizz.chattingredismodule.listener.RedisExpirationListener;
import me.choizz.chattingredismodule.session.LoginUsers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = {RedisExpirationListener.class, LoginUsers.class, TestRedisConfig.class})
@ActiveProfiles("test")
class RedisExpirationListenerTest {

    @Autowired
    private RedisExpirationListener redisExpirationListener;
    @Autowired
    private LoginUsers loginUsers;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @DisplayName("RedisExpirationListener가 동작하면 loginUsers와 sessionKey가 삭제된다.")
    @Test
    void test() throws Exception {
        String email = "expiredKey";
        String key = "sessionKey";

        LoginUser loginUser =
            LoginUser.builder()
                .sessionId(key)
                .email(email)
                .roles("USER")
                .nickname("test")
                .userId(1L)
                .build();

        loginUsers.addLoginUser(loginUser);

        Message message = new DefaultMessage("__keyevent@*__:expired".getBytes(), key.getBytes());
        redisExpirationListener.onMessage(message, null);

        assertThat(loginUsers.get()).isEmpty();

    }


    @DisplayName("redis 세션 기한이 지났을 때, RedisExpirationListener가 작동한다.")
    @Test
    void test2() throws Exception {
        String email = "email";
        String sessionKey = "sessionKey";
        redisTemplate.opsForValue().set(sessionKey, email);

        LoginUser loginUser =
            LoginUser.builder()
                .sessionId(sessionKey)
                .email(email)
                .roles("USER")
                .nickname("test")
                .userId(1L)
                .build();

        loginUsers.addLoginUser(loginUser);

        redisTemplate.opsForValue().getAndExpire(sessionKey, Duration.ofSeconds(2));

        Thread.sleep(3000L);

        assertThat(loginUsers.get()).isEmpty();
    }

}