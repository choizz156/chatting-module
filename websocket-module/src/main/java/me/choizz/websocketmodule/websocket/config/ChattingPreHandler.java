package me.choizz.websocketmodule.websocket.config;

import static net.logstash.logback.argument.StructuredArguments.kv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.json.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@RequiredArgsConstructor
@Slf4j
public class ChattingPreHandler implements ChannelInterceptor {

    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger("fileLog");

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {

        StompHeaderAccessor headerAccessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        UsernamePasswordAuthenticationToken simpUser =
            (UsernamePasswordAuthenticationToken) headerAccessor.getHeader("simpUser");
        StompCommand command = headerAccessor.getCommand();

        if (
            command.equals(StompCommand.CONNECT) ||
                command.equals(StompCommand.SEND) ||
                command.equals(StompCommand.MESSAGE) ||
                command.equals(StompCommand.DISCONNECT)
        ) {
            if (!simpUser.isAuthenticated()) {
                throw new MessageDeliveryException("인증되지 않은 사용자입니다.");
            }
            String simpSessionId = (String) headerAccessor.getHeader("simpSessionId");
            MDC.put("TRACE_ID", simpSessionId);
            logger.info("stomp 연결, user = {}", simpUser.getPrincipal().toString());
            return message;
        }
        return message;
    }

    @Override
    public void postSend(
        final Message<?> message,
        final MessageChannel channel,
        final boolean sent
    ) {
        StompHeaderAccessor headerAccessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        String info = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);
        StompCommand command = headerAccessor.getCommand();
        if(command.equals(StompCommand.SEND)){
            logging(sent, info);
        }
    }

    private void logging(final boolean sent, final String meessage) {
        try {
            UserInfo userInfo = objectMapper.readValue(meessage, UserInfo.class);
            logger.info("{}, {}, {}, {}",
                kv("is sent", sent),
                kv("senderId", userInfo.senderId),
                kv("receiverId", userInfo.receiverId),
                kv("roomId",userInfo.roomId)
            );
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public void afterSendCompletion(
        final Message<?> message,
        final MessageChannel channel,
        final boolean sent,
        final Exception ex
    ) {
        MDC.clear();
    }

    private record UserInfo(String senderId, String receiverId, Long roomId) {
    }
}
