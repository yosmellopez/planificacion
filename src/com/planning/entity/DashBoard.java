package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.planning.api.entity.EstadoTarea;

import java.util.TreeSet;

public class DashBoard {
    
    private TreeSet<EstadoTarea> estadoTareas = new TreeSet<>();
    
    private long totalTareas = 0;
    
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private long totalNotificaciones = 5;
    
    private boolean success = false;
    
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
}
