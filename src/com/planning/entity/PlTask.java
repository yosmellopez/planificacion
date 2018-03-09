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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Nodo
 */
@Entity
@Table(name = "pl_task")
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedEntityGraphs({
    @NamedEntityGraph(name = "PlanTarea.tareas", attributeNodes = {
        @NamedAttributeNode(value = "plan", subgraph = "plan")
        ,
                @NamedAttributeNode(value = "criticalyLevels")
        ,
                @NamedAttributeNode(value = "grupos")
        ,
                @NamedAttributeNode(value = "channels"),}, subgraphs = {
        @NamedSubgraph(name = "plan", attributeNodes = @NamedAttributeNode(value = "criticalyLevel"))
    })})
public class PlTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pl_task_id_seq")
    @SequenceGenerator(name = "pl_task_id_seq", sequenceName = "pl_task_id_seq", allocationSize = 1, initialValue = 1)
    @Basic(optional = false)
    @JsonProperty(value = "planTareaId")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 1000)
    private String name;

    @Column(name = "code", length = 100)
    @JsonProperty(value = "codigo")
    private String code;

    @Lob
    @Column(name = "description", length = 50000)
    @JsonProperty(value = "descripcion")
    private String description = "";

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

    @Column(name = "tiempo_recurrencia")
    @ColumnDefault(value = "0")
    private Integer tiempoRecurrencia;

    @Basic(optional = false)
    @Column(name = "tranversal", nullable = false)
    @ColumnDefault(value = "0")
    @JsonProperty(value = "tranversal")
    private boolean tranversal;

    @JsonProperty(value = "producto")
    @Column(name = "product", length = 500)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idplan", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_pltask_plan"))
    private Plan plan;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_plan_task_statustask"))
    @JsonProperty(value = "estado")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StatusTask statusTask;

    @JsonProperty(value = "criticidad_id")
    @JsonSerialize(using = ListCriticalyLevelSerializer.class)
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "plan_task_criticaly_level",
               joinColumns = @JoinColumn(name = "plan_tasks_id", foreignKey = @ForeignKey(name = "fk_plan_task_level")),
               inverseJoinColumns = @JoinColumn(name = "criticalylevels_id", foreignKey = @ForeignKey(name = "fk_level_plan_task")))
    private Set<CriticalyLevel> criticalyLevels = new HashSet<>();

    @Transient
    private Set<Document> documents = new HashSet<>();

    @ManyToOne(optional = false)
    @JsonProperty(value = "cargo_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JoinColumn(name = "idposition", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_position"))
    private Position position;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "taskGrupo", fetch = FetchType.EAGER)
    private List<Grupo> grupos;

    @JsonProperty(value = "canales_id")
    @JsonSerialize(using = ListChannelsSerializer.class)
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "plan_task_channel", foreignKey = @ForeignKey(name = "fk_plan_task_channel"), inverseForeignKey = @ForeignKey(name = "fk_channel_plan_task"),
               joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "channel_id"))
    private Set<Channel> channels = new HashSet<>();

    @JsonIgnore
    @Column(name = "fecha_asignacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAsignacion = new Date();

    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Tarea> relacionadas = new ArrayList<>();

    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Tarea> agrupadas = new ArrayList<>();

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_usuario"))
    private Users usuario;

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

    @JsonIgnore
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion = new Date();

    public PlTask() {
    }

    public PlTask(Integer id) {
        this.id = id;
    }

    public PlTask(String id) {
        this.id = Integer.parseInt(id);
    }

    public PlTask(Integer id, String name, int idtask) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCargo() {
        cargo = position != null ? position.getName() : "";
        return cargo;
    }

    public void setCargo(String cargo) {
        cargo = position != null ? position.getName() : "";
        this.cargo = cargo;
    }

    public String getGerencia() {
        gerencia = position != null ? position.getArea().getManagement().getName() : "";
        return gerencia;
    }

    public void setGerencia(String gerencia) {
        gerencia = position != null ? position.getArea().getManagement().getName() : "";
        this.gerencia = gerencia;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    public String fechaActual() {
        if (fechaAsignacion == null) {
            return "";
        }
        String mensajeTiempo;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date fechafinal = calendar.getTime();
        calendar.setTime(fechaAsignacion);
        Date fechainicial = calendar.getTime();
        if (fechafinal.getTime() - fechainicial.getTime() <= 60000) {
            mensajeTiempo = "Hace unos segundos";
        } else if (fechafinal.getTime() - fechainicial.getTime() > 60000 && fechafinal.getTime() - fechainicial.getTime() <= 3600000) {
            mensajeTiempo = "Hace " + ((fechafinal.getTime() - fechainicial.getTime()) / 60000) + " minutos";
        } else if (fechafinal.getTime() - fechainicial.getTime() > 60000 && fechafinal.getTime() - fechainicial.getTime() <= 86400000) {
            mensajeTiempo = "" + ((fechafinal.getTime() - fechainicial.getTime()) / 3600000) + ":00 hrs " + (((fechafinal.getTime() - fechainicial.getTime()) % 3600000) / 60000) + " minutos";
        } else if ((fechafinal.getTime() - fechainicial.getTime()) > 86400000 && fechafinal.getTime() - fechainicial.getTime() <= 2678400000L) {
            int dias = (int) ((fechafinal.getTime() - fechainicial.getTime()) / 86400000);
            mensajeTiempo = dias + (dias == 1 ? " día atrás." : " días atrás.");
        } else {
            int mes = (int) ((fechafinal.getTime() / 2678400000L) - (fechainicial.getTime() / 2678400000L));
            mensajeTiempo = mes + (mes == 1 ? " mes atrás." : " meses atrás.");
        }
        return mensajeTiempo;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public StatusTask getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isHito() {
        return hito;
    }

    public void setHito(boolean hito) {
        this.hito = hito;
    }

    public boolean isIsrecurrent() {
        return isrecurrent;
    }

    public void setIsrecurrent(boolean isrecurrent) {
        this.isrecurrent = isrecurrent;
    }

    public Integer getTiempoRecurrencia() {
        return tiempoRecurrencia;
    }

    public void setTiempoRecurrencia(Integer tiempoRecurrencia) {
        this.tiempoRecurrencia = tiempoRecurrencia;
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

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Users getUsuario() {
        return usuario;
    }

    public void setUsuario(Users usuario) {
        this.usuario = usuario;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
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
        if (!(object instanceof PlTask)) {
            return false;
        }
        PlTask other = (PlTask) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "PlTask[ id=" + id + ", plan=" + plan + ", tarea=" + name + "]";
    }

    public void clonarDatos(PlTask tarea) {
        channels = tarea.channels;
        code = tarea.code;
        hito = tarea.hito;
        isrecurrent = tarea.isrecurrent;
        position = tarea.position;
        start = tarea.start;
        product = tarea.product;
        statusTask = tarea.statusTask;
        tiempoRecurrencia = tarea.tiempoRecurrencia;
        tranversal = tarea.tranversal;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }
}
