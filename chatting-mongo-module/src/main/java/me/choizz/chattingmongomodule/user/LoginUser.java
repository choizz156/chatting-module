package me.choizz.chattingmongomodule.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document
public class LoginUser {

    @Id
    private String id;
    @Indexed(unique = true)
    private Long userId;
    private String email;
    private String nickname;

    @Builder
    public LoginUser(final Long userId, final String email, final String nickname) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
    }
}
