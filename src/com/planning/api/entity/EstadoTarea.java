package com.planning.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.planning.entity.StatusTask;

public class EstadoTarea implements Comparable<EstadoTarea> {
    
    private Integer idEstado;
    
    private String name;
    
    @JsonIgnore
    private String description;
    
    @JsonIgnore
    private String color;
    
    private Integer order;
    
    private Integer totalTareas = 0;
    
    public EstadoTarea(StatusTask statusTask) {
        idEstado = statusTask.getId();
        name = statusTask.getName();
        description = statusTask.getDescription();
        color = statusTask.getColor();
        order = statusTask.getOrder();
    }
    
    public Integer getIdEstado() {
        return idEstado;
    }
    
    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
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
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public Integer getOrder() {
        return order;
    }
    
    public void setOrder(Integer order) {
        this.order = order;
    }
    
    public Integer getTotalTareas() {
        return totalTareas;
    }
    
    public void setTotalTareas(Integer totalTareas) {
        this.totalTareas = totalTareas;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstado != null ? idEstado.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StatusTask)) {
            return false;
        }
        EstadoTarea other = (EstadoTarea) object;
        return !((this.idEstado == null && other.idEstado != null) || (this.idEstado != null && !this.idEstado.equals(other.idEstado)));
    }
    
    @Override
    public String toString() {
        return "EstadoTarea[ id=" + idEstado + " ]";
    }
    
    @Override
    public int compareTo(EstadoTarea o) {
        return order - o.order;
    }
}
