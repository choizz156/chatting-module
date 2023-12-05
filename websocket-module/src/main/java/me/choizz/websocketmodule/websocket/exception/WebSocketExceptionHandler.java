package me.choizz.websocketmodule.websocket.exception;

import com.mongodb.MongoSocketClosedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebSocketExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponseDto<ErrorResponse> methodArgumentNotValidExceptionHandler(MongoSocketClosedException e) {
        return new ApiResponseDto<>(ErrorResponse.of("알 수 없는 오류가 발생했습니다."));
    }
}
