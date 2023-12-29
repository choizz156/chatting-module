package me.choizz.domainjpamodule.dto;

import jakarta.validation.constraints.NotNull;


public record ChatRoomRequestDto(
    String name,

    @NotNull
    Long hostId,

    @NotNull
    Long clientId
) {

}
