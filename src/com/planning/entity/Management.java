/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Nodo
 */
@Entity
@Table(name = "management", uniqueConstraints = @UniqueConstraint(name = "codigo_unico", columnNames = {"orden"}))
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Management implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "management_id_seq")
    @SequenceGenerator(name = "management_id_seq", sequenceName = "management_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id", nullable = false)
    @JsonProperty(value = "gerencia_id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 128)
    @JsonProperty(value = "descripcion")
    private String name;

    @Basic(optional = false)
    @Column(name = "active", nullable = false)
    @ColumnDefault(value = "1")
    private boolean active;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "management", fetch = FetchType.LAZY)
    private Set<Area> areas;

    @Column(name = "orden")
    @JsonProperty(value = "peso")
    @ColumnDefault(value = "1")
    private Integer order;

    public Management() {
    }

    public Management(Integer id) {
        this.id = id;
    }

    public Management(String id) {
        this.id = Integer.parseInt(id);
    }

    public Management(Integer id, String name, boolean active) {
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

    public void setActive(boolean active) {
        this.active = active;
    }

    @XmlTransient
    public Set<Area> getAreas() {
        return areas;
    }

    public void setAreas(Set<Area> areas) {
        this.areas = areas;
    }

    public boolean isActive() {
        return active;
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
        if (!(object instanceof Management)) {
            return false;
        }
        Management other = (Management) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Direccion[ id=" + id + ", nombre: " + name + " ]";
    }

}
