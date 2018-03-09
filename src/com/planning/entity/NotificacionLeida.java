package com.planning.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.planning.util.TaskSerializer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "read_notification")
public class NotificacionLeida implements Serializable {
    
    @EmbeddedId
    private ReadNotificationPK notificationPK;
    
    @ManyToOne(optional = false)
    @JsonSerialize(using = TaskSerializer.class)
    @JoinColumn(name = "notification_id", foreignKey = @ForeignKey(name = "fk_notificacion_user"), nullable = false, insertable = false, updatable = false)
    private Notificacion notificacion;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_notificacion"), nullable = false, insertable = false, updatable = false)
    private Users user;
    
    @Column(name = "leido")
    private Boolean leido;
    
    public NotificacionLeida() {
    }
    
    public boolean isLeido() {
        return leido;
    }
    
    public void setLeido(boolean read) {
        this.leido = read;
    }
    
    public ReadNotificationPK getNotificationPK() {
        return notificationPK;
    }
    
    public void setNotificationPK(ReadNotificationPK notificationPK) {
        this.notificationPK = notificationPK;
    }
    
    public Notificacion getNotificacion() {
        return notificacion;
    }
    
    public void setNotificacion(Notificacion notificacion) {
        this.notificacion = notificacion;
    }
    
    public Users getUser() {
        return user;
    }
    
    public void setUser(Users user) {
        this.user = user;
    }
    
    public void setLeido(Boolean leido) {
        this.leido = leido;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificacionLeida)) {
            return false;
        }
        NotificacionLeida that = (NotificacionLeida) o;
        return notificationPK.equals(that.notificationPK);
    }
    
    @Override
    public int hashCode() {
        return notificationPK.hashCode();
    }
}
