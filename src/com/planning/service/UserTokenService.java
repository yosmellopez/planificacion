package com.planning.service;

import com.planning.entity.Position;
import com.planning.entity.UserToken;
import com.planning.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTokenService extends JpaRepository<UserToken, Integer> {
    
    public Optional<UserToken> findByPlayerId(String playerId);
    
    public Optional<UserToken> findByPlataformAndPlayerId(String plataform, String playerId);
    
    public Optional<UserToken> findByPlataformAndPlayerIdAndUser(String plataform, String playerId, Users user);
    
    public List<UserToken> findByUserPosition(Position position);
}
