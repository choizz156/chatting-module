package me.choizz.apimodule.auth.dto;

import lombok.Builder;
import me.choizz.apimodule.auth2.UserAttribute;
import me.choizz.chattingredismodule.dto.LoginUser;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRole;

@Builder
public record UserResponseDto(String email, String nickname, Long userId, UserRole userRole) {

    public static UserResponseDto of(final LoginUser user) {
        return UserResponseDto.builder().email(user.email()).userId(user.userId()).build();
    }

    public static UserResponseDto of(final User user) {
        return new UserResponseDto(
            user.getEmail(),
            user.getNickname(),
            user.getId(),
            user.getRoles()
        );
    }

    public static UserResponseDto of(final UserAttribute user) {
        return new UserResponseDto(
            user.email(),
            user.nickname(),
            user.userId(),
            user.userRole()
        );
    }

}
