package net.fullstack7.studyShare.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String userId;
    private final String token;

    public JwtAuthenticationToken(String token, String userId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.userId = userId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    public String getUserId() {
        return userId;
    }
} 