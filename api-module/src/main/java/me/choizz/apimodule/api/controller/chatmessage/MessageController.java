package me.choizz.apimodule.api.controller.chatmessage;


import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.chatmessage.ChatMessage;
import me.choizz.chattingmongomodule.chatmessage.ChatMessageService;
import me.choizz.websocketmodule.websocket.exception.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/messages")
@RestController
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger("fileLog");
    private final ChatMessageService chatMessageService;

    @GetMapping("/{selectedRoomId}")
    public ResponseDto<List<ChatMessage>> findChatMessages(
        @PathVariable Long selectedRoomId
    ) {
        return new ResponseDto<>(chatMessageService.findChatMessages(selectedRoomId));
    }
}
