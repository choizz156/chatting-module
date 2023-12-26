package me.choizz.apimodule.auth;

import lombok.RequiredArgsConstructor;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDetailsVerification implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger("fileLog");

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        logger.info("loadUserByUsername => {}", username);
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 회원입니다."));

        UserAttribute userAttribute = UserAttribute.builder()
            .userId(user.getId())
            .nickname(user.getNickname())
            .email(user.getEmail())
            .password(user.getPassword())
            .userRole(user.getRoles())
            .build();

        return new UserPrincipal(userAttribute);
    }
}
