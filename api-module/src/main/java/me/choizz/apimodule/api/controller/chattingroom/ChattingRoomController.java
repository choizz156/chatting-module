package me.choizz.apimodule.api.controller.chattingroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.api.controller.dto.ApiResponseDto;
import me.choizz.domainjpamodule.chattingroom.ChattingRoom;
import me.choizz.domainjpamodule.chattingroom.ChattingRoomService;
import me.choizz.domainjpamodule.dto.ChatRoomRequestDto;
import me.choizz.domainjpamodule.dto.ChatRoomResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatting-rooms")
@RestController
public class ChattingRoomController {

    private static final Logger logger = LoggerFactory.getLogger("fileLog");
    private final ChattingRoomService chattingRoomService;

    @PostMapping
    public ApiResponseDto<ChatRoomResponseDto> createChattingRoom(
        @RequestBody ChatRoomRequestDto requestDto
    ) {
        logger.info("chatRoomInfo => {}", requestDto);
        ChattingRoom chattingRoom = chattingRoomService.createOneToOne(
            requestDto.getName(),
            requestDto.getHostId(),
            requestDto.getClientId()
        );

        return new ApiResponseDto<>(
            ChatRoomResponseDto.of(
                chattingRoom.getId(),
                chattingRoom.getHostNickName(),
                chattingRoom.getClientName()
            )
        );
    }
}
