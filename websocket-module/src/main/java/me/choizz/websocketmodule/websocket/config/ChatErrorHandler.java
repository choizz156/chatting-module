package me.choizz.websocketmodule.websocket.config;

import java.nio.charset.StandardCharsets;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;


public class ChatErrorHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError(
        final Message<byte[]> clientMessage,
        final Throwable ex
    ) {

        if (ex.getMessage().equals("인증되지 않은 사용자입니다.")) {
            return errorMessage(ex.getMessage());
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }


    private Message<byte[]> errorMessage(String errorMessage) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8),
            accessor.getMessageHeaders());
    }
}
