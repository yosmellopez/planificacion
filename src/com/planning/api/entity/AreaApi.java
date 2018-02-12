package com.planning.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planning.entity.Management;

public class AreaApi {
    
    @JsonProperty(value = "unidadId")
    private Integer id;
    
    @JsonProperty(value = "nombre")
    private String name;
    
    @JsonProperty(value = "descripcion")
    private String description;
    
    private String code = "";
    
    private boolean active;
    
    @JsonProperty(value = "gerencia")
    private Management management;
}
