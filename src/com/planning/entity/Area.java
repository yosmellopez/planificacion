/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "area")
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Area implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "area_id_seq")
    @SequenceGenerator(name = "area_id_seq", sequenceName = "area_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id", nullable = false)
    @JsonProperty(value = "area_id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 100)
    @JsonProperty(value = "nombre")
    private String name;
    
    @JsonProperty(value = "descripcion")
    @Column(name = "description", length = 100)
    private String description;
    
    @Basic(optional = false)
    @Column(name = "code", nullable = false, length = 150)
    private String code = "";
    
    @Basic(optional = false)
    @Column(name = "active", nullable = false)
    @ColumnDefault(value = "true")
    private boolean active;
    
    @JoinColumn(name = "idmanagement", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_area_management"))
    @ManyToOne(optional = false)
    @JsonProperty(value = "gerencia_id")
    private Management management;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "area")
    private Set<Position> positionSet;
    
    public Area() {
    }
    
    public Area(Integer id) {
        this.id = id;
    }
    
    public Area(String id) {
        this.id = Integer.parseInt(id);
    }
    
    public Area(Integer id, String name, String code, boolean active) {
        this.id = id;
        this.name = name;
        this.code = code;
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
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public boolean getActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Management getManagement() {
        return management;
    }
    
    public void setManagement(Management management) {
        this.management = management;
    }
    
    @XmlTransient
    public Set<Position> getPositionSet() {
        return positionSet;
    }
    
    public void setPositionSet(Set<Position> positionSet) {
        this.positionSet = positionSet;
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
        if (!(object instanceof Area)) {
            return false;
        }
        Area other = (Area) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    
    @Override
    public String toString() {
        return "com.entity.entity.Area[ id=" + id + " ]";
    }
    
}
