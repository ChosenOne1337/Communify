package ru.nsu.ccfit.mvcentertainment.communify.backend.security;

import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
public class CustomUser extends User {

    private final Long userId;

    public CustomUser(
            Long userId,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
