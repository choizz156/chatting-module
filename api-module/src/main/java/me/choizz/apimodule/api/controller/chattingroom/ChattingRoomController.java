package me.choizz.apimodule.api.controller.chattingroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.api.controller.ApiResponseDto;
import me.choizz.domainjpamodule.chattingroom.ChattingRoom;
import me.choizz.domainjpamodule.chattingroom.ChattingRoomService;
import me.choizz.domainjpamodule.chattingroom.dto.ChatRoomRequest;
import me.choizz.domainjpamodule.chattingroom.dto.ChatRoomResponse;
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
