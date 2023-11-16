package me.choizz.chattingserver.websocket.controller;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import me.choizz.chattingserver.websocket.dto.ChatInfo;
import me.choizz.chattingserver.websocket.service.ChatService;
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
        var chatMessage = chatInfo.toEntity(LocalDateTime.now());
        chatService.saveMassage(roomId, chatMessage);

        operations.convertAndSend("/sub/" + roomId, chatInfo);
    }
}
