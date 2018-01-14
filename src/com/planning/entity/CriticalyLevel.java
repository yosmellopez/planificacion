/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;

/**
 * @author Nodo
 */
@Entity
@Table(name = "criticaly_level")
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CriticalyLevel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "criticaly_level_id_seq")
    @SequenceGenerator(name = "criticaly_level_id_seq", sequenceName = "criticaly_level_id_seq", allocationSize = 1, initialValue = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @JsonProperty(value = "criticidad_id")
    private Integer id;
    
    @Basic(optional = false)
    @JsonProperty(value = "nombre")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "description", length = 150)
    @JsonProperty(value = "descripcion")
    private String description;
    
    @Column(name = "color", length = 150)
    @JsonProperty(value = "color")
    private String color;
    
    @Basic(optional = false)
    @JsonProperty(value = "estado")
    @Column(name = "active", nullable = false)
    @ColumnDefault(value = "true")
    private boolean active;
    
    @Column(name = "orden")
    @JsonProperty(value = "peso")
    @ColumnDefault(value = "1")
    private Integer order;
    
    public CriticalyLevel() {
    }
    
    public CriticalyLevel(CriticalyLevel level) {
        this.id = level.id;
        this.name = level.name;
        this.description = level.description;
        this.color = level.color;
        this.active = level.active;
        this.order = level.order;
    }
    
    public CriticalyLevel(Integer id) {
        this.id = id;
    }
    
    public CriticalyLevel(String id) {
        this.id = Integer.parseInt(id);
    }
    
    public CriticalyLevel(Integer id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean getActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public Integer getOrder() {
        return order;
    }
    
    public void setOrder(Integer order) {
        this.order = order;
    }
}
