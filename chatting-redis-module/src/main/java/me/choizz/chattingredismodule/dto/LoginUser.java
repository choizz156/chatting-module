package me.choizz.chattingredismodule.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Builder;


@Builder
public record LoginUser(Long userId, String email, String nickname, String roles) implements
    Serializable {

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LoginUser loginUser = (LoginUser) o;
        return Objects.equals(userId, loginUser.userId) && Objects.equals(email,
            loginUser.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }
}
