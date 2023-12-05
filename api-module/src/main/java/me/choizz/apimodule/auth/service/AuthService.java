package me.choizz.apimodule.auth.service;

import jakarta.servlet.http.HttpSession;
import me.choizz.apimodule.auth.dto.LoginUser;
import me.choizz.domainjpamodule.exception.ApiBusinessLogicException;
import me.choizz.domainjpamodule.exception.ApiExceptionCode;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public void checkAuth(final HttpSession httpSession) {
        checkExistSession(httpSession);
        checkAuthorization(httpSession);
    }

    private void checkAuthorization(final HttpSession httpSession) {
        LoginUser loginUser = (LoginUser) httpSession.getAttribute(SessionName.LOGIN_USER.name());

        if (!loginUser.roles().equals("USER")) {
            throw new ApiBusinessLogicException(ApiExceptionCode.NONE_ACCESS);
        }
    }

    private void checkExistSession(final HttpSession httpSession) {
        if (httpSession == null) {
                throw new ApiBusinessLogicException(ApiExceptionCode.NONE_ACCESS);
        }
    }

}
