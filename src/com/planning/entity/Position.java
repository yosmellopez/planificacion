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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nodo
 */
@Entity
@Table(name = "position")
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedEntityGraphs({
        @NamedEntityGraph(name = "Cargo.area",
                attributeNodes = {
                        @NamedAttributeNode(value = "area", subgraph = "direccion")
                        , @NamedAttributeNode(value = "direcciones")
                }, subgraphs = {
                @NamedSubgraph(name = "direccion", attributeNodes = @NamedAttributeNode(value = "management"))
        })})
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
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "position")
    private Set<Users> users;
    
    @ManyToOne(optional = false)
    @JsonProperty(value = "area_id")
    @JsonSerialize(using = AreaSerializer.class)
    @JoinColumn(name = "idarea", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_position_area"))
    private Area area;
    
    @Column(name = "basico")
    @ColumnDefault(value = "0")
    private Boolean basico = false;
    
    @Column(name = "de_area")
    @ColumnDefault(value = "0")
    private Boolean deArea = false;
    
    @Transient
    private Boolean direccion = false;
    
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "fk_cargo_direccion")),
            inverseJoinColumns = @JoinColumn(name = "management_id", foreignKey = @ForeignKey(name = "fk_direccion_cargo")))
    private Set<Management> direcciones = new HashSet<>();
    
    public Position() {
    }
    
    public Position(Integer id) {
        this.id = id;
    }
    
    public Position(String id) {
        this.id = Integer.parseInt(id);
    }
    
    public Position(Integer id, String name) {
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
    
    public boolean isBasico() {
        if (!direcciones.isEmpty()) {
            basico = false;
        }
        return basico;
    }
    
    public void setBasico(Boolean basico) {
        if (basico) {
            deArea = false;
            direccion = false;
        }
        this.basico = basico;
    }
    
    public boolean isDeArea() {
        if (!direcciones.isEmpty()) {
            deArea = false;
        }
        return deArea;
    }
    
    public void setDeArea(Boolean deArea) {
        if (deArea) {
            basico = false;
            direccion = false;
        }
        this.deArea = deArea;
    }
    
    public boolean isDireccion() {
        if (!direcciones.isEmpty()) {
            direccion = true;
        }
        return direccion;
    }
    
    public void setDireccion(Boolean direccion) {
        if (!direcciones.isEmpty()) {
            direccion = true;
        }
        this.direccion = direccion;
    }
    
    public Set<Management> getDirecciones() {
        return direcciones;
    }
    
    public void setDirecciones(Set<Management> direcciones) {
        this.direcciones = direcciones;
    }
    
    public void clonarDatos(Position t) {
        name = t.name;
        area = t.area;
        basico = t.isBasico();
        deArea = t.isDeArea();
        direccion = t.isDireccion();
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
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    
    @Override
    public String toString() {
        return "Cargo[ id=" + id + ", nombre: " + name + " ]";
    }
    
}
