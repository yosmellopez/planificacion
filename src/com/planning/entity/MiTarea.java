/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Nodo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MiTarea {

    @JsonProperty(value = "tarea_id")
    private Integer tareaId;

    @JsonProperty(value = "cargo_id")
    private Integer cargoId;

    private Collection<CriticalyLevel> criticidad_id;

    @JsonProperty(value = "estado_id")
    private Integer estadoId;

    private int peso;

    @JsonProperty(value = "gerencia_id")
    private Integer gerenciaId;

    private String nombre;

    private String descripcion;

    private String cargo;

    private String criticidades = "";

    private String codigo;

    private String estado;

    private String producto;

    private String criticidad;

    private boolean recurrente;

    private boolean partida;

    private ArrayList<Channel> canales;

    private String color;

    private boolean esMia;

    private Date fechaCreacion = new Date();

    private ArrayList<Document> modelos = new ArrayList<>();

    private ArrayList<Tarea> sucesoras = new ArrayList<>();

    public MiTarea() {
    }

    public MiTarea(PlTask plTask) {
        tareaId = plTask.getTask().getId();
        cargoId = plTask.getTask().getPosition().getId();
        criticidad_id = plTask.getTask().getCriticalyLevels();
        estadoId = plTask.getTask().getStatusTask().getId();
        peso = plTask.getPosition();
        gerenciaId = plTask.getTask().getPosition().getArea().getManagement().getId();
        nombre = plTask.getName();
        descripcion = plTask.getTask().getDescription();
        cargo = plTask.getTask().getCargo();
        codigo = plTask.getTask().getCode();
        estado = plTask.getTask().getStatusTask().getName();
        producto = plTask.getTask().getProduct();
        criticidad = plTask.getTask().getCriticidad();
        recurrente = plTask.getTask().isIsrecurrent();
        partida = plTask.getTask().isStart();
        canales = new ArrayList<>(plTask.getTask().getChannels());
        color = "";
        modelos = new ArrayList<>(plTask.getTask().getDocuments());
    }

    public Integer getTareaId() {
        return tareaId;
    }

    public void setTareaId(Integer tareaId) {
        this.tareaId = tareaId;
    }

    public Integer getCargoId() {
        return cargoId;
    }

    public void setCargoId(Integer cargoId) {
        this.cargoId = cargoId;
    }

    public Collection<CriticalyLevel> getCriticidad_id() {
        return criticidad_id;
    }

    public void setCriticidad_id(Collection<CriticalyLevel> criticidad_id) {
        this.criticidad_id = criticidad_id;
    }

    public Integer getGerenciaId() {
        return gerenciaId;
    }

    public void setGerenciaId(Integer gerenciaId) {
        this.gerenciaId = gerenciaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ArrayList<Channel> getCanales() {
        return canales;
    }

    public void setCanales(ArrayList<Channel> canales) {
        this.canales = canales;
    }

    public boolean isRecurrente() {
        return recurrente;
    }

    public void setRecurrente(boolean recurrente) {
        this.recurrente = recurrente;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isEsMia() {
        return esMia;
    }

    public void setEsMia(boolean esMia) {
        this.esMia = esMia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public boolean isPartida() {
        return partida;
    }

    public void setPartida(boolean partida) {
        this.partida = partida;
    }

    public Integer getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCriticidades() {
        int i = 0;
        for (CriticalyLevel criticalyLevel : criticidad_id) {
            criticidades += (i++ != 0 ? ", " : "") + criticalyLevel.getName();
        }
        return criticidades;
    }

    public void setCriticidades(String criticidades) {
        this.criticidades = criticidades;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getCriticidad() {
        criticidad = criticidades;
        return criticidad;
    }

    public void setCriticidad(String criticidad) {
        criticidad = criticidades;
        this.criticidad = criticidad;
    }

    public ArrayList<Tarea> getSucesoras() {
        return sucesoras;
    }

    public void setSucesoras(ArrayList<Tarea> sucesoras) {
        this.sucesoras = sucesoras;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public ArrayList<Document> getModelos() {
        return modelos;
    }

    public void setModelos(ArrayList<Document> modelos) {
        this.modelos = modelos;
    }
}
