package me.choizz.chattingserver.api;

import java.time.LocalDateTime;

public record ApiResponseDto<T>(
    LocalDateTime time,
    T data
) {

    public ApiResponseDto(final T data) {
       this(LocalDateTime.now(), data);
    }
}
