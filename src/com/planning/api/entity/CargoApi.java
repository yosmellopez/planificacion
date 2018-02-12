package com.planning.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planning.entity.Area;
import com.planning.entity.Position;

public class CargoApi {
    
    @JsonProperty(value = "cargoId")
    private Integer id;
    
    @JsonProperty(value = "nombre")
    private String name;
    
    @JsonProperty(value = "unidad")
    private Area area;
    
    public CargoApi(Position position) {
        this.id = position.getId();
        this.name = position.getName();
        this.area = position.getArea();
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
    
    public Area getArea() {
        return area;
    }
    
    public void setArea(Area area) {
        this.area = area;
    }
}
