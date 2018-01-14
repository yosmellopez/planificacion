/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.planning.util.ListCriticalyLevelSerializer;

import java.io.Serializable;
import java.util.*;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;

/**
 * @author Nodo
 */
@Entity
@Table(name = "task")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "Task.criticalyLevels", attributeNodes = {
                @NamedAttributeNode(value = "criticalyLevels"),
                @NamedAttributeNode(value = "position"),
                @NamedAttributeNode(value = "channel"),
                @NamedAttributeNode(value = "statusTask")
        })})
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Task implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_seq")
    @SequenceGenerator(name = "task_id_seq", sequenceName = "task_id_seq", allocationSize = 1, initialValue = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @JsonProperty(value = "tarea_id")
    private Integer id;
    
    @Basic(optional = false)
    @JsonProperty(value = "nombre")
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Basic(optional = false)
    @Column(name = "code", nullable = false, length = 100)
    @JsonProperty(value = "codigo")
    private String code;
    
    @Column(name = "description", length = 50000)
    @JsonProperty(value = "descripcion")
    private String description = "";
    
    @Column(name = "product", length = 500)
    @JsonProperty(value = "producto")
    private String product;
    
    @Column(name = "partida")
    @JsonProperty(value = "partida")
    @ColumnDefault(value = "0")
    private boolean start;
    
    @Column(name = "hito")
    @ColumnDefault(value = "0")
    private boolean hito = false;
    
    @Basic(optional = false)
    @Column(name = "isrecurrent", nullable = false)
    @ColumnDefault(value = "0")
    @JsonProperty(value = "recurrente")
    private boolean isrecurrent;
    
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion = new Date();
    
    @OneToMany(mappedBy = "task", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonProperty(value = "modelos")
    private Set<Document> documents = new HashSet<>();
    
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_statustask"))
    @JsonProperty(value = "estado")
    private StatusTask statusTask;
    
    @JsonProperty(value = "criticidad_id")
    @JsonSerialize(using = ListCriticalyLevelSerializer.class)
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(name = "task_criticaly_level", foreignKey = @ForeignKey(name = "fk_task_level"), inverseForeignKey = @ForeignKey(name = "fk_level_task"),
            joinColumns = @JoinColumn(name = "tasks_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "criticalylevels_id", referencedColumnName = "id"))
    private Set<CriticalyLevel> criticalyLevels = new HashSet<>();
    
    @JoinColumn(name = "idposition", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_pltask_position"))
    @ManyToOne(optional = false)
    @JsonProperty(value = "cargo_id")
    private Position position;
    
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_channel"))
    @JsonProperty(value = "canal")
    private Channel channel;
    
    @Transient
    private List<Tarea> relacionadas = new ArrayList<>();
    
    @Transient
    private String cargo = "";
    
    @Transient
    private String gerencia;
    
    @Transient
    private String criticidad = "";
    
    @Transient
    private String color = "#333333";
    
    public Task() {
    
    }
    
    public Task(Integer id) {
        this.id = id;
    }
    
    public Task(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getProduct() {
        return product;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isIsrecurrent() {
        return isrecurrent;
    }
    
    public void setIsrecurrent(boolean isrecurrent) {
        this.isrecurrent = isrecurrent;
    }
    
    public boolean isHito() {
        return hito;
    }
    
    public void setHito(boolean hito) {
        this.hito = hito;
    }
    
    @XmlTransient
    public Set<Document> getDocuments() {
        return documents;
    }
    
    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public boolean isStart() {
        return start;
    }
    
    public void setStart(boolean start) {
        this.start = start;
    }
    
    public StatusTask getStatusTask() {
        return statusTask;
    }
    
    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }
    
    public Set<CriticalyLevel> getCriticalyLevels() {
        return criticalyLevels;
    }
    
    public void setCriticalyLevels(Set<CriticalyLevel> criticalyLevels) {
        this.criticalyLevels = criticalyLevels;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public Channel getChannel() {
        return channel;
    }
    
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    
    public String getCargo() {
        cargo = position.getName();
        return cargo;
    }
    
    public void setCargo(String cargo) {
        cargo = position.getName();
        this.cargo = cargo;
    }
    
    public String getCriticidad() {
        int i = 0;
        criticidad = "";
        for (CriticalyLevel criticalyLevel : criticalyLevels) {
            criticidad += (i == 0 ? "" : ", ") + criticalyLevel.getName();
            i++;
        }
        return criticidad;
    }
    
    public void setCriticidad(String criticidad) {
        int i = 0;
        for (CriticalyLevel criticalyLevel : criticalyLevels) {
            criticidad += (i == 0 ? "" : ", ") + criticalyLevel.getName();
            i++;
        }
        this.criticidad = criticidad;
    }
    
    public String getGerencia() {
        gerencia = position != null ? position.getArea().getManagement().getName() : "";
        return gerencia;
    }
    
    public void setGerencia(String gerencia) {
        gerencia = position != null ? position.getArea().getManagement().getName() : "";
        this.gerencia = gerencia;
    }
    
    public List<Tarea> getRelacionadas() {
        return relacionadas;
    }
    
    public void setRelacionadas(List<Tarea> relacionadas) {
        this.relacionadas = relacionadas;
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
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    
    @Override
    public String toString() {
        return "Task[ id=" + id + ", nombre=" + name + " ]";
    }
    
    public void clonarDatos(Task other) {
        if (other.code != null && !other.code.isEmpty()) {
            code = other.code;
        }
        description = other.description == null || other.description.isEmpty() ? description : other.description;
        name = other.name;
        product = other.product;
        hito = other.hito;
        position = other.position;
        statusTask = other.statusTask;
        start = other.start;
        channel = other.channel;
        isrecurrent = other.isrecurrent;
        criticalyLevels = other.criticalyLevels;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}
