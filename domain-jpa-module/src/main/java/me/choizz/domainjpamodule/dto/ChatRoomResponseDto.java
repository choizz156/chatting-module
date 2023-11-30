package me.choizz.domainjpamodule.dto;


import lombok.Builder;

public record ChatRoomResponseDto(Long roomId, String hostNickname, String clientNickname) {

    @Builder
    public ChatRoomResponseDto(
        final Long roomId,
        final String hostNickname,
        final String clientNickname
    ) {
        this.roomId = roomId;
        this.hostNickname = hostNickname;
        this.clientNickname = clientNickname;
    }

    public static ChatRoomResponseDto of(Long roomId, String hostNickname, String clientNickname) {
        return new ChatRoomResponseDto(roomId, hostNickname, clientNickname);
    }

    public static ChatRoomResponseDto of(final Long roomId) {
        return ChatRoomResponseDto.builder().roomId(roomId).build() ;
    }
}
