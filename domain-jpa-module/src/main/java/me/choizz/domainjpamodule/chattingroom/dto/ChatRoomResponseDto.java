package me.choizz.domainjpamodule.chattingroom.dto;


public record ChatRoomResponseDto(Long roomId, String hostId, String clientId) {

    public static ChatRoomResponseDto of(Long roomId, String hostId, String clientId) {
        return new ChatRoomResponseDto(roomId, hostId, clientId);
    }
}
