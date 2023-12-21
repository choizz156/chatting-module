package me.choizz.websocketmodule.websocket.message;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.chatmessage.ChatMessage;
import me.choizz.chattingmongomodule.chatmessage.ChatMessageService;
import me.choizz.chattingmongomodule.dto.ChatMessageDto;
import me.choizz.websocketmodule.websocket.exception.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/messages")
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations operations;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<String> addMessage(@RequestBody ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = chatMessageDto.toEntity();
        chatMessageService.saveMassage(chatMessageDto.roomId(), chatMessage);
        return new ResponseDto<>(chatMessage.getId());
    }

    @MessageMapping("/chat")
    public void chat(ChatMessageDto chatMessageDto) {
        log.error("{}", chatMessageDto.id());
        operations.convertAndSend("/queue/" +
                chatMessageDto.receiverId() +
                "/messages",
            chatMessageDto
        );
    }

    @GetMapping("/{selectedRoomId}")
    public ResponseEntity<Optional<List<ChatMessage>>> findChatMessages(
        @PathVariable Long selectedRoomId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(selectedRoomId));
    }

    @MessageExceptionHandler
    public void handler(Exception e, ChatMessageDto chatMessageDto) {
        chatMessageService.deleteMessage(chatMessageDto.id());
        operations.convertAndSend(
            "/topic/" + chatMessageDto.senderId() + "/error",
            "통신 장애가 발생했습니다."
        );
    }
}
