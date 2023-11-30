package me.choizz.chattingmongomodule.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@NoArgsConstructor
@Document
public class LoginUser {

    @MongoId(targetType = FieldType.INT64)
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
