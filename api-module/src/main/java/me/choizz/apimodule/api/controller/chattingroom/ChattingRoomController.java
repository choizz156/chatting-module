package me.choizz.apimodule.api.controller.chattingroom;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.api.controller.ApiResponseDto;
import me.choizz.domainjpamodule.chattingroom.ChattingRoom;
import me.choizz.domainjpamodule.chattingroom.ChattingRoomService;
import me.choizz.domainjpamodule.dto.ChatRoomRequestDto;
import me.choizz.domainjpamodule.dto.ChatRoomResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatting-rooms")
@RestController
public class ChattingRoomController {

    private final ChattingRoomService chattingRoomService;

    @ResponseStatus(CREATED)
    @PostMapping
    public ApiResponseDto<ChatRoomResponseDto> chattingRoom(
        @RequestBody ChatRoomRequestDto requestDto
    ) {
        //todo: 필터에서 처리해야할듯...
        Optional<Long> roomId =
            chattingRoomService.checkDuplicationOfChattingRoom(requestDto.getHostId(), requestDto.getClientId());

        if(roomId.isPresent()){
            return new ApiResponseDto<>(
                ChatRoomResponseDto.of(roomId.get())
            );
        }

        ChattingRoom chattingRoom = chattingRoomService.createOneToOne(
            requestDto.getName(),
            requestDto.getHostId(),
            requestDto.getClientId()
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
