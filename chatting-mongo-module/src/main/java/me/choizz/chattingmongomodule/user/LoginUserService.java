package me.choizz.chattingmongomodule.user;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginUserService {

    private final LoginUserRepository loginUserRepository;


    public void connectUser(LoginUser loginUser){
        loginUserRepository.save(loginUser);
    }

    public void disconnectUser(LoginUser user){
        loginUserRepository.deleteById(user.getUserId());
    }

    public List<LoginUser> findConnectedUsers(){
        return loginUserRepository.findAll();
    }
}
