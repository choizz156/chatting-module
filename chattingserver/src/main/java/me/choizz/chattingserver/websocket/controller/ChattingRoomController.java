package me.choizz.chattingserver.websocket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingserver.api.ApiResponseDto;
import me.choizz.chattingserver.websocket.domain.ChattingRoom;
import me.choizz.chattingserver.websocket.dto.ChatRoomRequest;
import me.choizz.chattingserver.websocket.dto.ChatRoomResponse;
import me.choizz.chattingserver.websocket.service.ChatService;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChattingRoomController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations operations;

    @PostMapping("/rooms")
    public ApiResponseDto<ChatRoomResponse> chattingRoom(@RequestBody ChatRoomRequest chatRoomRequest) {

        if(chatRoomRequest.getName().isEmpty()){
            chatRoomRequest.toDefaultName(chatRoomRequest.getNickname());
        }

        ChattingRoom chattingRoom = chatService.createChattingRoom(chatRoomRequest.getName());
        return new ApiResponseDto<>(new ChatRoomResponse(chattingRoom.getId(), chattingRoom.getName()));
    }
}
