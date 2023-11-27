package me.choizz.apimodule.api.controller.user;

import me.choizz.domainjpamodule.user.User;

public record UserResponseDto(String email, String nickname, Long userId) {

    public static UserResponseDto of(User user){
        return new UserResponseDto(user.getEmail(), user.getNickname(), user.getId());
    }
}
