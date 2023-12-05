package me.choizz.apimodule.auth.dto;

import lombok.Builder;
import me.choizz.domainjpamodule.user.User;

@Builder
public record UserResponseDto(String email, String nickname, Long userId) {

    public static UserResponseDto of(LoginUser user){
        return UserResponseDto.builder().email(user.email()).userId(user.userId()).build();
    }

    public static UserResponseDto of(User user){
        return new UserResponseDto(user.getEmail(), user.getNickname(), user.getId());
    }

}
