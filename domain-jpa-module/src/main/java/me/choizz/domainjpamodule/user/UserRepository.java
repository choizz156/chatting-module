package me.choizz.domainjpamodule.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsUserByNickname(String nickname);
    boolean existsUserByEmail(String email);

    @Query("select u from User u where u.id = :hostId or u.id = :clientId")
    List<User> findUsers(@Param("hostId") Long hostId, @Param("clientId") Long clientId);
}
