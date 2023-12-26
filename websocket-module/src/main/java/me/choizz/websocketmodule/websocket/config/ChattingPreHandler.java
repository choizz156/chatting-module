package me.choizz.websocketmodule.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@Slf4j
public class ChattingPreHandler implements ChannelInterceptor {

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
            if(!simpUser.isAuthenticated()){
                throw new MessageDeliveryException("인증되지 않은 사용자입니다.");
            }
            return message;
        }
        return message;
    }
}
