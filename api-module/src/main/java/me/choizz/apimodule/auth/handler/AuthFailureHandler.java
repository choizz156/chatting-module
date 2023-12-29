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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@RequiredArgsConstructor
public class AuthFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger("fileLog");
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AuthenticationException exception
    ) throws IOException, ServletException {
        logger.warn("AuthFailureHandler in");
        sendError(response, exception, UNAUTHORIZED, objectMapper);
    }
}
