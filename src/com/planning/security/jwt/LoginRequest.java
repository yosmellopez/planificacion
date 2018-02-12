package com.planning.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 512;

    @NotNull
    @Size(min = 1, max = 50)
    @JsonProperty(value = "email")
    private String username;

    @NotNull
    @Size(min = LoginRequest.PASSWORD_MIN_LENGTH, max = LoginRequest.PASSWORD_MAX_LENGTH)
    private String password;

    @JsonProperty(value = "plataform")
    private String plataform = "Android";

    @JsonProperty(value = "token")
    private String token = "";

    @JsonProperty(value = "playerId")
    private String playerId = "1234";

    @JsonProperty(value = "remember_me")
    private Boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlataform() {
        return plataform;
    }

    public void setPlataform(String plataform) {
        this.plataform = plataform;
    }

    public Boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "LoginRequest{" + "username='" + username + '\'' + ", rememberMe=" + rememberMe + '}';
    }
}
