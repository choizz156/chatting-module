package me.choizz.websocketmodule.websocket.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.dto.ChatMessageDto;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final SimpMessageSendingOperations operations;
    @MessageMapping("/chat")
    public void chat(ChatMessageDto chatMessageDto) {
        operations.convertAndSendToUser(
            String.valueOf(chatMessageDto.receiverId()),
            "/queue/messages",
            chatMessageDto
        );
    }

    @MessageExceptionHandler
    @SendTo("/topic/error")
    public String handler(Exception e) {
        return e.getMessage();
    }
}
