package me.choizz.chattingserver.api.controller.chattingroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingserver.api.ApiResponseDto;
import me.choizz.chattingserver.api.controller.chattingroom.dto.ChatRoomRequest;
import me.choizz.chattingserver.api.controller.chattingroom.dto.ChatRoomResponse;
import me.choizz.chattingserver.api.service.chattingroom.ChattingRoomService;
import me.choizz.chattingserver.domain.chattingroom.ChattingRoom;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChattingRoomController {

    private final ChattingRoomService chattingRoomService;

    @PostMapping("/chatting-rooms")
    public ApiResponseDto<ChatRoomResponse> chattingRoom(
        @RequestBody ChatRoomRequest chatRoomRequest
    ) {

        ChattingRoom chattingRoom = chattingRoomService.createOneToOne(
            chatRoomRequest.getName(),
            chatRoomRequest.getHostId(),
            chatRoomRequest.getClientId()
        );

        return new ApiResponseDto<>(
            ChatRoomResponse.of(
                chattingRoom.getId(),
                chattingRoom.getHost(),
                chattingRoom.getClient()
            )
        );
    }
}
