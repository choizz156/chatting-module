package me.choizz.chattingserver.api.service.chattingroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.choizz.chattingserver.api.exception.BusinessLogicException;
import me.choizz.chattingserver.api.exception.ExceptionCode;
import me.choizz.chattingserver.domain.chattingroom.ChattingRoom;
import me.choizz.chattingserver.domain.chattingroom.ChattingRoomRepository;
import me.choizz.chattingserver.domain.user.User;
import me.choizz.chattingserver.domain.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 유저입니다."));
        log.warn(host.getEmail());
        User client = userRepository.findById(clientId)
            .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 유저입니다."));
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
