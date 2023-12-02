package me.choizz.apimodule.auth.handler;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.api.controller.dto.ApiResponseDto;
import me.choizz.apimodule.api.controller.user.UserResponseDto;
import me.choizz.apimodule.auth.details.UserDetail;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
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
        log.error("{}", principal.getAuthorities());
        User user = userRepository.findByEmail(principal.getUsername()).orElseThrow();

        HttpSession session = request.getSession();
        String id = session.getId();
        log.warn("{}", id);
        ServletContext servletContext = session.getServletContext();
        Enumeration<String> attributeNames = session.getAttributeNames();
        attributeNames.asIterator().forEachRemaining(n -> System.out.println(session.getAttribute(n)));

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setStatus(SC_OK);

        String userData =
            objectMapper.writeValueAsString(new ApiResponseDto<>(UserResponseDto.of(user)));

        response.getWriter().write(userData);
    }
}
