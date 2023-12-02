package me.choizz.domainjpamodule.chattingroom;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.domainjpamodule.exception.ApiBusinessLogicException;
import me.choizz.domainjpamodule.exception.ExceptionCode;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChattingRoomService {

    private final ChattingRoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChattingRoom createOneToOne(
        final String roomName,
        final Long hostId,
        final Long clientId
    ) {

        Optional<ChattingRoom> roomOptional = checkDuplicationOfChattingRoom(hostId, clientId);
        if(roomOptional.isPresent()){
            return roomOptional.get();
        }
        
        ChattingUser users = getChattingUser(hostId, clientId);
        ChattingRoom room = new ChattingRoom(roomName);
        room.makeChattingRoom(users.host(), users.client());
        return roomRepository.save(room);
    }

    private Optional<ChattingRoom> checkDuplicationOfChattingRoom(final Long hostId, final Long clientId) {

        List<ChattingRoom> hostRoom =
            roomRepository.findChattingRoomByHostIdAndClientId(hostId, clientId);

        if (hostRoom.size() == 1) {
            return Optional.of(hostRoom.get(0));
        }

        List<ChattingRoom> clientRoom =
            roomRepository.findChattingRoomByHostIdAndClientId(clientId, hostId);

        if (clientRoom.size() == 1) {
            return Optional.of(clientRoom.get(0)); 
        }

        return Optional.empty();
    }

    private ChattingUser getChattingUser(final Long hostId, final Long clientId) {

        User host = userRepository.findById(hostId)
            .orElseThrow(() -> new ApiBusinessLogicException(ExceptionCode.NOT_FOUND_UER));

        User client = userRepository.findById(clientId)
            .orElseThrow(() -> new ApiBusinessLogicException(ExceptionCode.NOT_FOUND_UER));

        return new ChattingUser(host, client);
    }

    private record ChattingUser(User host, User client) {

    }
}
