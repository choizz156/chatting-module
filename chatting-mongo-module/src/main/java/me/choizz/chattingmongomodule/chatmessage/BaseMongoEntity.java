package me.choizz.chattingmongomodule.chatmessage;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@Getter
public abstract class BaseMongoEntity {

    @CreatedDate
    LocalDateTime createdAt;
}
