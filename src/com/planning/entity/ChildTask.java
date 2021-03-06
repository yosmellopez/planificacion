/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;

/**
 * @author Nodo
 */
@Entity
@Table(name = "child_task", uniqueConstraints = @UniqueConstraint(name = "unique_from_to", columnNames = {"from_id", "to_id"}))
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChildTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "child_task_id_seq")
    @SequenceGenerator(name = "child_task_id_seq", sequenceName = "child_task_id_seq", allocationSize = 1, initialValue = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "ispredecessor", nullable = false)
    @ColumnDefault(value = "0")
    private boolean isChild;

    @Column(name = "from_id")
    private Integer from;

    @Column(name = "to_id")
    private Integer to;

    public ChildTask() {
    }

    public ChildTask(Integer id) {
        this.id = id;
    }

    public ChildTask(Integer id, boolean isChild) {
        this.id = id;
        this.isChild = isChild;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getIsChild() {
        return isChild;
    }

    public void setIsChild(boolean ispredecessor) {
        this.isChild = ispredecessor;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer childid) {
        this.from = childid;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean child) {
        isChild = child;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public boolean isValid() {
        return !from.equals(to);
    }

    public ChildTask clonar() {
        ChildTask childTask = new ChildTask();
        childTask.setChild(isChild);
        childTask.setFrom(from);
        childTask.setTo(to);
        return childTask;
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
        if (!(object instanceof ChildTask)) {
            return false;
        }
        ChildTask other = (ChildTask) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.entity.entity.ChildTask[ id=" + id + " ]";
    }

}
