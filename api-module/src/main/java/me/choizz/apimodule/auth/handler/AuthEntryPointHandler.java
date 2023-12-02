package me.choizz.apimodule.auth.handler;

import static me.choizz.apimodule.auth.handler.AuthErrorResponseSender.sendError;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.auth.details.UserDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
@RequiredArgsConstructor
public class AuthEntryPointHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    @Override
    public void commence(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AuthenticationException authException
    ) throws IOException, ServletException {
        log.error("인증이 더 필며");
        sendError(response, authException, UNAUTHORIZED, objectMapper);
    }
}
