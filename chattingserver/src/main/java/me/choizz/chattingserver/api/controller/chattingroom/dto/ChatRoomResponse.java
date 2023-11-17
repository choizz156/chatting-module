package me.choizz.chattingserver.api.controller.chattingroom.dto;

import me.choizz.chattingserver.domain.user.User;

public record ChatRoomResponse(Long roomId, User host, User client) {

    public static ChatRoomResponse of(Long roomId, User host, User client) {
        return new ChatRoomResponse(roomId, host, client);
    }
}
