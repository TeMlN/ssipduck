package com.server.ssipduck.domain.user.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_PERSONAL;

    @Override
    public String getAuthority() {
        return name();
    }
}
