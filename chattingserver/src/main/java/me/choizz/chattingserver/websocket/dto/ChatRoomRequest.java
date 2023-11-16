package me.choizz.chattingserver.websocket.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatRoomRequest {

    private String name;

    @NotNull
    private String nickname;

    public void toDefaultName(final String nickname) {
        this.name = nickname;
    }
}
