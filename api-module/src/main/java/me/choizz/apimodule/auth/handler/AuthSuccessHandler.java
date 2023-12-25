package me.choizz.apimodule.auth.handler;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import me.choizz.apimodule.api.controller.dto.ApiResponseDto;
import me.choizz.apimodule.auth.dto.UserResponseDto;
import me.choizz.apimodule.auth.UserAttribute;
import me.choizz.apimodule.auth.UserPrincipal;
import me.choizz.chattingredismodule.dto.LoginUser;
import me.choizz.chattingredismodule.session.LoginUsers;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final LoginUsers loginUsers;

    @Override
    public void onAuthenticationSuccess(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication
    ) throws IOException, ServletException {

        LoginUser loginUser = addLoginUser(request, authentication);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setStatus(SC_OK);

        String userData =
            objectMapper.writeValueAsString(
                new ApiResponseDto<>(
                    UserResponseDto.of(loginUser)
                )
            );

        response.getWriter().write(userData);
    }

    private LoginUser addLoginUser(
        final HttpServletRequest request,
        final Authentication authentication
    ) {
        HttpSession session = request.getSession(false);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UserAttribute userAttribute = principal.getUserAttribute();
        LoginUser loginUser = LoginUser.builder()
            .sessionId(session.getId())
            .userId(userAttribute.userId())
            .nickname(userAttribute.nickname())
            .email(userAttribute.email())
            .build();

        loginUsers.addLoginUser(loginUser);
        return loginUser;
    }
}
