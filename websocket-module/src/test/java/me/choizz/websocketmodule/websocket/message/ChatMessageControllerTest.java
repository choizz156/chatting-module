package me.choizz.websocketmodule.websocket.message;


import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.chatRoom.ChatRoomRepository;
import me.choizz.chattingmongomodule.chatmessage.ChatMessageService;
import me.choizz.chattingredismodule.config.TestRedisConfig;
import me.choizz.websocketmodule.websocket.config.WebSocketTestConfig;
import me.choizz.websocketmodule.websocket.dto.ChatRequestMessageDto;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
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


@DisplayName("메시지 전송 테스트")
@Slf4j
@ActiveProfiles("test")
@EnableAutoConfiguration
@ContextConfiguration(classes = {
    ChatMessageService.class,
    ChatMessageController.class,
    WebSocketTestConfig.class,
    ChatRoomRepository.class,
    TestRedisConfig.class
})
@EnableMongoRepositories(basePackages = "me.choizz.chattingmongomodule")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
})
class ChatMessageControllerTest {

    @LocalServerPort
    private int port;

    private WebSocketStompClient webSocketStompClient;

    @BeforeEach
    void setUpEach() throws Exception {
        this.webSocketStompClient = new WebSocketStompClient(
            new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient())))
        );

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @DisplayName("메시지를 전송할 수 있다.")
    @Test
    void test1() throws Exception {

        Result result = getConnected();

        result.stompSession().send("/app/chat", result.messageDto());

        Awaitility.await().atMost(1, TimeUnit.SECONDS)
            .untilAsserted(() -> assertThat(result.q().poll()).isEqualTo(result.messageDto()));
    }

    private Result getConnected() throws Exception {
        StompSession stompSession = webSocketStompClient.connectAsync(
                String.format("ws://localhost:%d/chat", port),
                new MyStompSessionHandlerAdapter())
            .get(1, TimeUnit.SECONDS);

        BlockingQueue<ChatRequestMessageDto> q = new ArrayBlockingQueue<>(1);

        ChatRequestMessageDto messageDto = ChatRequestMessageDto.builder()
            .senderId(1L).
            senderNickname("test")
            .receiverNickname("test1")
            .receiverId(2L)
            .content("tesstest")
            .roomId(1L)
            .build();

        stompSession.subscribe("/queue/2/messages", new StompFrameHandler() {

            @Override
            public Type getPayloadType(final StompHeaders headers) {
                return ChatRequestMessageDto.class;
            }

            @Override
            public void handleFrame(final StompHeaders headers, final Object payload) {
                q.add((ChatRequestMessageDto) payload);
            }
        });
        Result result = new Result(stompSession, q, messageDto);
        return result;
    }

    private record Result(StompSession stompSession, BlockingQueue<ChatRequestMessageDto> q,
                          ChatRequestMessageDto messageDto) {

    }

    private static class MyStompSessionHandlerAdapter extends StompSessionHandlerAdapter {

    }
}