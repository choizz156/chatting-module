package me.choizz.apimodule.auth.details;

import java.util.Collection;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import me.choizz.apimodule.auth.dto.UserDetailsDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@EqualsAndHashCode
public class UserDetail implements UserDetails {

    private final UserDetailsDto dto;

    public UserDetail(UserDetailsDto dto) {
        this.dto = dto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(dto.role());
    }

    @Override
    public String getPassword() {
        return dto.password();
    }

    @Override
    public String getUsername() {
        return dto.email();
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
