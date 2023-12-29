package me.choizz.domainjpamodule.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRole;

public record JoinDto(
    @Email(message = "@를 포함해야합니다.")
    @NotEmpty(message = "이메일은 공백일 수 없습니다.")
    String email,

    @NotBlank
    String password,

    @NotBlank
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
