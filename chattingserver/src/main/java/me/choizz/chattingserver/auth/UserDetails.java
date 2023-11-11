package me.choizz.chattingserver.auth;

import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserDetails extends User {

    private final Long userId;

    public UserDetails(me.choizz.chattingserver.domain.user.User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
        this.userId = user.getId();
    }
}
