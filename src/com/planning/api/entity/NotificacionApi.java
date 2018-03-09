package com.planning.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.planning.entity.Notificacion;
import com.planning.entity.Position;
import com.planning.util.SerializadorFechaTiempo;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificacionApi {
    
    @JsonProperty(value = "notificacionId")
    private Long id;
    
    @JsonProperty(value = "titulo")
    private String title;
    
    @JsonProperty(value = "descripcion")
    private String description;
    
    @JsonProperty(value = "cargo")
    private Position position;
    
    private boolean leida;
    
    @JsonSerialize(using = SerializadorFechaTiempo.class)
    private Date fecha;
    
    public NotificacionApi(Notificacion notificacion, boolean leida) {
        this.id = notificacion.getId();
        this.title = notificacion.getTitle();
        this.description = notificacion.getDescription();
        this.position = notificacion.getPosition();
        this.fecha = notificacion.getFecha();
        this.leida = leida;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public boolean isLeida() {
        return leida;
    }
    
    public void setLeida(boolean leida) {
        this.leida = leida;
    }
}
