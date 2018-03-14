package com.planning.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.planning.entity.*;
import com.planning.util.ListChannelsSerializer;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TareaApi {
    
    @JsonProperty(value = "tareaId")
    private Integer id;
    
    @JsonProperty(value = "nombre")
    private String name;
    
    @JsonProperty(value = "codigo")
    private String code;
    
    @JsonProperty(value = "descripcion")
    private String descripcion;
    
    @JsonProperty(value = "producto")
    private String product;
    
    @JsonProperty(value = "partida")
    private boolean start;
    
    private boolean hito = false;
    
    @JsonProperty(value = "recurrente")
    private boolean isrecurrent;
    
    private Integer tiempoRecurrencia;
    
    private boolean tranversal;
    
    @JsonProperty(value = "documentos")
    private Set<Document> documents = new HashSet<>();
    
    @JsonProperty(value = "estadoTarea")
    private StatusTask statusTask;
    
    private Date fechaCreacion = new Date();
    
    @JsonProperty(value = "nivelesAlerta")
    private Set<CriticalyLevel> criticalyLevels = new HashSet<>();
    
    @JsonProperty(value = "canales")
    @JsonSerialize(using = ListChannelsSerializer.class)
    private Set<Channel> channels = new HashSet<>();
    
    @JsonProperty(value = "cargo")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Position position;
    
    public TareaApi(PlTask task) {
        this.id = task.getId();
        this.channels = task.getChannels();
        this.name = task.getName();
        this.documents = task.getDocuments();
        this.position = task.getPosition();
        this.product = task.getProduct();
        this.statusTask = task.getStatusTask();
        this.fechaCreacion = task.getFechaCreacion();
        this.isrecurrent = task.isIsrecurrent();
        this.tiempoRecurrencia = task.getTiempoRecurrencia();
        this.descripcion = task.getDescription();
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
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getProduct() {
        return product;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    
    public boolean isStart() {
        return start;
    }
    
    public void setStart(boolean start) {
        this.start = start;
    }
    
    public boolean isHito() {
        return hito;
    }
    
    public void setHito(boolean hito) {
        this.hito = hito;
    }
    
    public boolean isIsrecurrent() {
        return isrecurrent;
    }
    
    public void setIsrecurrent(boolean isrecurrent) {
        this.isrecurrent = isrecurrent;
    }
    
    public Integer getTiempoRecurrencia() {
        return tiempoRecurrencia;
    }
    
    public void setTiempoRecurrencia(Integer tiempoRecurrencia) {
        this.tiempoRecurrencia = tiempoRecurrencia;
    }
    
    public boolean isTranversal() {
        return tranversal;
    }
    
    public void setTranversal(boolean tranversal) {
        this.tranversal = tranversal;
    }
    
    public Set<Document> getDocuments() {
        return documents;
    }
    
    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
    
    public StatusTask getStatusTask() {
        return statusTask;
    }
    
    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }
    
    public Set<CriticalyLevel> getCriticalyLevels() {
        return criticalyLevels;
    }
    
    public void setCriticalyLevels(Set<CriticalyLevel> criticalyLevels) {
        this.criticalyLevels = criticalyLevels;
    }
    
    public Set<Channel> getChannels() {
        return channels;
    }
    
    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
