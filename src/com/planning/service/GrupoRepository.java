/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.service;

import com.planning.entity.GroupPK;
import com.planning.entity.Grupo;
import com.planning.entity.Plan;
import com.planning.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Nodo
 */
public interface GrupoRepository extends JpaRepository<Grupo, GroupPK> {
    
    List<Grupo> findByTaskGrupo(Task task);
    
    List<Grupo> findByTaskGrupo(Task task, Sort sort);
    
    Page<Grupo> findByTaskGrupo(Task task, Pageable pageable);
    
    List<Grupo> findByPlan(Plan plan);
    
    List<Grupo> findByPlan(Plan plan, Sort sort);
    
    Page<Grupo> findByPlan(Plan plan, Pageable pageable);
    
    List<Grupo> findByPlanAndTaskAgrupada(Plan plan, Task task);
    
    List<Grupo> findByPlanAndTaskAgrupada(Plan plan, Task task, Sort sort);
    
    Page<Grupo> findByPlanAndTaskAgrupada(Plan plan, Task task, Pageable pageable);
    
    List<Grupo> findByPlanAndTaskGrupo(Plan plan, Task task);
    
    List<Grupo> findByPlanAndTaskGrupo(Plan plan, Task task, Sort sort);
    
    Page<Grupo> findByPlanAndTaskGrupo(Plan plan, Task task, Pageable pageable);
    
    @Transactional
    void deleteByTaskGrupo(Task task);
}
