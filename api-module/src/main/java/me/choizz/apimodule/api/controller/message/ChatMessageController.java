package me.choizz.apimodule.api.controller.message;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.api.controller.dto.ApiResponseDto;
import me.choizz.chattingmongomodule.chatmessage.ChatMessage;
import me.choizz.chattingmongomodule.chatmessage.ChatMessageService;
import me.choizz.chattingmongomodule.dto.ChatMessageDto;
import org.springframework.http.ResponseEntity;
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

    @ResponseStatus(CREATED)
    @PostMapping("/messages")
    public ApiResponseDto<ChatMessageDto> addMessages(@RequestBody ChatMessageDto dto) {
        ChatMessage chatMessage = dto.toEntity();
        chatMessageService.saveMassage(dto.roomId(), chatMessage);
        return new ApiResponseDto<>(dto);
    }
    @GetMapping("/messages/{selectedRoomId}")
    public ResponseEntity<Optional<List<ChatMessage>>> findChatMessages(
        @PathVariable Long selectedRoomId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(selectedRoomId));
    }
}

