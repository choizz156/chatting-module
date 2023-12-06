package me.choizz.apimodule.auth.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.auth.dto.LoginUser;
import me.choizz.domainjpamodule.exception.ApiBusinessLogicException;
import me.choizz.domainjpamodule.exception.ApiExceptionCode;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(final String email, final String password) {
        return checkAuthentication(email, password);
    }

    private User checkAuthentication(final String email, final String password) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(
                () -> new ApiBusinessLogicException(ApiExceptionCode.NOT_FOUND_UER)
            );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ApiBusinessLogicException(ApiExceptionCode.NONE_AUTH);
        }

        return user;
    }

    public LoginUser createSession(final HttpSession session, final User user) {
        LoginUser loginUser =
            LoginUser.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .roles(user.getRoles().name())
                .build();

        session.setAttribute(SessionName.LOGIN_USER.name(), loginUser);
        return loginUser;
    }

    public void logout(final HttpSession httpSession) {
        if(httpSession == null){
           return;
        }
        httpSession.invalidate();
    }
}
