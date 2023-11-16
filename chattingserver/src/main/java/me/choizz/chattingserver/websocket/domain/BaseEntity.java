package me.choizz.chattingserver.websocket.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@Getter
public abstract class BaseEntity {

    @CreatedDate
    LocalDateTime createdAt;
}
