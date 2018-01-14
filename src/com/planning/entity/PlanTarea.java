package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlanTarea {
    
    @JsonProperty(value = "tarea_id")
    private Integer tareaId;
    
    @JsonProperty(value = "plan_id")
    private Integer planId;
        
    public PlanTarea() {
    }
    
    public Integer getTareaId() {
        return tareaId;
    }
    
    public void setTareaId(Integer tareaId) {
        this.tareaId = tareaId;
    }
    
    public Integer getPlanId() {
        return planId;
    }
    
    public void setPlanId(Integer planId) {
        this.planId = planId;
    }
}
