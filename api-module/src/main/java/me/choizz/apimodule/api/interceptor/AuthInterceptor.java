package me.choizz.apimodule.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingredismodule.dto.LoginUser;
import me.choizz.chattingredismodule.session.SessionKey;
import me.choizz.domainjpamodule.exception.ApiBusinessLogicException;
import me.choizz.domainjpamodule.exception.ApiExceptionCode;
import me.choizz.domainjpamodule.user.UserRole;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Object handler
    ) throws Exception {
        log.info(request.getRequestURI());

        if (isPreflight(request)) {
            return true;
        }

        if (checkSession(request)) {
            return true;
        }

        throw new ApiBusinessLogicException(ApiExceptionCode.SESSION_EXPIRED);
    }

    private boolean isPreflight(final HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.name());
    }

    private boolean checkSession(final HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            LoginUser attribute = (LoginUser) session.getAttribute(SessionKey.LOGIN_USER.name());
            log.warn("{}", attribute);
            return attribute != null && attribute.roles().equals(UserRole.USER.name());
        }
        return false;
    }
}
