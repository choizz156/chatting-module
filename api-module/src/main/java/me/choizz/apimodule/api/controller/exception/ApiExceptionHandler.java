package me.choizz.apimodule.api.controller.exception;

import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.api.controller.dto.ApiResponseDto;
import me.choizz.domainjpamodule.exception.ApiBusinessLogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger("fileLog");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDto<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        logger.info("MethodArgumentNotValidException => {}", e.getBindingResult());
        return new ApiResponseDto<>(ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getBindingResult()));
    }

    @ExceptionHandler(ApiBusinessLogicException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDto<ErrorResponse> businessLogicExceptionHandler(ApiBusinessLogicException e) {
        return new ApiResponseDto<>(ErrorResponse.of(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponseDto<ErrorResponse> exceptionHandler(Exception e) {
        logger.error("internal server error => {}", e.getMessage());
        return new ApiResponseDto<>(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR,"알 수 없는 오류가 발생했습니다."));
    }
}
