package me.choizz.domainjpamodule.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRole;

public record JoinDto(
    @Email
    String email,

    @NotEmpty
    String password,

    @NotEmpty
    String nickname
) {

    public User toEntity() {
        return User.builder()
            .email(email())
            .roles(UserRole.USER)
            .password(password())
            .nickname(nickname)
            .build();
    }
}
