package me.choizz.apimodule.auth.handler;

import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.api.controller.dto.ApiResponseDto;
import me.choizz.apimodule.api.controller.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
@RequiredArgsConstructor(access = PRIVATE)
class AuthErrorResponseSender {

    private static final Logger logger = LoggerFactory.getLogger("fileLog");
    private final ObjectMapper objectMapper;

    public static void sendError(
        HttpServletResponse response,
        Exception exception,
        HttpStatus status,
        ObjectMapper objectMapper
    ) throws IOException {

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setStatus(status.value());

        ErrorResponse errorInfo = ErrorResponse.of(status, exception.getMessage());
        logger.warn("auth error => {}", errorInfo);
        String errorResponse =
            objectMapper.writeValueAsString(new ApiResponseDto<>(errorInfo));

        response.getWriter().write(errorResponse);
    }
}
