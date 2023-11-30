package me.choizz.domainjpamodule.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.domainjpamodule.chattingroom.dto.JoinDto;
import me.choizz.domainjpamodule.exception.BusinessLogicException;
import me.choizz.domainjpamodule.exception.ExceptionCode;
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
        checkDuplicationOfEmail(joinDto.email());
        checkDuplicationOfNickname(joinDto.nickname());

        User user = joinDto.toEntity();
        var password = passwordEncoder.encode(joinDto.password());

        user.savePassword(password);

        userRepository.save(user);

        return user;
    }

    private void checkDuplicationOfNickname(final String nickname) {
        if (userRepository.existsUserByNickname(nickname)) {
            throw new BusinessLogicException(ExceptionCode.EXIST_NICKNAME);
        }
    }

    private void checkDuplicationOfEmail(final String email) {
        if (userRepository.existsUserByEmail(email)) {
            throw new BusinessLogicException(ExceptionCode.EXIST_EMAIL);
        }
    }
}