package me.choizz.apimodule.auth.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingredismodule.dto.LoginUser;
import me.choizz.chattingredismodule.session.SessionKey;
import me.choizz.domainjpamodule.exception.ApiBusinessLogicException;
import me.choizz.domainjpamodule.exception.ApiExceptionCode;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final RedisOperations<String, Object> redisOperations;

    public void checkAuth(final HttpSession httpSession) {
        checkExistSession(httpSession);
        checkAuthorization(httpSession);
    }

    private void checkAuthorization(final HttpSession session) {
        String sessionKey = SessionKey.of(session.getId());
        LoginUser loginUser = (LoginUser) session.getAttribute(SessionKey.LOGIN_USER.name());
        log.warn("{}", loginUser);
        if (loginUser == null || !loginUser.roles().equals("USER")) {
            throw new ApiBusinessLogicException(ApiExceptionCode.NONE_ACCESS);
        }
    }

    private void checkExistSession(final HttpSession httpSession) {
        if (httpSession == null) {
            throw new ApiBusinessLogicException(ApiExceptionCode.NONE_ACCESS);
        }
    }

}
