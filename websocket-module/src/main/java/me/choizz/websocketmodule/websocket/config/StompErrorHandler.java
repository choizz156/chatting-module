package me.choizz.websocketmodule.websocket.config;

import java.nio.charset.StandardCharsets;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;


public class StompErrorHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError(
        final Message<byte[]> clientMessage,
        final Throwable ex
    ) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setLeaveMutable(true);

       return MessageBuilder.createMessage(
            ex.getMessage().getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
