/*Generado por Disrupsoft*/
package com.planning.service;

import com.planning.entity.Position;
import com.planning.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsersService extends JpaRepository<Users, Integer> {
    
    public Users findByUsuarioOrEmail(String username, String email);
    
    public List<Users> findByPosition(Position position);
    
    public List<Users> findByTitular(boolean titular);
    
}
