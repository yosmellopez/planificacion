package com.planning.security.jwt;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSettings {
    
    public static final String AUTHORIZATION_HEADER = "Authorization";
    
    public static final String AUTHORIZATION_TOKEN = "access_token";
    
    @Value("${planning.security.jwt.tokenExpirationTime}")
    private Integer tokenExpirationTime;
    
    @Value("${planning.security.jwt.tokenIssuer}")
    private String tokenIssuer;
    
    @Value("${planning.security.jwt.tokenSigningKey}")
    private String tokenSigningKey;
    
    @Value("${planning.security.jwt.refreshTokenExpTime}")
    private Integer refreshTokenExpTime;
    
    public Integer getRefreshTokenExpTime() {
        return refreshTokenExpTime;
    }
    
    public void setRefreshTokenExpTime(Integer refreshTokenExpTime) {
        this.refreshTokenExpTime = refreshTokenExpTime;
    }
    
    public Integer getTokenExpirationTime() {
        return tokenExpirationTime;
    }
    
    public void setTokenExpirationTime(Integer tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }
    
    public String getTokenIssuer() {
        return tokenIssuer;
    }
    
    public void setTokenIssuer(String tokenIssuer) {
        this.tokenIssuer = tokenIssuer;
    }
    
    public String getTokenSigningKey() {
        return tokenSigningKey;
    }
    
    public void setTokenSigningKey(String tokenSigningKey) {
        this.tokenSigningKey = tokenSigningKey;
    }
}
