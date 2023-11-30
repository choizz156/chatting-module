package me.choizz.domainjpamodule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRoomRequestDto {

    private String name;

    @NotBlank
    private Long hostId;

    @NotBlank
    private Long clientId;

    public void toDefaultName(final String nickname) {
        this.name = nickname;
    }
}
