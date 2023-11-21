package me.choizz.websocketmodule.websocket.controller;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import me.choizz.chattingmongomodule.chatmessage.ChatMessage;
import me.choizz.chattingmongomodule.chatmessage.ChatService;
import me.choizz.websocketmodule.websocket.controller.dto.ChatInfo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MessagingController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations operations;

    @MessageMapping("/chat/{roomId}")
    public void chat(String roomId, ChatInfo chatInfo) {
        ChatMessage chatMessage = chatInfo.toEntity(LocalDateTime.now());
        chatService.saveMassage(roomId, chatMessage);

        operations.convertAndSend("/sub/" + roomId, chatInfo);
    }
}
