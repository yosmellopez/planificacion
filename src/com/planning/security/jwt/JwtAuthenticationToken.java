package com.planning.security.jwt;

import com.planning.entity.Users;
import com.planning.security.jwt.token.RawAccessJwtToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * An {@link org.springframework.security.core.Authentication} implementation
 * that is designed for simple presentation of JwtToken.
 *
 * @author vladimir.stankovic
 * <p>
 * May 23, 2016
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    
    private static final long serialVersionUID = 2877954820905567501L;
    
    private RawAccessJwtToken rawAccessToken;
    
    private Users users;
    
    public JwtAuthenticationToken(RawAccessJwtToken unsafeToken) {
        super(null);
        this.rawAccessToken = unsafeToken;
        this.setAuthenticated(false);
    }
    
    public JwtAuthenticationToken(Users users, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.users = users;
        super.setAuthenticated(true);
    }
    
    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }
    
    @Override
    public Object getCredentials() {
        return rawAccessToken;
    }
    
    @Override
    public Object getPrincipal() {
        return this.users;
    }
    
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.rawAccessToken = null;
    }
}
