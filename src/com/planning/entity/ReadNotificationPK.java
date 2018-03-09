package com.planning.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReadNotificationPK implements Serializable {
    
    @Basic(optional = false)
    @Column(name = "notification_id", nullable = false)
    private Long idNotification;
    
    @Basic(optional = false)
    @Column(name = "user_id", nullable = false)
    private Integer idUser;
    
    public ReadNotificationPK() {
    }
    
    public ReadNotificationPK(Long idNotification, Integer idUser) {
        this.idNotification = idNotification;
        this.idUser = idUser;
    }
    
    public Long getIdNotification() {
        return idNotification;
    }
    
    public void setIdNotification(Long idTask) {
        this.idNotification = idTask;
    }
    
    public Integer getIdUser() {
        return idUser;
    }
    
    public void setIdUser(Integer idPlan) {
        this.idUser = idPlan;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ReadNotificationPK)) {
            return false;
        }
        ReadNotificationPK gpk = (ReadNotificationPK) obj;
        return Objects.equals(gpk.idNotification, idNotification) && Objects.equals(gpk.idUser, idUser);
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.idNotification);
        return hash;
    }
    
}
