package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tarea {
    
    @JsonProperty(value = "tarea_id")
    private Integer id;
    
    @JsonProperty(value = "nombre")
    private String name;
    
    @JsonProperty(value = "codigo")
    private String code;
    
    @JsonProperty(value = "descripcion")
    private String description = "";
    
    @JsonProperty(value = "producto")
    private String product;
    
    private Set<Document> documents = new HashSet<>();
    
    @JsonProperty(value = "estado")
    private StatusTask statusTask;
    
    @JsonProperty(value = "criticidad_id")
    private Set<CriticalyLevel> criticalyLevels = new HashSet<>();
    
    @JsonProperty(value = "cargo_id")
    private Position position;
    
    @JsonProperty(value = "canales_id")
    private ArrayList<Channel> channels;
    
    private String criticidad = "";
    
    public Tarea() {
    
    }
    
    public Tarea(Integer id) {
        this.id = id;
    }
    
    public Tarea(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
    
    public Tarea(PlTask from) {
        id = from.getId();
        name = from.getName();
        code = from.getCode();
        description = from.getDescription();
        product = from.getProduct();
        documents = from.getDocuments();
        statusTask = from.getStatusTask();
        criticalyLevels = from.getCriticalyLevels();
        position = from.getPosition();
        channels = new ArrayList<>(from.getChannels());
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getProduct() {
        return product;
    }
    
    public void setProduct(String product) {
        this.product = product;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public ArrayList<Channel> getChannels() {
        return channels;
    }
    
    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }
    
    public String getCargo() {
        return position.getName();
    }
    
    public void setCargo(String cargo) {
    }
    
    public String getCriticidad() {
        int i = 0;
        for (CriticalyLevel criticalyLevel : criticalyLevels) {
            criticidad += (i == 0 ? "" : ", ") + criticalyLevel.getName();
            i++;
        }
        return criticidad;
    }
    
    public void setCriticidad(String criticidad) {
        int i = 0;
        for (CriticalyLevel criticalyLevel : criticalyLevels) {
            criticidad += (i == 0 ? "" : ", ") + criticalyLevel.getName();
            i++;
        }
        this.criticidad = criticidad;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tarea)) return false;
        Tarea tarea = (Tarea) o;
        return id.equals(tarea.id);
    }
    
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
    
    @Override
    public String toString() {
        return "[idTarea: " + id + ", nombre: " + name + "]";
    }
}
