package me.choizz.chattingredismodule;

import static org.assertj.core.api.Assertions.assertThat;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import me.choizz.chattingredismodule.dto.LoginUser;
import me.choizz.chattingredismodule.session.LoginUsers;
import me.choizz.chattingredismodule.listener.RedisExpirationListener;
import me.choizz.chattingredismodule.session.SessionKeyStore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

@SpringBootTest
class RedisExpirationListenerTest {

    @Autowired
    private RedisExpirationListener redisExpirationListener;
    @Autowired
    private SessionKeyStore sessionKeyStore;
    @Autowired
    private LoginUsers loginUsers;
    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection;
    private static RedisCommands<String, String> syncCommands;

    @BeforeAll
    public static void setup() {
        // Redis 서버 정보 설정
        RedisURI redisUri = RedisURI.Builder.redis("localhost", 6379).build();

        // RedisClient 생성
        redisClient = RedisClient.create(redisUri);

        // Redis 연결 생성
        connection = redisClient.connect();

        // RedisCommands 인스턴스 생성
        syncCommands = connection.sync();
    }

    @AfterAll
    public static void cleanup() {
        // Redis 연결 종료
        connection.close();

        // RedisClient 종료
        redisClient.shutdown();
    }


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
        syncCommands.set(sessionKey, email);

        sessionKeyStore.addValue(email, sessionKey);
        LoginUser loginUser =
            LoginUser.builder()
                .email(email)
                .roles("USER")
                .nickname("test")
                .userId(1L)
                .build();

        loginUsers.addLoginUser(loginUser);

        syncCommands.expire(sessionKey, 2);

        Thread.sleep(3000L);

        assertThat(loginUsers.get()).isEmpty();
        assertThat(sessionKeyStore.isEmpty()).isTrue();
    }

}