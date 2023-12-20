package me.choizz.apimodule.auth.service;

import jakarta.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.auth.dto.LoginUser;
import me.choizz.domainjpamodule.exception.ApiBusinessLogicException;
import me.choizz.domainjpamodule.exception.ApiExceptionCode;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRepository;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {

    private final RedisOperations<String, Object> redisOperations;
    private final SessionKeyStore sessionKeyStore;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginUsers loginUsers;


    public User login(final String email, final String password) {
        return checkAuthentication(email, password);
    }

    public LoginUser createSession(final HttpSession session, final User user) {

        LoginUser loginUser =
            LoginUser.builder()
                .email(user.getEmail())
                .roles(user.getRoles().name())
                .nickname(user.getNickname())
                .userId(user.getId())
                .build();

        addSession(session, loginUser);
        return loginUser;
    }

    public boolean isExistSession(final String email, final HttpSession session) {
        deleteExistSession(email);

        LoginUser loginUser = getExistSession(session);

        if (loginUser != null) {
            removeSession(session, loginUser);
            return true;
        }

        return false;
    }

    public void logout(final HttpSession session) {
        if (session == null) {
            return;
        }

        LoginUser existSession = getExistSession(session);
        if (existSession != null) {
            removeSession(session, existSession);
        }
    }

    private void removeSession(final HttpSession session, final LoginUser existSession) {
        loginUsers.removeValue(existSession.email());
        sessionKeyStore.removeValue(existSession.email());
        session.invalidate();
    }


    private void deleteExistSession(final String email) {
        boolean isExistUser = loginUsers.contains(email);
        if (isExistUser) {
            redisOperations
                .opsForValue()
                .getOperations()
                .delete(sessionKeyStore.getValue(email));
            log.warn("{}", sessionKeyStore.getValue(email));
        }
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

    private void addSession(final HttpSession session, final LoginUser loginUser) {
        session.setAttribute(SessionKey.LOGIN_USER.name(), loginUser);
        String sessionKey = SessionKey.of(session.getId());
        sessionKeyStore.addValue(loginUser.email(), sessionKey);
        loginUsers.addLoginUser(loginUser.email());


    }

    private LoginUser getExistSession(final HttpSession session) {
        return (LoginUser) session.getAttribute(SessionKey.LOGIN_USER.name());
    }

    @Scheduled(fixedDelay = 3600000)
    private void checkLeakMemory() {
        if (!sessionKeyStore.isEmpty()) {
            Enumeration<String> email = sessionKeyStore.getKeys();
            email.asIterator().forEachRemaining(k ->
                {

                    List<Object> values = redisOperations
                        .opsForHash()
                        .values(sessionKeyStore.getValue(k));

                    if (values.isEmpty()) {
                        loginUsers.removeValue(k);
                        sessionKeyStore.removeValue(k);
                    }
                }
            );
        }
    }
}
