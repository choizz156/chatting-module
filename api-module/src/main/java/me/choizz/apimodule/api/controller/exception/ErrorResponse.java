package me.choizz.apimodule.api.controller.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.choizz.domainjpamodule.exception.ApiExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Getter
@ToString
@JsonInclude(Include.NON_EMPTY)
public class ErrorResponse {

    private final int status;
    private final String msg;
    private final List<CustomFieldError> customFieldErrors;

    @Builder
    private ErrorResponse(int status, String msg, List<CustomFieldError> customFieldErrors) {
        this.status = status;
        this.msg = msg;
        this.customFieldErrors = customFieldErrors;
    }

    public static ErrorResponse of(HttpStatus httpStatus, BindingResult bindingResult) {
        return builder()
            .status(httpStatus.value())
            .customFieldErrors(CustomFieldError.of(bindingResult))
            .build();
    }

    public static ErrorResponse of(ApiExceptionCode apiExceptionCode) {
        return builder()
            .status(apiExceptionCode.getCode())
            .msg(apiExceptionCode.getMsg())
            .build();
    }

    public static ErrorResponse of(HttpStatus httpStatus, String msg) {
        return builder()
            .status(httpStatus.value())
            .msg(msg)
            .build();
    }

    public static ErrorResponse of(String msg) {
        return builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .msg(msg)
            .build();
    }

    @Getter
    public static class CustomFieldError {

        private String field;
        private Object rejectedValue;
        private String reason;

        private CustomFieldError(
            final String field,
            final Object rejectedValue,
            final String reason
        ) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<CustomFieldError> of(BindingResult bindingResult) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                .map(error ->
                    new CustomFieldError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                    )
                )
                .toList();
        }
    }
}

