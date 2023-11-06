package me.choizz.chattingserver.global.exception;

import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingserver.api.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDto<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        return new ApiResponseDto<>(ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getBindingResult()));
    }

    @ExceptionHandler(BusinessLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDto<ErrorResponse> businessLogicExceptionHandler(BusinessLoginException e) {
        return new ApiResponseDto<>(ErrorResponse.of(e.getMessage()));
    }
}
