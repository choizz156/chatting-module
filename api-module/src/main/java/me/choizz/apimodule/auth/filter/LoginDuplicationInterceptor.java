package me.choizz.apimodule.auth.filter;

import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;
import static java.nio.charset.StandardCharsets.UTF_8;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.auth.service.SessionName;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginDuplicationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Object handler
    ) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionName.LOGIN_USER.name()) == null) {
            return true;
        }

        log.warn("{}", session.getId());
        session.getAttributeNames().asIterator().forEachRemaining(System.out::println);
        response.setStatus(SC_CONFLICT);
        response.setCharacterEncoding(UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("이미 로그인되어 있습니다.");
        return false;
    }
}
