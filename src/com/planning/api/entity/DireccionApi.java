package com.planning.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planning.entity.Management;

public class DireccionApi {
    
    @JsonProperty(value = "direccionId")
    private Integer id;
    
    @JsonProperty(value = "nombre")
    private String name;
    
    private boolean active;
    
    public DireccionApi(Management management) {
        this.id = management.getId();
        this.name = management.getName();
        this.active = management.isActive();
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
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}
