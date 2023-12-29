package me.choizz.apimodule.auth.handler;


import static me.choizz.apimodule.auth.handler.AuthErrorResponseSender.sendError;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@RequiredArgsConstructor
public class AuthDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger("fileLog");
    private final ObjectMapper objectMapper;

    @Override
    public void handle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        logger.warn("AuthDeniedHandler in");
        sendError(response, accessDeniedException, HttpStatus.FORBIDDEN, objectMapper);
    }
}
