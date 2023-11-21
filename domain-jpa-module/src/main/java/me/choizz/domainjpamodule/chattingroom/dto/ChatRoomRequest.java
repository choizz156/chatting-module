package me.choizz.domainjpamodule.chattingroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRoomRequest {

    private String name;

    @NotBlank
    private Long hostId;

    @NotBlank
    private Long clientId;

    public void toDefaultName(final String nickname) {
        this.name = nickname;
    }
}
