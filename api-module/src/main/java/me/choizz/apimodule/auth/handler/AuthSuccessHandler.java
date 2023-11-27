package me.choizz.apimodule.auth.handler;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import me.choizz.apimodule.api.controller.ApiResponseDto;
import me.choizz.apimodule.api.controller.user.UserResponseDto;
import me.choizz.apimodule.auth.UserDetail;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication
    ) throws IOException, ServletException {

        UserDetail principal = (UserDetail) authentication.getPrincipal();
        User user = userRepository.findByEmail(principal.getUsername()).orElseThrow();

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setStatus(SC_OK);

        String userData =
            objectMapper.writeValueAsString(new ApiResponseDto<>(UserResponseDto.of(user)));

        response.getWriter().write(userData);
    }
}
