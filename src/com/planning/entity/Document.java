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
import java.io.Serializable;

/**
 * @author Nodo
 */
@Entity
@Table(name = "document")
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_id_seq")
    @SequenceGenerator(name = "document_id_seq", sequenceName = "document_id_seq", allocationSize = 1, initialValue = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @JsonProperty(value = "modelo_id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "docpath", nullable = false, length = 450)
    @JsonProperty(value = "ruta")
    private String docpath;

    @Column(name = "description", length = 100)
    @JsonProperty(value = "descripcion")
    private String description;

    @JsonIgnore
    @Basic(optional = false)
    @Column(name = "model", nullable = false)
    @ColumnDefault(value = "true")
    private boolean model;

    @Basic(optional = false)
    @Column(name = "active", nullable = false)
    @JsonProperty(value = "estado")
    @ColumnDefault(value = "true")
    private boolean active;

    @JsonIgnore
    @Column(name = "ultimo", nullable = true)
    private boolean ultimo = false;

    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name = "taskid", referencedColumnName = "id", nullable = true,
                foreignKey = @ForeignKey(name = "fk_document_task"))
    private Task task;

    public Document() {
    }

    public Document(Integer id) {
        this.id = id;
    }

    public Document(String docpath, String descripcion, boolean model, boolean active) {
        this.docpath = docpath;
        this.model = model;
        this.active = active;
        this.description = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocpath() {
        return docpath;
    }

    public void setDocpath(String docpath) {
        this.docpath = docpath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getModel() {
        return model;
    }

    public void setModel(boolean model) {
        this.model = model;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isUltimo() {
        return ultimo;
    }

    public void setUltimo(boolean ultimo) {
        this.ultimo = ultimo;
    }

    public void clonarDatos(Document d) {
        description = d.description;
        active = d.active;
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
        if (!(object instanceof Document)) {
            return false;
        }
        Document other = (Document) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.entity.entity.Document[ id=" + id + " ]";
    }

}
