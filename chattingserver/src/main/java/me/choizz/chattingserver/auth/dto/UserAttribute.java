package me.choizz.chattingserver.auth.dto;

import lombok.Builder;
import me.choizz.chattingserver.domain.user.UserRole;

public record UserAttribute(String email, String password, String nickname, UserRole userRole) {

    @Builder
    public UserAttribute(
        final String email,
        final String password,
        final String nickname,
        final UserRole userRole
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }

//    public static UserAttribute of(User user){
//        return new UserAttribute(user.getEmail(), user.getPassword(), user.getNickname());
//    }
}
