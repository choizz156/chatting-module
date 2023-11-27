package me.choizz.apimodule.api.controller.exception;

import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.api.controller.ApiResponseDto;
import me.choizz.domainjpamodule.exception.BusinessLogicException;
import me.choizz.apimodule.api.controller.ErrorResponse;
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
        log.error("MethodArgumentNotValidException");
        return new ApiResponseDto<>(ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getBindingResult()));
    }

    @ExceptionHandler(BusinessLogicException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDto<ErrorResponse> businessLogicExceptionHandler(BusinessLogicException e) {
        return new ApiResponseDto<>(ErrorResponse.of(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponseDto<String> exceptionHandler(Exception e) {

        return new ApiResponseDto<>("알 수 없는 오류가 발생했습니다.");
    }


}
