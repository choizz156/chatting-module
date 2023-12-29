package me.choizz.domainjpamodule.chattingroom;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.domainjpamodule.exception.ApiBusinessLogicException;
import me.choizz.domainjpamodule.exception.ApiExceptionCode;
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

        Optional<ChattingRoom> chattingRoom = checkDuplicationOfChattingRoom(hostId, clientId);
        if (chattingRoom.isPresent()) {
            return chattingRoom.get();
        }

        ChattingUser users = getChattingUser(hostId, clientId);
        ChattingRoom room = new ChattingRoom(roomName);
        room.makeChattingRoom(users.host(), users.client());
        return roomRepository.save(room);
    }

    private Optional<ChattingRoom> checkDuplicationOfChattingRoom(
        final Long hostId,
        final Long clientId
    ) {
        return roomRepository.findChattingRoomByHostIdAndClientId(hostId, clientId);
    }

    private ChattingUser getChattingUser(final Long hostId, final Long clientId) {
        List<User> users = userRepository.findUsers(clientId, hostId);
        checkExistUsers(users);
        return new ChattingUser(users.get(0), users.get(1));
    }

    private void checkExistUsers(final List<User> users) {
        if(users.size() != 2){
            throw new ApiBusinessLogicException(ApiExceptionCode.NOT_FOUND_UER);
        }
    }

    private record ChattingUser(User host, User client) {
    }
}
