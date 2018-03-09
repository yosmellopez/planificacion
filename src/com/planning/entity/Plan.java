/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    
    @Lob
    @Column(name = "diagrama")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String diagrama;
    
    @Lob
    @Column(name = "modelo_agrupado")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String modeloAgrupado;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion = new Date();
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "statusplanid", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_plan_status_plan"))
    @JsonProperty(value = "estado_id")
    private StatusPlan statusplanid;
    
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_plan_estado_tarea"))
    @JsonProperty(value = "nivelAlerta")
    private CriticalyLevel criticalyLevel;
    
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_plan_user_activado"))
    private Users usuario;
    
    @Column(name = "ejecucion")
    @ColumnDefault(value = "0")
    private boolean ejecucion = false;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_activacion")
    private Date fechaActivacion = new Date();
    
    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MiTarea> tareas = new ArrayList<>();
    
    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
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
    
    public void setPlTasks(Set<PlTask> plTasks) {
        for (PlTask plTask : plTasks) {
            tareas.add(new MiTarea(plTask));
        }
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
    
    public String getModeloAgrupado() {
        return modeloAgrupado;
    }
    
    public void setModeloAgrupado(String modeloAgrupado) {
        this.modeloAgrupado = modeloAgrupado;
    }
    
    public boolean isEjecucion() {
        return ejecucion;
    }
    
    public void setEjecucion(boolean ejecucion) {
        this.ejecucion = ejecucion;
    }
    
    public CriticalyLevel getCriticalyLevel() {
        return criticalyLevel;
    }
    
    public void setCriticalyLevel(CriticalyLevel criticalyLevel) {
        this.criticalyLevel = criticalyLevel;
    }
    
    public Date getFechaActivacion() {
        return fechaActivacion;
    }
    
    public void setFechaActivacion(Date fechaActivacion) {
        this.fechaActivacion = fechaActivacion;
    }
    
    public Users getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Users usuario) {
        this.usuario = usuario;
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
