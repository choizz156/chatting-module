package me.choizz.chattingserver.auth;

import java.util.Collection;
import me.choizz.chattingserver.auth.dto.UserAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsEntity implements UserDetails {

    private final UserAttribute userAttribute;

    public UserDetailsEntity(UserAttribute userAttribute) {
        this.userAttribute = userAttribute;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(userAttribute.userRole().name());
    }

    @Override
    public String getPassword() {
        return userAttribute.password();
    }

    @Override
    public String getUsername() {
        return userAttribute.email();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
