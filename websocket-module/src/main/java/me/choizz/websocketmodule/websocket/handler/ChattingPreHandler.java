package me.choizz.websocketmodule.websocket.handler;

import static net.logstash.logback.argument.StructuredArguments.kv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@RequiredArgsConstructor
@Slf4j
public class ChattingPreHandler implements ChannelInterceptor {

    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger("fileLog");

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {

        StompHeaderAccessor headerAccessor = getStompHeaderAccessor(
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class)
        );
        StompCommand command = getStompCommand(headerAccessor);
        UsernamePasswordAuthenticationToken simpUser = getUser(headerAccessor);

        if (isNeedAuthenticationCommand(command)) {
            String simpSessionId = (String) headerAccessor.getHeader("simpSessionId");
            MDC.put("TRACE_ID", simpSessionId);
            logger.info("stomp 연결, user = {}", simpUser.getPrincipal());
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
        StompHeaderAccessor headerAccessor = getStompHeaderAccessor(
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class)
        );
        StompCommand command = getStompCommand(headerAccessor);
        if (command.equals(StompCommand.SEND)) {
            logging(sent, new String((byte[]) message.getPayload(), StandardCharsets.UTF_8));
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

    private boolean isNeedAuthenticationCommand(final StompCommand command) {
        return command.equals(StompCommand.CONNECT) ||
            command.equals(StompCommand.SEND) ||
            command.equals(StompCommand.MESSAGE) ||
            command.equals(StompCommand.DISCONNECT);
    }

    private StompHeaderAccessor getStompHeaderAccessor(final StompHeaderAccessor headerAccessor) {
        return Preconditions.checkNotNull(headerAccessor, "stomp 통신 오류 발생");
    }

    private UsernamePasswordAuthenticationToken getUser
        (
            final StompHeaderAccessor headerAccessor
        ) {
        UsernamePasswordAuthenticationToken simpUser = null;
        try {
            simpUser = (UsernamePasswordAuthenticationToken) headerAccessor.getHeader("simpUser");
            Preconditions.checkNotNull(simpUser, "인증 되지 않은 사용자입니다.");
            Preconditions.checkArgument(simpUser.isAuthenticated(), "인증 되지 않은 사용자입니다.");
        } catch (IllegalArgumentException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
        return simpUser;
    }

    private StompCommand getStompCommand(final StompHeaderAccessor headerAccessor) {
        try {
            return Preconditions.checkNotNull(
                headerAccessor.getCommand(),
                "stomp command는 null일 수 없습니다."
            );
        } catch (IllegalArgumentException e) {
            throw new MessageDeliveryException(e.getMessage());
        }
    }

    private void logging(final boolean sent, final String messageInfo) {
        try {
            UserInfo userInfo = objectMapper.readValue(messageInfo, UserInfo.class);
            logger.info("{}, {}, {}, {}",
                kv("is sent", sent),
                kv("senderId", userInfo.senderId),
                kv("receiverId", userInfo.receiverId),
                kv("roomId", userInfo.roomId)
            );
        } catch (JsonProcessingException e) {
            throw new MessageDeliveryException(e.getMessage());
        }
    }

    private record UserInfo(String senderId, String receiverId, Long roomId) {

    }
}
