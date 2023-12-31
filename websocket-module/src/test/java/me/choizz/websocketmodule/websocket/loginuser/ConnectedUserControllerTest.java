package me.choizz.websocketmodule.websocket.loginuser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.chatRoom.ChatRoomRepository;
import me.choizz.chattingredismodule.config.TestRedisConfig;
import me.choizz.chattingredismodule.dto.LoginUser;
import me.choizz.chattingredismodule.session.LoginUsers;
import me.choizz.websocketmodule.websocket.config.WebSocketTestConfig;
import me.choizz.websocketmodule.websocket.connecteduser.ConnectedUserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@Slf4j
@DisplayName("connected user 테스트")
@ActiveProfiles("test")
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    WebSocketTestConfig.class,
    ChatRoomRepository.class,
    ConnectedUserController.class,
    TestRedisConfig.class,
    LoginUsers.class
})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
})
class ConnectedUserControllerTest {

    @LocalServerPort
    private int port;

    @SpyBean
    private ConnectedUserController connectedUserController;

    @Autowired
    private LoginUsers loginUsers;

    //loginusers에 로그인 유저를 넣는다. -> 스케쥴링을 테스트한다.
    //세션이 없어질경우 테스트
    private WebSocketStompClient webSocketStompClient;

    @BeforeEach
    void setUpEach() throws Exception {
        getLoginUser();

        this.webSocketStompClient = new WebSocketStompClient(
            new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient())))
        );

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    private void getLoginUser() {
        LoginUser loginUser1 = LoginUser.builder()
            .sessionId("sessionId1")
            .email("test1@t.com")
            .roles("USER")
            .nickname("test")
            .userId(1L)
            .build();

        LoginUser loginUser2 = LoginUser.builder()
            .sessionId("sessionId2")
            .email("test2@t.com")
            .roles("USER")
            .nickname("test1")
            .userId(2L)
            .build();

        loginUsers.addLoginUser(loginUser1);
        loginUsers.addLoginUser(loginUser2);
    }

    @DisplayName("로그인 유저들의 데이터를 일정 간격으로 전송할 수 있다.")
    @Test
    void test1() throws Exception {

        BlockingQueue<Set<LoginUser>> q = connectStomp();
        connectedUserController.getConnectedUsers();

        await()
            .atMost(10, TimeUnit.SECONDS)
            .untilAsserted(() ->
                verify(connectedUserController, times(2)).getConnectedUsers());

        assertThat(q.poll()).hasSize(2);
    }

    @DisplayName("로그인 유저가 로그아웃 시 전송 데이터가 변한다.")
    @Test
    void test2() throws Exception {
        BlockingQueue<Set<LoginUser>> q = connectStomp();
        connectedUserController.getConnectedUsers();

        await()
            .atMost(10, TimeUnit.SECONDS)
            .untilAsserted(() ->
                verify(connectedUserController, times(2)).getConnectedUsers());

        loginUsers.removeValue("sessionId1");

        await().
            atMost(3, TimeUnit.SECONDS)
            .untilAsserted(() -> assertThat(q.poll()).hasSize(1));

    }

    private BlockingQueue<Set<LoginUser>> connectStomp()
        throws InterruptedException, ExecutionException, TimeoutException {
        StompSession stompSession = webSocketStompClient.connectAsync(
                String.format("ws://localhost:%d/chat", port),
                new MyStompSessionHandlerAdapter())
            .get(10, TimeUnit.SECONDS);

        BlockingQueue<Set<LoginUser>> q = new ArrayBlockingQueue<>(2);

        stompSession.subscribe("/topic/public", new StompFrameHandler() {

            @Override
            public Type getPayloadType(final StompHeaders headers) {
                return Set.class;
            }

            @Override
            public void handleFrame(final StompHeaders headers, final Object payload) {
                q.add((Set<LoginUser>) payload);
            }
        });

        return q;
    }

    private static class MyStompSessionHandlerAdapter extends StompSessionHandlerAdapter {

    }

}