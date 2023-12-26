package me.choizz.websocketmodule.websocket.message;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.chatmessage.ChatMessage;
import me.choizz.chattingmongomodule.chatmessage.ChatMessageService;
import me.choizz.chattingmongomodule.dto.ChatMessageDto;
import me.choizz.websocketmodule.websocket.exception.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/messages")
@RestController
public class ChatMessageController {

    private final Logger logger = LoggerFactory.getLogger("fileLog");
    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations operations;

    @GetMapping("/{selectedRoomId}")
    public ResponseDto<List<ChatMessage>> findChatMessages(
        @PathVariable Long selectedRoomId
    ) {
        return new ResponseDto<>(chatMessageService.findChatMessages(selectedRoomId));
    }

    @MessageMapping("/chat")
    public void sendChat(ChatMessageDto chatMessageDto) {
        logger.info("addMessage in chatRoom -> id = {}", chatMessageDto.roomId());
        logger.info("{}, {}",
            kv("senderID", chatMessageDto.senderId()),
            kv("receiverID", chatMessageDto.receiverId())
        );

        ChatMessage messageEntity =
            chatMessageService.saveMassage(chatMessageDto.roomId(), chatMessageDto.toEntity());

        sendMessage(messageEntity);

        logger.info("send message complete :: roomId => {}", chatMessageDto.roomId());
    }

    @MessageExceptionHandler
    public void exceptionHandler(Exception e, ChatMessageDto chatMessageDto) {
        logger.error("messaging error => {}", e.getMessage());
        operations.convertAndSend(
            "/topic/" + chatMessageDto.senderId() + "/error",
            "통신 장애가 발생했습니다."
        );
    }

    private void sendMessage(final ChatMessage message) {
        try {
            operations.convertAndSend("/queue/" + message.getReceiverId() + "/messages", message);
        } catch (Exception e) {
            String id = message.getId();
            chatMessageService.deleteMessage(id);
            logger.error("sent message delete => messageID : {}", id);
            logger.error("stomp error => {}", e.getMessage());
            throw e;
        }
    }
}
