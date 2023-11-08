package me.choizz.chattingserver.websocket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingserver.api.ApiResponseDto;
import me.choizz.chattingserver.websocket.ChatMessage;
import me.choizz.chattingserver.websocket.ChatRoom;
import me.choizz.chattingserver.websocket.dto.ChatInfo;
import me.choizz.chattingserver.websocket.dto.ChatRoomRequest;
import me.choizz.chattingserver.websocket.dto.ChatRoomResponse;
import me.choizz.chattingserver.websocket.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations operations;

    @PostMapping("/chatting-room")
    public ApiResponseDto<ChatRoomResponse> chattingRoom(ChatRoomRequest chatRoomRequest) {

        if(chatRoomRequest.getRoomName().isEmpty()){
            chatRoomRequest.toDefaultName(chatRoomRequest.getNickname());
        }

        ChatRoom chatRoom = chatService.createChatRoom(chatRoomRequest.getRoomName());
        return new ApiResponseDto<>(new ChatRoomResponse(chatRoom.getRoomId(), chatRoom.getName()));
    }

    @MessageMapping("/chat")
    public void chat(ChatInfo chatInfo) {
        ChatMessage chatMessage = chatInfo.toEntity();
        chatService.saveMassage(chatMessage);

        operations.convertAndSend("/sub/" + chatInfo.roomId() , chatInfo);
    }
}
