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
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;

/**
 *
 * @author Nodo
 */
@Entity
@Table(name = "status_task")
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StatusTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_task_id_seq")
    @SequenceGenerator(name = "status_task_id_seq", sequenceName = "status_task_id_seq", allocationSize = 1, initialValue = 1)
    @Basic(optional = false)
    @JsonProperty(value = "estado_id")
    @Column(name = "id", nullable = false)
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

    @Column(name = "active")
    @JsonProperty(value = "estado")
    @ColumnDefault(value = "true")
    private boolean active;

    @Column(name = "orden")
    @JsonProperty(value = "peso")
    @ColumnDefault(value = "1")
    private Integer order;

    public StatusTask() {
        this.order = 0;
    }

    public StatusTask(Integer id) {
        this.order = 0;
        this.id = id;
    }

    public StatusTask(String id) {
        this.order = 0;
        this.id = Integer.parseInt(id);
    }

    public StatusTask(Integer id, String name) {
        this.order = 0;
        this.id = id;
        this.name = name;
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

    public boolean isActive() {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StatusTask)) {
            return false;
        }
        StatusTask other = (StatusTask) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.entity.entity.StatusTask[ id=" + id + " ]";
    }

}
