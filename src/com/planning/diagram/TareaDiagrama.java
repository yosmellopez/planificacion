package com.planning.diagram;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planning.entity.PlTask;

import java.util.Date;

public class TareaDiagrama {
    
    @JsonProperty(value = "tarea_id")
    private Integer id;
    
    private Integer idGerencia;
    
    private Integer idCriticidad;
    
    @JsonProperty(value = "nombre")
    private String name;
    
    @JsonProperty(value = "codigo")
    private String code;
    
    @JsonProperty(value = "descripcion")
    private String description = "";
    
    private String gerencia = "";
    
    private String criticidad = "";
    
    private String producto;
    
    private String cargo;
    
    private boolean hito;
    
    private Boolean partida;
    
    private Boolean recurrente;
    
    private boolean grupo = false;
    
    private Date fechaCreacion = new Date();
    
    private String color = "#333333";
    
    public TareaDiagrama(PlTask task, String criticidad, Integer idCriticidad, String color, boolean grupo) {
        id = task.getTask().getId();
        name = task.getTask().getName();
        code = task.getTask().getCode();
        description = task.getTask().getDescription();
        producto = task.getTask().getProduct();
        cargo = task.getTask().getPosition().getName();
        partida = task.isStart();
        recurrente = task.isIsrecurrent();
        fechaCreacion = task.getTask().getFechaCreacion();
        gerencia = task.getTask().getPosition().getArea().getManagement().getName();
        idGerencia = task.getTask().getPosition().getArea().getManagement().getId();
        hito = task.getTask().isHito();
        this.idCriticidad = idCriticidad;
        this.criticidad = criticidad;
        this.color = color;
        this.grupo = grupo;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getProducto() {
        return producto;
    }
    
    public void setProducto(String product) {
        this.producto = product;
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
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getCargo() {
        return cargo;
    }
    
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    public Boolean getPartida() {
        return partida;
    }
    
    public void setPartida(Boolean partida) {
        this.partida = partida;
    }
    
    public Boolean getRecurrente() {
        return recurrente;
    }
    
    public void setRecurrente(Boolean recurrente) {
        this.recurrente = recurrente;
    }
    
    public String getGerencia() {
        return gerencia;
    }
    
    public void setGerencia(String gerencia) {
        this.gerencia = gerencia;
    }
    
    public String getCriticidad() {
        return criticidad;
    }
    
    public void setCriticidad(String criticidad) {
        this.criticidad = criticidad;
    }
    
    public Integer getIdGerencia() {
        return idGerencia;
    }
    
    public void setIdGerencia(Integer idGerencia) {
        this.idGerencia = idGerencia;
    }
    
    public Integer getIdCriticidad() {
        return idCriticidad;
    }
    
    public void setIdCriticidad(Integer idCriticidad) {
        this.idCriticidad = idCriticidad;
    }
    
    public boolean isGrupo() {
        return grupo;
    }
    
    public void setGrupo(boolean grupo) {
        this.grupo = grupo;
    }
    
    public boolean isHito() {
        return hito;
    }
    
    public void setHito(boolean hito) {
        this.hito = hito;
    }
}
