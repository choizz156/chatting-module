package me.choizz.apimodule.auth;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Getter
@EqualsAndHashCode(callSuper = false)
public class UserDetails extends User {

    private final Long userId;

    public UserDetails(me.choizz.domainjpamodule.user.User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
        this.userId = user.getId();
    }
}
