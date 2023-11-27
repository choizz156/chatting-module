package me.choizz.websocketmodule.websocket.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.Builder;
import me.choizz.chattingmongomodule.dto.ChatMessageDto;


@Builder
public record ChatInfo(@NotBlank Long roomId, @NotEmpty String nickname, @NotEmpty String msg) {

    public ChatMessageDto toEntity(LocalDateTime createTime) {
        return ChatMessageDto.builder()
            .message(msg())
            .nickname(nickname())
            .createdAt(createTime)
            .build();
    }

}
