package me.choizz.apimodule.auth.handler;

import static me.choizz.apimodule.auth.handler.AuthErrorResponseSender.sendError;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@RequiredArgsConstructor
public class AuthEntryPointHandler implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger("fileLog");
    private final ObjectMapper objectMapper;
    @Override
    public void commence(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AuthenticationException authException
    ) throws IOException, ServletException {
        logger.warn("AuthEntryPointHandler in");
        sendError(response, authException, UNAUTHORIZED, objectMapper);
    }
}
