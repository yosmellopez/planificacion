/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_token")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserToken implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_id_seq")
    @SequenceGenerator(name = "token_id_seq", sequenceName = "token_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id", nullable = false)
    private Integer idToken;
    
    @Column(name = "token", length = 512)
    private String token = "123";
    
    @Column(name = "plataform", nullable = false, length = 512)
    private String plataform = "Android";
    
    @Column(name = "player_id", length = 512)
    private String playerId = "";
    
    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_token_user"))
    private Users user;
    
    public UserToken(String token, String plataform, String playerId) {
        this.token = token;
        this.plataform = plataform;
        this.playerId = playerId;
    }
    
    public UserToken() {
    }
    
    public UserToken(String plataform, String playerId, Users user) {
        this.plataform = plataform;
        this.playerId = playerId;
        this.user = user;
    }
    
    public Integer getIdToken() {
        return idToken;
    }
    
    public void setIdToken(Integer idToken) {
        this.idToken = idToken;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getPlataform() {
        return plataform;
    }
    
    public void setPlataform(String plataform) {
        this.plataform = plataform;
    }
    
    public Users getUser() {
        return user;
    }
    
    public void setUser(Users users) {
        this.user = users;
    }
    
    public String getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.idToken);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserToken other = (UserToken) obj;
        return Objects.equals(this.idToken, other.idToken);
    }
    
}
