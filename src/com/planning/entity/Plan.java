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

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.*;

/**
 * @author Nodo
 */
@Entity
@Table(name = "plan")
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Plan implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plan_id_seq")
    @SequenceGenerator(name = "plan_id_seq", sequenceName = "plan_id_seq", allocationSize = 1, initialValue = 1)
    @JsonProperty(value = "plan_id")
    @Column(nullable = false)
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 100)
    @JsonProperty(value = "nombre")
    private String name;
    
    @JsonProperty(value = "descripcion")
    @Column(name = "description", length = 150)
    private String description;
    
    @Column(name = "diagrama", length = 50000)
    private String diagrama;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion = new Date();
    
    @JoinColumn(name = "statusplanid", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_plan_statustask"))
    @ManyToOne(optional = false)
    @JsonProperty(value = "estado_id")
    private StatusPlan statusplanid;
    
    @JsonIgnore
    @OneToMany(mappedBy = "plan", fetch = FetchType.EAGER)
    @OrderBy(value = "position ASC")
    private Set<PlTask> plTasks = new HashSet<>();
    
    @Transient
    private List<MiTarea> tareas = new ArrayList<>();
    
    @Transient
    private List<PlTaskUtil> tareasPlan = new ArrayList<>();
    
    public Plan() {
    }
    
    public Plan(Integer id) {
        this.id = id;
    }
    
    public Plan(String id) {
        this.id = Integer.parseInt(id);
    }
    
    public Plan(Integer id, String name) {
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
    
    public StatusPlan getStatusplanid() {
        return statusplanid;
    }
    
    public void setStatusplanid(StatusPlan statusplanid) {
        this.statusplanid = statusplanid;
    }
    
    @XmlTransient
    public Set<PlTask> getPlTasks() {
        return plTasks;
    }
    
    public void setPlTasks(Set<PlTask> plTasks) {
        for (PlTask plTask : plTasks) {
            tareas.add(new MiTarea(plTask));
        }
        this.plTasks = plTasks;
    }
    
    public List<MiTarea> getTareas() {
        return tareas;
    }
    
    public void setTareas(List<MiTarea> tareas) {
        this.tareas = tareas;
    }
    
    public String getDiagrama() {
        return diagrama;
    }
    
    public void setDiagrama(String diagrama) {
        this.diagrama = diagrama;
    }
    
    public List<PlTaskUtil> getTareasPlan() {
        return tareasPlan;
    }
    
    public void setTareasPlan(List<PlTaskUtil> tareasPlan) {
        this.tareasPlan = tareasPlan;
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
        if (!(object instanceof Plan)) {
            return false;
        }
        Plan other = (Plan) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    
    @Override
    public String toString() {
        return "com.entity.entity.Plan[ id=" + id + ", nombre=" + name + " ]";
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
}
