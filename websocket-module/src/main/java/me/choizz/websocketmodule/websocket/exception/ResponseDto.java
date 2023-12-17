package me.choizz.websocketmodule.websocket.exception;

import java.time.LocalDateTime;

public record ResponseDto<T>(
    LocalDateTime time,
    T data
) {

    public ResponseDto(final T data) {
       this(LocalDateTime.now(), data);
    }
}
