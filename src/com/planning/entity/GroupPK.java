package com.planning.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GroupPK implements Serializable {
    
    @Basic(optional = false)
    @Column(name = "grupo_id", nullable = false)
    private Integer idGroup;
    
    @Basic(optional = false)
    @Column(name = "task_id", nullable = false)
    private Integer idTask;
    
    @Basic(optional = false)
    @Column(name = "plan_id", nullable = false)
    private Integer idPlan;
    
    public GroupPK() {
    }
    
    public GroupPK(Integer idGroup, Integer idTask, Integer idPlan) {
        this.idGroup = idGroup;
        this.idTask = idTask;
        this.idPlan = idPlan;
    }
    
    public Integer getIdGroup() {
        return idGroup;
    }
    
    public void setIdGroup(Integer idGroup) {
        this.idGroup = idGroup;
    }
    
    public Integer getIdTask() {
        return idTask;
    }
    
    public void setIdTask(Integer idTask) {
        this.idTask = idTask;
    }
    
    public Integer getIdPlan() {
        return idPlan;
    }
    
    public void setIdPlan(Integer idPlan) {
        this.idPlan = idPlan;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GroupPK)) {
            return false;
        }
        GroupPK gpk = (GroupPK) obj;
        return Objects.equals(gpk.idGroup, idGroup) && Objects.equals(gpk.idTask, idTask) && Objects.equals(gpk.idPlan, idPlan);
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.idGroup);
        hash = 67 * hash + Objects.hashCode(this.idTask);
        return hash;
    }
    
}
