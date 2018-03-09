/*Generado por Disrupsoft*/
package com.planning.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import com.planning.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service object for domain model class StatusTask.
 *
 * @see StatusTask
 */
public interface NotificacionLeidaService extends JpaRepository<NotificacionLeida, ReadNotificationPK> {
    
    public Optional<NotificacionLeida> findByNotificacionAndUserAndLeido(Notificacion notificacion, Users user, boolean leido);
    
    public Optional<NotificacionLeida> findByNotificacionAndUser(Notificacion notificacion, Users user);
    
    public List<NotificacionLeida> findByNotificacion(Notificacion notificacion);
    
    public List<NotificacionLeida> findByUser_PositionAndLeido(Position position, boolean leido);
    
}