package me.choizz.websocketmodule.websocket.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingmongomodule.chatmessage.ChatMessage;
import me.choizz.chattingmongomodule.chatmessage.ChatMessageService;
import me.choizz.websocketmodule.websocket.dto.ChatRequestMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/messages")
@RestController
public class ChatMessageController {

    private static final Logger logger = LoggerFactory.getLogger("fileLog");
    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations operations;

    @MessageMapping("/chat")
    public void sendChat(
        @Header(value = "simpSessionId") String sessionId,
        ChatRequestMessageDto messageDto
    ) {
        chatMessageService.checkExistChatRoom(messageDto.roomId());
        ChatMessage messageEntity = chatMessageService.saveMassage(messageDto.roomId(),
            messageDto.toEntity());

        sendMessage(messageEntity);
        logger.info(
            "send message complete :: roomId => {}, sessionId = {}",
            messageDto.roomId(), sessionId
        );
    }

    @MessageExceptionHandler
    public void exceptionHandler(Exception e, ChatRequestMessageDto chatRequestMessageDto) {
        logger.warn("error => {}", e.getMessage());
        operations.convertAndSend(
            "/topic/" + chatRequestMessageDto.senderId() + "/error",
            "통신 장애가 발생했습니다."
        );
    }

    private void sendMessage(final ChatMessage message) {
        try {
            operations.convertAndSend("/queue/" + message.getReceiverId() + "/messages", message);
        } catch (MessagingException e) {
            chatMessageService.deleteMessage(message.getId());
            throw new MessageDeliveryException(e.getMessage());
        }
    }
}
