package me.choizz.domainjpamodule.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsUserByNickname(String nickname);
    boolean existsUserByEmail(String email);
}
