package me.choizz.chattingserver.domain.user.repository;

import java.util.Optional;
import me.choizz.chattingserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {

    Optional<User> findByEmail(String username);
}
