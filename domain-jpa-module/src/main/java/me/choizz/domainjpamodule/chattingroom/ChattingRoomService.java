package me.choizz.domainjpamodule.chattingroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.domainjpamodule.exception.BusinessLogicException;
import me.choizz.domainjpamodule.exception.ExceptionCode;
import me.choizz.domainjpamodule.user.User;
import me.choizz.domainjpamodule.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@RequiredArgsConstructor
@Service
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChattingRoom createOneToOne(
        final String roomName,
        final Long hostId,
        final Long clientId
    ) {
        log.error("sdfsdfsdfsdf");
        ChattingUser users = getChattingUser(hostId, clientId);
        ChattingRoom room = new ChattingRoom(roomName);
        room.makeChattingRoom(users.host(), users.client());

        return chattingRoomRepository.save(room);
    }

    private ChattingUser getChattingUser(final Long hostId, final Long clientId) {
        User host = userRepository.findById(hostId)
            .orElseThrow(() -> new BusinessLogicException (ExceptionCode.NOT_FOUND_UER));

        User client = userRepository.findById(clientId)
            .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NOT_FOUND_UER));

        checkDuplicationOfChattingRoom(host, client);

        return new ChattingUser(host, client);
    }

    private void checkDuplicationOfChattingRoom(final User host, final User client) {
        boolean isExistRoom = chattingRoomRepository.existsChattingRoomByHostAndClient(host, client);

        if(isExistRoom){
            throw new BusinessLogicException(ExceptionCode.EXIST_CHATTING_ROOM);
        }
    }

    private record ChattingUser(User host, User client) {
    }
}
