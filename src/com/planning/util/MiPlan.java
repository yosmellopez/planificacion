package com.planning.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planning.entity.CriticalyLevel;
import com.planning.entity.Plan;
import com.planning.entity.StatusPlan;

import java.util.Date;

public class MiPlan {
    
    @JsonProperty(value = "plan_id")
    private Integer id;
    
    @JsonProperty(value = "nombre")
    private String name;
    
    @JsonProperty(value = "descripcion")
    private String description;
    
    private Date fechaCreacion = new Date();
    
    @JsonProperty(value = "estado")
    private StatusPlan estadoPlan;
    
    @JsonProperty(value = "nivelAlerta")
    private CriticalyLevel criticalyLevel;
    
    private boolean ejecucion = false;
    
    private Date fechaActivacion = new Date();
    
    public MiPlan(Plan plan) {
        id = plan.getId();
        name = plan.getName();
        description = plan.getDescription();
        fechaCreacion = plan.getFechaCreacion();
        estadoPlan = plan.getStatusplanid();
        criticalyLevel = plan.getCriticalyLevel();
        ejecucion = plan.isEjecucion();
        fechaActivacion = plan.getFechaActivacion();
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public StatusPlan getEstadoPlan() {
        return estadoPlan;
    }
    
    public void setEstadoPlan(StatusPlan estadoPlan) {
        this.estadoPlan = estadoPlan;
    }
    
    public CriticalyLevel getCriticalyLevel() {
        return criticalyLevel;
    }
    
    public void setCriticalyLevel(CriticalyLevel criticalyLevel) {
        this.criticalyLevel = criticalyLevel;
    }
    
    public boolean isEjecucion() {
        return ejecucion;
    }
    
    public void setEjecucion(boolean ejecucion) {
        this.ejecucion = ejecucion;
    }
    
    public Date getFechaActivacion() {
        return fechaActivacion;
    }
    
    public void setFechaActivacion(Date fechaActivacion) {
        this.fechaActivacion = fechaActivacion;
    }
}
