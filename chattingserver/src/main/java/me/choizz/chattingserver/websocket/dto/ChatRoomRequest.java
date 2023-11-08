package me.choizz.chattingserver.websocket.dto;

import lombok.Data;

@Data
public class ChatRoomRequest {

    private String roomName;
    private String nickname;

    public void toDefaultName(final String nickname) {
        this.roomName = nickname;
    }
}
