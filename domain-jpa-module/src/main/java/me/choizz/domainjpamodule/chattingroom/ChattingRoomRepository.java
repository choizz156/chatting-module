package me.choizz.domainjpamodule.chattingroom;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    @Query("select r from ChattingRoom  r where r.host.id = :hostId and r.client.id = :clientId or r.host.id = :clientId and r.client.id = :hostId")
    Optional<ChattingRoom> findChattingRoomByHostIdAndClientId(
        @Param("hostId") Long hostId,
        @Param("clientId") Long clientId
    );
}
