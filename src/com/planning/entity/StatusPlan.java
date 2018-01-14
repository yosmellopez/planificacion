/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;

/**
 *
 * @author Nodo
 */
@Entity
@Table(name = "status_plan")
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StatusPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_plan_id_seq")
    @SequenceGenerator(name = "status_plan_id_seq", sequenceName = "status_plan_id_seq", allocationSize = 1, initialValue = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @JsonProperty(value = "estado_id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 100)
    @JsonProperty(value = "descripcion")
    private String name;

    @Column(name = "active")
    @JsonProperty(value = "estado")
    @ColumnDefault(value = "true")
    private boolean active;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statusplanid")
    private Set<Plan> planSet;

    public StatusPlan() {
    }

    public StatusPlan(Integer id) {
        this.id = id;
    }

    public StatusPlan(String id) {
        this.id = Integer.parseInt(id);
    }

    public StatusPlan(Integer id, String name) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @XmlTransient
    public Set<Plan> getPlanSet() {
        return planSet;
    }

    public void setPlanSet(Set<Plan> planSet) {
        this.planSet = planSet;
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
        if (!(object instanceof StatusPlan)) {
            return false;
        }
        StatusPlan other = (StatusPlan) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.entity.entity.StatusPlan[ id=" + id + " ]";
    }

}
