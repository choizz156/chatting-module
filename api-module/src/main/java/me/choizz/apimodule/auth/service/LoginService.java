package me.choizz.apimodule.auth.service;

import jakarta.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.auth.dto.LoginUser;
import me.choizz.domainjpamodule.exception.ApiBusinessLogicException;
import me.choizz.domainjpamodule.exception.ApiExceptionCode;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRepository;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {

    private final RedisOperations<String, Object> redisOperations;
    private final ConcurrentHashMap<String, String> sessionKeyStore = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private BoundSetOperations<String, Object> loginUsers;

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

        LoginUser loginSession = getExistSession(session);

        if (loginSession != null) {
            loginUsers.remove(loginSession.email());
            sessionKeyStore.remove(loginSession.email());
            session.invalidate();
            return true;
        }

        return false;
    }

    public void logout(final HttpSession session) {
        if (session == null) {
            return;
        }

        LoginUser existSession = getExistSession(session);
        loginUsers.remove(existSession.email());
        sessionKeyStore.remove(existSession.email());
        session.invalidate();
    }


    @Scheduled(fixedDelay = 3600000)
    private void checkLeakMemory() {
        if (!sessionKeyStore.isEmpty()) {
            Enumeration<String> email = sessionKeyStore.keys();
            email.asIterator().forEachRemaining(k ->
                {
                    LoginUser loginUser =
                        (LoginUser) redisOperations
                            .opsForHash()
                            .get(sessionKeyStore.get(k), SessionKey.LOGIN_USER.name());

                    if (loginUser == null) {
                        loginUsers.remove(k);
                        sessionKeyStore.remove(k);
                    }
                }
            );
        }
    }

    private void deleteExistSession(final String email) {
        loginUsers = redisOperations.boundSetOps("LoginUsers");
        Boolean member = loginUsers.isMember(email);

        if (loginUsers.members() != null && member) {
            redisOperations.opsForHash()
                .delete(sessionKeyStore.get(email), SessionKey.LOGIN_USER.name());
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
        String sessionKey = SessionKey.of(session.getId());
        redisOperations.opsForHash()
            .put(sessionKey, SessionKey.LOGIN_USER.name(), loginUser);
        sessionKeyStore.put(loginUser.email(), sessionKey);
        loginUsers.add(loginUser.email());
    }

    private LoginUser getExistSession(final HttpSession session) {
        String sessionKey = SessionKey.of(session.getId());
        return (LoginUser)
            redisOperations.opsForHash().get(sessionKey, SessionKey.LOGIN_USER.name());
    }
}
