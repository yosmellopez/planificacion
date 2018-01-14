/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.planning.util.AreaSerializer;

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
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;

/**
 * @author Nodo
 */
@Entity
@Table(name = "position")
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Position implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "position_id_seq")
    @SequenceGenerator(name = "position_id_seq", sequenceName = "position_id_seq", allocationSize = 1, initialValue = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @JsonProperty(value = "cargo_id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 100)
    @JsonProperty(value = "nombre")
    private String name;
    
    @Column(name = "description", length = 100)
    @JsonProperty(value = "descripcion")
    private String description;
    
    @Basic(optional = false)
    @Column(name = "code", nullable = false, length = 150)
    private String code;
    
    @Basic(optional = false)
    @Column(name = "active", nullable = false)
    @ColumnDefault(value = "true")
    private boolean active;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "position")
    private Set<Users> users;
    
    @JoinColumn(name = "idarea", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_position_area"))
    @ManyToOne(optional = false)
    @JsonProperty(value = "area_id")
    @JsonSerialize(using = AreaSerializer.class)
    private Area area;
    
    public Position() {
    }
    
    public Position(Integer id) {
        this.id = id;
    }
    
    public Position(String id) {
        this.id = Integer.parseInt(id);
    }
    
    public Position(Integer id, String name, String code, boolean active) {
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
    
    @XmlTransient
    public Set<Users> getUsers() {
        return users;
    }
    
    public void setUsers(Set<Users> users) {
        this.users = users;
    }
    
    public Area getArea() {
        return area;
    }
    
    public void setArea(Area area) {
        this.area = area;
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
        if (!(object instanceof Position)) {
            return false;
        }
        Position other = (Position) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.entity.entity.Position[ id=" + id + " ]";
    }
    
}
