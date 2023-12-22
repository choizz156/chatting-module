package me.choizz.chattingredismodule;

import static org.assertj.core.api.Assertions.assertThat;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.time.Duration;
import me.choizz.chattingredismodule.dto.LoginUser;
import me.choizz.chattingredismodule.listener.RedisExpirationListener;
import me.choizz.chattingredismodule.session.LoginUsers;
import me.choizz.chattingredismodule.session.SessionKeyStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
class RedisExpirationListenerTest {

    @Autowired
    private RedisExpirationListener redisExpirationListener;
    @Autowired
    private SessionKeyStore sessionKeyStore;
    @Autowired
    private LoginUsers loginUsers;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection;
    private static RedisCommands<String, String> syncCommands;


    @Test
    void test() throws Exception {
        String email = "expiredKey";
        String key = "sessionKey";

        sessionKeyStore.addValue(email, key);
        LoginUser loginUser =
            LoginUser.builder()
                .email(email)
                .roles("USER")
                .nickname("test")
                .userId(1L)
                .build();
        loginUsers.addLoginUser(loginUser);

        Message message = new DefaultMessage("__keyevent@*__:expired".getBytes(), key.getBytes());
        redisExpirationListener.onMessage(message, null);

        assertThat(loginUsers.get()).isEmpty();
        assertThat(sessionKeyStore.isEmpty()).isTrue();
    }


    @Test
    void test2() throws Exception {
        String email = "email";
        String sessionKey = "sessionKey";
        redisTemplate.opsForValue().set(sessionKey,email);

        sessionKeyStore.addValue(email, sessionKey);
        LoginUser loginUser =
            LoginUser.builder()
                .email(email)
                .roles("USER")
                .nickname("test")
                .userId(1L)
                .build();

        loginUsers.addLoginUser(loginUser);

        redisTemplate.opsForValue().getAndExpire(sessionKey, Duration.ofSeconds(2));

        Thread.sleep(3000L);

        assertThat(loginUsers.get()).isEmpty();
        assertThat(sessionKeyStore.isEmpty()).isTrue();
    }

}