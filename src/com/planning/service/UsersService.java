/*Generado por Disrupsoft*/
package com.planning.service;

import com.planning.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersService extends JpaRepository<Users, Integer> {

    public Users findByUsuarioOrEmail(String username, String email);
}
