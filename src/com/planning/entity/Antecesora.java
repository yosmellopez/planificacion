package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Antecesora {
    
    @JsonProperty(value = "tarea_id")
    private Integer id;
    
    @JsonProperty(value = "nombre")
    private String name;
    
    @JsonProperty(value = "codigo")
    private String code;
    
    @JsonIgnore
    private Set<CriticalyLevel> criticalyLevels = new HashSet<>();
    
    @JsonIgnore
    private Position position;
    
    private String cargo = "";
    
    private String criticidad = "";
    
    public Antecesora(PlTask plTask) {
        id = plTask.getTask().getId();
        name = plTask.getTask().getName();
        code = plTask.getTask().getCode();
        position = plTask.getTask().getPosition();
        criticalyLevels = plTask.getTask().getCriticalyLevels();
    }
    
    public Antecesora(Task task) {
        id = task.getId();
        name = task.getName();
        code = task.getCode();
        position = task.getPosition();
        criticalyLevels = task.getCriticalyLevels();
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public Set<CriticalyLevel> getCriticalyLevels() {
        return criticalyLevels;
    }
    
    public void setCriticalyLevels(Set<CriticalyLevel> criticalyLevels) {
        this.criticalyLevels = criticalyLevels;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public String getCargo() {
        cargo = position.getName();
        return cargo;
    }
    
    public void setCargo(String cargo) {
        cargo = position.getName();
        this.cargo = cargo;
    }
    
    public String getCriticidad() {
        int i = 0;
        criticidad = "";
        for (CriticalyLevel criticalyLevel : criticalyLevels) {
            criticidad += (i == 0 ? "" : ", ") + criticalyLevel.getName();
            i++;
        }
        return criticidad;
    }
    
    public void setCriticidad(String criticidad) {
        int i = 0;
        for (CriticalyLevel criticalyLevel : criticalyLevels) {
            criticidad += (i == 0 ? "" : ", ") + criticalyLevel.getName();
            i++;
        }
        this.criticidad = criticidad;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Antecesora)) return false;
        Antecesora that = (Antecesora) o;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
