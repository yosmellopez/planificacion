package com.planning.entity;

public class PlanActivado {
    
    private Integer planId;
    
    private Integer estadoId;
    
    private boolean ejecucion;
    
    public PlanActivado() {
    
    }
    
    public Integer getPlanId() {
        return planId;
    }
    
    public void setPlanId(Integer planId) {
        this.planId = planId;
    }
    
    public Integer getEstadoId() {
        return estadoId;
    }
    
    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
    }
    
    public boolean isEjecucion() {
        return ejecucion;
    }
    
    public void setEjecucion(boolean ejecucion) {
        this.ejecucion = ejecucion;
    }
}
