/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.filter;

import com.planning.entity.Area;
import com.planning.entity.CriticalyLevel;
import com.planning.entity.Management;
import com.planning.entity.PlTask;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.planning.entity.Plan;
import com.planning.entity.Position;
import java.util.ArrayList;
import org.springframework.data.jpa.domain.Specification;

public class PlTaskFilter implements Specification<PlTask> {
    
    private Plan plan;
    
    private Position position;
    
    private Area area;
    
    private Management management;
    
    private CriticalyLevel criticalyLevel;
    
    @Override
    public Predicate toPredicate(Root<PlTask> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        if (position != null) {
            predicates.add(cb.equal(root.get("position"), position));
        } else if (area != null) {
            predicates.add(cb.equal(root.get("position").get("area"), area));
        }
        if (plan != null) {
            predicates.add(cb.equal(root.get("plan"), plan));
        }
        if (criticalyLevel != null) {
            predicates.add(cb.isMember(criticalyLevel, root.get("criticalyLevels")));
        }
        Predicate[] elementos = new Predicate[predicates.size()];
        int i = 0;
        for (Predicate elemento : predicates) {
            elementos[i++] = elemento;
        }
        return cb.and(elementos);
    }
    
    public Plan getPlan() {
        return plan;
    }
    
    public void setPlan(Plan plan) {
        this.plan = plan;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public Area getArea() {
        return area;
    }
    
    public void setArea(Area area) {
        this.area = area;
    }
    
    public Management getManagement() {
        return management;
    }
    
    public void setManagement(Management management) {
        this.management = management;
    }
    
    public CriticalyLevel getCriticalyLevel() {
        return criticalyLevel;
    }
    
    public void setCriticalyLevel(CriticalyLevel criticalyLevel) {
        this.criticalyLevel = criticalyLevel;
    }
    
}
