package me.choizz.chattingserver.domain.chattingroom;

import me.choizz.chattingserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    boolean existsChattingRoomByHostAndClient(User host, User client);
}
