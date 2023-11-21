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

        User host = userRepository.findById(hostId)
            .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 유저입니다."));
        log.warn(host.getEmail());
        User client = userRepository.findById(clientId)
            .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 유저입니다."));
        log.warn(client.getEmail());

        checkDuplicationOfChattingRoom(host, client);

        ChattingRoom room = new ChattingRoom(roomName);
        room.makeChattingRoom(host, client);

        return chattingRoomRepository.save(room);
    }

    private void checkDuplicationOfChattingRoom(final User host, final User client) {
        boolean isExistRoom = chattingRoomRepository.existsChattingRoomByHostAndClient(host, client);

        if(isExistRoom){
            throw new BusinessLogicException(ExceptionCode.EXIST_CHATTING_ROOM);
        }
    }
}
