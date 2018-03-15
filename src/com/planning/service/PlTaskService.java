/*Generado por Disrupsoft*/
package com.planning.service;

import com.planning.entity.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

public interface PlTaskService extends JpaRepository<PlTask, Integer>, JpaSpecificationExecutor<PlTask> {
    
    @EntityGraph(value = "PlanTarea.tareas")
    public List<PlTask> findByPlan(Plan plan);
    
    @EntityGraph(value = "PlanTarea.tareas")
    public List<PlTask> findByPlan(Plan plan, Sort sort);
    
    @EntityGraph(value = "PlanTarea.tareas")
    public List<PlTask> findByPlanAndCriticalyLevelsContains(Plan plan, CriticalyLevel criticalyLevel);
    
    @EntityGraph(value = "PlanTarea.tareas")
    public Optional<PlTask> findByPlanAndStart(Plan plan, boolean start);
    
    @Override
    @EntityGraph(value = "PlanTarea.tareas")
    public PlTask findOne(Integer id);
    
    @EntityGraph(value = "PlanTarea.tareas")
    public List<PlTask> findByPlanAndCriticalyLevelsContainsAndPosition(Plan plan, CriticalyLevel criticalyLevel, Position position);
    
    @EntityGraph(value = "PlanTarea.tareas")
    public List<PlTask> findByPlanAndCriticalyLevelsContainsAndPosition_Area(Plan plan, CriticalyLevel criticalyLevel, Area areaBd);
    
    public List<PlTask> findByPlanAndCriticalyLevelsContainsAndPosition_Area_Management(Plan plan, CriticalyLevel criticalyLevel, Management management);
    
    public List<PlTask> findByPlanAndPosition(Plan plan, Position position);
    
    public List<PlTask> findByPlanAndPosition_Area(Plan plan, Area areaBd);
    
    public List<PlTask> findByPlanAndPosition_Area_Management(Plan plan, Management management);
    
    public List<PlTask> findByCriticalyLevelsContainsAndPosition(CriticalyLevel level, Position position);
    
    public List<PlTask> findByCriticalyLevelsContainsAndPosition_Area(CriticalyLevel level, Area areaBd);
    
    public List<PlTask> findByCriticalyLevelsContainsAndPosition_Area_Management(CriticalyLevel level, Management management);
    
    public List<PlTask> findByCriticalyLevelsContains(CriticalyLevel level);
    
    public List<PlTask> findByUsuarioAndPlan(Users usuario, Plan plan);
    
    public List<PlTask> findByPosition(Position position);
    
    public List<PlTask> findByPosition_Area(Area area);
    
    public List<PlTask> findByPosition_Area_Management(Management management);
    
    public Page<PlTask> findByCriticalyLevelsContainsAndPosition(CriticalyLevel level, Position position, Pageable pageRequest);
    
    public Page<PlTask> findByPosition(Position position, Pageable pageRequest);
    
    public Page<PlTask> findByPosition_Area_Management(Management management, Pageable pageRequest);
    
    public Page<PlTask> findByPosition_Area(Area findOne, Pageable pageRequest);
    
    public Page<PlTask> findByCriticalyLevelsContains(CriticalyLevel findOne, Pageable pageRequest);
    
    public Page<PlTask> findByCriticalyLevelsContainsAndPosition_Area_Management(CriticalyLevel level, Management management, Pageable pageRequest);
    
    public Page<PlTask> findByCriticalyLevelsContainsAndPosition_Area(CriticalyLevel level, Area areaBd, Pageable pageRequest);
    
    public long countByPosition(Position position);
    
    public long countByPositionAndPlan(Position position, Plan plan);
    
    public Integer countByStatusTaskAndPosition(StatusTask statusTask, Position position);
    
    public Integer countByStatusTaskAndPositionAndPlan(StatusTask statusTask, Position position, Plan plan);
    
    public List<PlTask> findByStatusTaskAndPosition(StatusTask statusTask, Position position, Sort sort);
    
    public List<PlTask> findByStatusTaskAndPositionAndPlan(StatusTask statusTask, Position position, Plan plan, Sort sort);
    
    public Long countByStatusTask(StatusTask statusTask);
    
    public Long countByPlanAndStatusTask(Plan plan, StatusTask statusTask);
    
    public Long countByPlan(Plan plan);
    
    public Long countByPlanAndStart(Plan plan, boolean start);
    
    @Transactional
    public void deleteByPlan(Plan plan);
    
}
