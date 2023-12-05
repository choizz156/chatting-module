package me.choizz.chattingmongomodule.user;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConnectedUserService {

    private final ConnectedUserRepository connectedUserRepository;

    public void connectUser(ConnectedUser connectedUser){
        connectedUserRepository.save(connectedUser);
    }

    public void disconnectUser(Long userId){
        connectedUserRepository.deleteById(userId);
    }

    public List<ConnectedUser> findConnectedUsers(){
        return connectedUserRepository.findAll();
    }
}
