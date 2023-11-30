package me.choizz.domainjpamodule.chattingroom;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {


    List<ChattingRoom> findChattingRoomByHostIdAndClientId(Long hostId, Long clientId);

}
