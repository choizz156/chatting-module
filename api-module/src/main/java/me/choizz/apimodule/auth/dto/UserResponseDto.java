package me.choizz.apimodule.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import me.choizz.chattingredismodule.dto.LoginUser;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRole;

@Builder
@JsonInclude(Include.NON_EMPTY)
public record UserResponseDto(String email, String nickname, Long userId, UserRole userRole) {

    public static UserResponseDto of(final LoginUser user) {
        return UserResponseDto.builder()
            .email(user.email())
            .userId(user.userId())
            .nickname(user.nickname())
            .build();
    }

    public static UserResponseDto of(final User user) {
        return new UserResponseDto(
            user.getEmail(),
            user.getNickname(),
            user.getId(),
            user.getRoles()
        );
    }
}
