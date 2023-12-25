package me.choizz.apimodule.auth;

import java.io.Serializable;
import lombok.Builder;
import me.choizz.domainjpamodule.user.UserRole;

public record UserAttribute(
    Long userId,
    String email,
    String password,
    String nickname,
    UserRole userRole
) implements Serializable {

    @Builder
    public UserAttribute(
        final Long userId,
        final String email,
        final String password,
        final String nickname,
        final UserRole userRole
    ) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }
}
