package me.choizz.chattingredismodule;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import me.choizz.chattingredismodule.config.TestRedisConfig;
import me.choizz.chattingredismodule.dto.LoginUser;
import me.choizz.chattingredismodule.listener.RedisExpirationListener;
import me.choizz.chattingredismodule.session.LoginUsers;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@SpringBootTest(classes = {RedisExpirationListener.class, LoginUsers.class, TestRedisConfig.class})
class RedisExpirationListenerTest {

    @Autowired
    private RedisExpirationListener redisExpirationListener;
    @Autowired
    private LoginUsers loginUsers;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @AfterEach
    void tearDown() {
        redisTemplate.delete("loginUsers");
    }

    @DisplayName("RedisExpirationListener가 동작하면 loginUsers와 sessionKey가 삭제된다.")
    @Test
    void test() throws Exception {
        //given
        String email = "expiredKey";
        String key = "sessionKey";
        loginUsers.addLoginUser(getLoginUser(key, email));

        //when
        Message message = new DefaultMessage("__keyevent@*__:expired".getBytes(), key.getBytes());
        redisExpirationListener.onMessage(message, null);

        //then
        assertThat(loginUsers.get()).isEmpty();
    }


    @DisplayName("redis 세션 기한이 지났을 때, RedisExpirationListener가 작동한다.")
    @Test
    void test2() throws Exception {
        //given
        String email = "email";
        String sessionKey = "sessionKey";
        redisTemplate.opsForValue().set(sessionKey, email);
        loginUsers.addLoginUser(getLoginUser(sessionKey, email));

        //when
        redisTemplate.opsForValue().getAndExpire(sessionKey, Duration.ofSeconds(1));

        //then
        Awaitility.await().atMost(2, TimeUnit.SECONDS)
            .untilAsserted(() -> assertThat(loginUsers.get()).isEmpty());
    }

    private LoginUser getLoginUser(final String key, final String email) {
        return LoginUser.builder()
            .sessionId(key)
            .email(email)
            .roles("USER")
            .nickname("test")
            .userId(1L)
            .build();
    }
}