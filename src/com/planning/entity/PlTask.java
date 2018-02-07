/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

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
@Table(name = "pl_task", uniqueConstraints = @UniqueConstraint(name = "plan_tarea_unica", columnNames = {"idtask", "idplan"}))
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedEntityGraphs({
    @NamedEntityGraph(name = "PlanTarea.tareas", attributeNodes = {
        @NamedAttributeNode(value = "task", subgraph = "cargo")
    }, subgraphs = {
        @NamedSubgraph(name = "cargo", attributeNodes = @NamedAttributeNode(value = "position", subgraph = "area"))
        , @NamedSubgraph(name = "area", attributeNodes = @NamedAttributeNode(value = "area", subgraph = "administracion"))
        , @NamedSubgraph(name = "administracion", attributeNodes = @NamedAttributeNode(value = "management"))
    })})
public class PlTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pl_task_id_seq")
    @SequenceGenerator(name = "pl_task_id_seq", sequenceName = "pl_task_id_seq", allocationSize = 1, initialValue = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 1000)
    private String name;

    @Basic(optional = false)
    @Column(name = "posx", nullable = false)
    private int posx;

    @Basic(optional = false)
    @Column(name = "posy", nullable = false)
    private int posy;

    @Basic(optional = false)
    @Column(name = "width", nullable = false)
    private int width;

    @Basic(optional = false)
    @Column(name = "height", nullable = false)
    @ColumnDefault(value = "0")
    private int height;

    @Basic(optional = false)
    @Column(name = "position", nullable = false)
    @ColumnDefault(value = "0")
    private int position;

    @Column(name = "partida")
    @JsonProperty(value = "partida")
    @ColumnDefault(value = "0")
    private boolean start;

    @Column(name = "ultima")
    @JsonProperty(value = "ultima")
    @ColumnDefault(value = "0")
    private boolean lastInserted;

    @JoinColumn(name = "idtask", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_pltask_task"))
    @ManyToOne(optional = false)
    private Task task;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idplan", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_pltask_plan"))
    private Plan plan;

    public PlTask() {
    }

    public PlTask(Integer id) {
        this.id = id;
    }

    public PlTask(Integer id, String name, int posx, int posy, int width, int height, int idtask) {
        this.id = id;
        this.name = name;
        this.posx = posx;
        this.posy = posy;
        this.width = width;
        this.height = height;
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

    public int getPosx() {
        return posx;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }

    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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

    public boolean isLastInserted() {
        return lastInserted;
    }

    public void setLastInserted(boolean lastInserted) {
        this.lastInserted = lastInserted;
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
        return "PlTask[ id=" + id + ", plan=" + plan + ", tarea=" + task + "]";
    }
}
