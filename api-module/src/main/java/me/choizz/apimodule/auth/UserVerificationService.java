package me.choizz.apimodule.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.auth.dto.UserDetailsDto;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserVerificationService implements UserDetailsService {

   private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(
        final String username
    ) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 회원입니다."));

        UserDetailsDto detailsDto = UserDetailsDto.builder()
            .email(user.getEmail())
            .password(user.getPassword())
            .role(user.getRoles().name())
            .build();

        return new UserDetail(detailsDto);
    }
}
