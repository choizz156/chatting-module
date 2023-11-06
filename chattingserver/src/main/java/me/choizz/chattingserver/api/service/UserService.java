package me.choizz.chattingserver.api.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingserver.api.controller.user.dto.JoinDto;
import me.choizz.chattingserver.domain.user.UserRepository;
import me.choizz.chattingserver.domain.user.User;
import me.choizz.chattingserver.api.exception.BusinessLoginException;
import me.choizz.chattingserver.api.exception.ExceptionCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User join(final JoinDto joinDto) {
        verifyEmail(joinDto.email());
        verifyNickname(joinDto.nickname());

        User user = joinDto.toEntity();
        String pwd = passwordEncoder.encode(joinDto.password());

        user.savePwd(pwd);

        userRepository.save(user);

        return user;
    }

    private void verifyNickname(final String nickname) {
        Optional<User> optionalUser = userRepository.findByNickname(nickname);
        if(optionalUser.isPresent()){
            throw new BusinessLoginException(ExceptionCode.EXIST_NICKNAME);
        }
    }

    private void verifyEmail(final String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            throw new BusinessLoginException(ExceptionCode.EXIST_EMAIL);
        }
    }
}
