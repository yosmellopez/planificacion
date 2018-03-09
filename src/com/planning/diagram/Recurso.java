/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.diagram;

import java.util.Date;

public class Recurso {
    
    private Integer id;
    
    private String name;
    
    private String image;
    
    private Boolean common;
    
    private String nombre;
    
    private Date fecha;
    
    private String usuario;
    
    private String fechaActual;
    
    private Integer idEstado;
    
    public Recurso(Integer id, String name, String image, Boolean common, String nombre, Date fecha, String usuario, String fechaActual, Integer idEstado) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.common = common;
        this.nombre = nombre;
        this.fecha = fecha;
        this.usuario = usuario;
        this.fechaActual = fechaActual;
        this.idEstado = idEstado;
        if (idEstado == 1 || idEstado == 3) {
            this.usuario = "No se ha asignado o ejecutado la tarea";
            this.fechaActual = "Sin fecha de asignación";
        }
        if (this.usuario == null || this.usuario.isEmpty()) {
            this.usuario = "Sin usuario";
        }
        if (this.fechaActual == null || this.fechaActual.isEmpty()) {
            this.fechaActual = "Sin fecha de asignación";
        }
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
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public Boolean getCommon() {
        return common;
    }
    
    public void setCommon(Boolean common) {
        this.common = common;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public String getFechaActual() {
        return fechaActual;
    }
    
    public void setFechaActual(String fechaActual) {
        this.fechaActual = fechaActual;
    }
    
    public Integer getIdEstado() {
        return idEstado;
    }
    
    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }
}
