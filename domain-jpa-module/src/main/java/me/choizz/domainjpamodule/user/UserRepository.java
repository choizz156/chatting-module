package me.choizz.domainjpamodule.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsUserByNickname(String nickname);
    boolean existsUserByEmail(String email);

    void findByEmailAndPassword(String email, String password);
}
