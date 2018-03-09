package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.planning.api.entity.EstadoTarea;
import com.planning.util.PlanSerializer;

import java.util.TreeSet;

public class DashBoard {
    
    private TreeSet<EstadoTarea> estadoTareas = new TreeSet<>();
    
    private long totalTareas = 0;
    
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private long totalNotificaciones = 5;
    
    private boolean success = false;
    
    @JsonSerialize(using = PlanSerializer.class)
    private Plan plan;
    
    public DashBoard() {
    }
    
    public TreeSet<EstadoTarea> getEstadoTareas() {
        return estadoTareas;
    }
    
    public void setEstadoTareas(TreeSet<EstadoTarea> estadoTareas) {
        this.estadoTareas = estadoTareas;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public long getTotalTareas() {
        return totalTareas;
    }
    
    public void setTotalTareas(long totalTareas) {
        this.totalTareas = totalTareas;
    }
    
    public long getTotalNotificaciones() {
        return totalNotificaciones;
    }
    
    public void setTotalNotificaciones(long totalNotificaciones) {
        this.totalNotificaciones = totalNotificaciones;
    }
    
    public Plan getPlan() {
        return plan;
    }
    
    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}
