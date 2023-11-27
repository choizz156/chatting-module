package me.choizz.websocketmodule.websocket.controller;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.chatmessage.ChatService;
import me.choizz.chattingmongomodule.dto.ChatMessageDto;
import me.choizz.websocketmodule.websocket.controller.dto.ChatInfo;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MessagingController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations operations;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/{roomId}")
    public void chat(@DestinationVariable Long roomId, ChatInfo chatInfo) {
        log.info("진입");

        ChatMessageDto chatMessageDto = chatInfo.toEntity(LocalDateTime.now());
        chatService.saveMassage(roomId, chatMessageDto);

        operations.convertAndSend(chatInfo);
    }
}
