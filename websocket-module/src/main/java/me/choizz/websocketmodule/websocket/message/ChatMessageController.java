package me.choizz.websocketmodule.websocket.message;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.chatmessage.ChatMessage;
import me.choizz.chattingmongomodule.chatmessage.ChatMessageService;
import me.choizz.chattingmongomodule.dto.ChatMessageDto;
import me.choizz.websocketmodule.websocket.exception.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations operations;

    @ResponseStatus(CREATED)
    @PostMapping("/messages")
    public ResponseDto<ChatMessageDto> addMessages(@RequestBody ChatMessageDto dto){
        ChatMessage chatMessage = dto.toEntity();
        chatMessageService.saveMassage(dto.roomId(), chatMessage);
        return new ResponseDto<>(dto);
    }

    @MessageMapping("/chat")
    public void chat(ChatMessageDto chatMessageDto) {
        log.warn("{}",chatMessageDto);
        operations.convertAndSend("/queue/"+
            String.valueOf(chatMessageDto.receiverId())+
            "/messages",
            chatMessageDto
        );
    }

    @GetMapping("/messages/{selectedRoomId}")
    public ResponseEntity<Optional<List<ChatMessage>>> findChatMessages(
        @PathVariable Long selectedRoomId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(selectedRoomId));
    }

    @MessageExceptionHandler
    @SendTo("/topic/error")
    public String handler(Exception e) {
        return e.getMessage();
    }
}
