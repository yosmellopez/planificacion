/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.planning.util.ListChannelsSerializer;
import com.planning.util.ListCriticalyLevelSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.*;

/**
 * @author Nodo
 */
@Entity
@Table(name = "task")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "nivelesAlerta",
                      attributeNodes = {
                          @NamedAttributeNode(value = "criticalyLevels")
                          ,@NamedAttributeNode(value = "documents")
                          ,@NamedAttributeNode(value = "channels")
                          ,@NamedAttributeNode(value = "grupos")
                          ,@NamedAttributeNode(value = "position", subgraph = "area")
                          ,@NamedAttributeNode(value = "statusTask")
                      }, subgraphs = {
                @NamedSubgraph(name = "area", attributeNodes = @NamedAttributeNode(value = "area", subgraph = "administracion")),
                @NamedSubgraph(name = "administracion", attributeNodes = @NamedAttributeNode(value = "management"))
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

    @JsonProperty(value = "producto")
    @Column(name = "product", length = 500)
    @JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Column(name = "tiempo_recurrencia", nullable = false)
    @ColumnDefault(value = "0")
    private Integer tiempoRecurrencia;

    @Basic(optional = false)
    @Column(name = "tranversal", nullable = false)
    @ColumnDefault(value = "0")
    @JsonProperty(value = "tranversal")
    private boolean tranversal;

    @JsonIgnore
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion = new Date();

    @OneToMany(mappedBy = "task", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonProperty(value = "modelos")
    private Set<Document> documents = new HashSet<>();

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_statustask"))
    @JsonProperty(value = "estado")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StatusTask statusTask;

    @JsonProperty(value = "criticidad_id")
    @JsonSerialize(using = ListCriticalyLevelSerializer.class)
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(name = "task_criticaly_level", foreignKey = @ForeignKey(name = "fk_task_level"), inverseForeignKey = @ForeignKey(name = "fk_level_task"),
               joinColumns = @JoinColumn(name = "tasks_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "criticalylevels_id", referencedColumnName = "id"))
    private Set<CriticalyLevel> criticalyLevels = new HashSet<>();

    @JsonProperty(value = "canales_id")
    @JsonSerialize(using = ListChannelsSerializer.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "task_channel", foreignKey = @ForeignKey(name = "fk_task_channel"), inverseForeignKey = @ForeignKey(name = "fk_channel_task"),
               joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "channel_id"))
    private Set<Channel> channels = new HashSet<>();

    @ManyToOne(optional = false)
    @JsonProperty(value = "cargo_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JoinColumn(name = "idposition", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_position"))
    private Position position;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "taskGrupo", fetch = FetchType.EAGER)
    private List<Grupo> grupos;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Tarea> relacionadas = new ArrayList<>();

    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Tarea> agrupadas = new ArrayList<>();

    @Transient
    private String cargo = "";

    @Transient
    private String gerencia;

    @Transient
    private String criticidad = "";

    @Transient
    private String canales = "";

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

    public Task(Task t) {
        id = t.id;
        name = t.name;
        code = t.code;
        description = t.description;
        position = t.position;
        criticidad = t.criticidad;
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

    public Set<Channel> getChannels() {
        return channels;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Integer getTiempoRecurrencia() {
        return tiempoRecurrencia;
    }

    public void setTiempoRecurrencia(Integer tiempoRecurrencia) {
        this.tiempoRecurrencia = tiempoRecurrencia;
    }

    public String getCargo() {
        cargo = position != null ? position.getName() : "";
        return cargo;
    }

    public void setCargo(String cargo) {
        cargo = position != null ? position.getName() : "";
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

    public boolean isTranversal() {
        return tranversal;
    }

    public void setTranversal(boolean tranversal) {
        this.tranversal = tranversal;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public List<Tarea> getAgrupadas() {
        return agrupadas;
    }

    public void setAgrupadas(List<Tarea> agrupadas) {
        this.agrupadas = agrupadas;
    }

    public String getCanales() {
        int i = 0;
        for (Channel channel : channels) {
            canales += (i == 0 ? "" : ", ") + channel.getChannel();
            i++;
        }
        return canales;
    }

    public void setCanales(String canales) {
        int i = 0;
        for (Channel channel : channels) {
            canales += (i == 0 ? "" : ", ") + channel.getChannel();
            i++;
        }
        this.canales = canales;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
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
        tranversal = other.tranversal;
        position = other.position;
        statusTask = other.statusTask;
        start = other.start;
        isrecurrent = other.isrecurrent;
        if (isrecurrent) {
            tiempoRecurrencia = other.tiempoRecurrencia;
        }
        criticalyLevels = other.criticalyLevels;
        channels = other.channels;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean validarTranversal() {
        if (criticalyLevels.size() < 2) {
            return false;
        }
        LinkedList<CriticalyLevel> linkedList = new LinkedList<>(criticalyLevels);
        Collections.sort(linkedList, (CriticalyLevel c1, CriticalyLevel c2) -> {
            return c1.getOrder() > c2.getOrder() ? 1 : c1.getOrder() == c2.getOrder() ? 0 : -1;
        });
        int pos = 0;
        int size = linkedList.size();
        for (int i = 1; i < size; i++) {
            if (linkedList.get(pos).getOrder() + 1 == linkedList.get(i).getOrder()) {
                pos++;
            } else {
                return false;
            }
        }
        return pos != 0;
    }
}
