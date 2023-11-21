package me.choizz.domainjpamodule.chattingroom;

import me.choizz.domainjpamodule.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    boolean existsChattingRoomByHostAndClient(User host, User client);
}
