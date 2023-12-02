package me.choizz.websocketmodule.websocket.message;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.chatmessage.ChatMessage;
import me.choizz.chattingmongomodule.chatmessage.ChatMessageService;
import me.choizz.chattingmongomodule.dto.ChatMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations operations;

    @MessageMapping("/chat")
    public void chat(ChatMessageDto chatMessageDto) {
        log.warn("{}", chatMessageDto.toString());
        ChatMessage chatMessage = chatMessageDto.toEntity();
        chatMessageService.saveMassage(chatMessageDto.roomId(), chatMessage);
        operations.convertAndSendToUser(
            String.valueOf(chatMessage.getReceiverId()),
            "/queue/messages",
            chatMessageDto
        );
    }

    @GetMapping("/messages/{selectedRoomId}")
    public ResponseEntity<Optional<List<ChatMessage>>> findChatMessages(
        @PathVariable Long selectedRoomId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(selectedRoomId));
    }
}
