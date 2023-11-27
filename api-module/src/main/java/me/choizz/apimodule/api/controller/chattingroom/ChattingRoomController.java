package me.choizz.apimodule.api.controller.chattingroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.api.controller.ApiResponseDto;
import me.choizz.domainjpamodule.chattingroom.ChattingRoom;
import me.choizz.domainjpamodule.chattingroom.ChattingRoomService;
import me.choizz.domainjpamodule.chattingroom.dto.ChatRoomRequestDto;
import me.choizz.domainjpamodule.chattingroom.dto.ChatRoomResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatting-rooms")
@RestController
public class ChattingRoomController {

    private final ChattingRoomService chattingRoomService;

    @PostMapping
    public ApiResponseDto<ChatRoomResponseDto> chattingRoom(
        @RequestBody ChatRoomRequestDto chatRoomRequestDto
    ) {

        ChattingRoom chattingRoom = chattingRoomService.createOneToOne(
            chatRoomRequestDto.getName(),
            chatRoomRequestDto.getHostId(),
            chatRoomRequestDto.getClientId()
        );

        return new ApiResponseDto<>(
            ChatRoomResponseDto.of(
                chattingRoom.getId(),
                chattingRoom.getHost().getNickname(),
                chattingRoom.getClient().getNickname()
            )
        );
    }
}
