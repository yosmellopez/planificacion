package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.planning.util.TaskSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "grupo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Grupo implements Serializable {

    @EmbeddedId
    private GroupPK groupPK;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "grupo_id", foreignKey = @ForeignKey(name = "fk_grupo_tarea"), nullable = false, insertable = false, updatable = false)
    private PlTask taskGrupo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id", foreignKey = @ForeignKey(name = "fk_agrupada_tarea"), nullable = false, insertable = false, updatable = false)
    private PlTask taskAgrupada;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_id", foreignKey = @ForeignKey(name = "fk_plan_tarea_agrupada"), nullable = false, insertable = false, updatable = false)
    private Plan plan;

    public Grupo() {
    }

    public Grupo(Integer idGrupo, Integer idTask, Integer idPlan) {
        this.groupPK = new GroupPK(idGrupo, idTask, idPlan);
    }

    public GroupPK getGroupPK() {
        return groupPK;
    }

    public void setGroupPK(GroupPK groupPK) {
        this.groupPK = groupPK;
    }

    public PlTask getTaskGrupo() {
        return taskGrupo;
    }

    public void setTaskGrupo(PlTask taskGrupo) {
        this.taskGrupo = taskGrupo;
    }

    public PlTask getTaskAgrupada() {
        return taskAgrupada;
    }

    public void setTaskAgrupada(PlTask taskAgrupada) {
        this.taskAgrupada = taskAgrupada;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
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
        final Grupo other = (Grupo) obj;
        return Objects.equals(this.groupPK, other.groupPK);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.groupPK);
        return hash;
    }

}
