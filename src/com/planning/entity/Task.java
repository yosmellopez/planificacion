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
@Table(name = "task")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "nivelesAlerta",
                      attributeNodes = {
                          @NamedAttributeNode(value = "documents")
                          , @NamedAttributeNode(value = "channels")
                      }, subgraphs = {
                @NamedSubgraph(name = "area_direcciones", attributeNodes = {
            @NamedAttributeNode(value = "area", subgraph = "administracion")
            ,@NamedAttributeNode(value = "direcciones")})
                ,@NamedSubgraph(name = "direcciones", attributeNodes = @NamedAttributeNode(value = "area", subgraph = "administracion"))
                ,@NamedSubgraph(name = "administracion", attributeNodes = @NamedAttributeNode(value = "management"))
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

    @Column(name = "description", length = 50000)
    @JsonProperty(value = "descripcion")
    private String description = "";

    @JsonProperty(value = "producto")
    @Column(name = "product", length = 500)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String product;

    @JsonIgnore
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion = new Date();

    @JsonProperty(value = "modelos")
    @OneToMany(mappedBy = "task", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Document> documents = new HashSet<>();

    @JsonProperty(value = "canales_id")
    @JsonSerialize(using = ListChannelsSerializer.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "task_channel", foreignKey = @ForeignKey(name = "fk_task_channel"), inverseForeignKey = @ForeignKey(name = "fk_channel_task"),
               joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "channel_id"))
    private Set<Channel> channels = new HashSet<>();

    @Transient
    private String canales = "";

    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Tarea> relacionadas = new ArrayList<>();

    public Task() {

    }

    public Task(Integer id) {
        this.id = id;
    }

    public Task(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Task(Task t) {
        id = t.id;
        name = t.name;
        description = t.description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Tarea> getRelacionadas() {
        return relacionadas;
    }

    public void setRelacionadas(List<Tarea> relacionadas) {
        this.relacionadas = relacionadas;
    }

    public String getCanales() {
        int i = 0;
        if (channels.isEmpty()) {
            return "Sin canales";
        }
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
        if (channels.isEmpty()) {
            this.canales = "Sin canales";
        }
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set<Channel> getChannels() {
        return channels;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
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
        description = other.description == null || other.description.isEmpty() ? description : other.description;
        name = other.name;
        product = other.product;
        channels = other.channels;
    }
}
