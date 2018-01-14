/*Generado por Disrupsoft*/
package com.planning.service;

import com.planning.entity.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service object for domain model class PlTask.
 *
 * @see PlTask
 */
public interface PlTaskService extends JpaRepository<PlTask, Integer> {
    
    public List<PlTask> findByPlan(Plan plan);
    
    public List<PlTask> findByPlan(Plan plan, Sort sort);
    
    public Optional<PlTask> findByLastInserted(boolean last);
    
    public PlTask findByPlanAndLastInserted(Plan plan, boolean last);
    
    public PlTask findByPlanAndTask(Plan plan, Task task);
    
    public PlTask findByPlanAndTaskAndPosition(Plan plan, Task task, int weight);
    
    public List<PlTask> findByPlanAndTask_Position(Plan plan, Position position);
    
    public List<PlTask> findByPlanAndTask_Position_Area(Plan plan, Area area);
    
    public List<PlTask> findByPlanAndTask_Position_Area_Management(Plan plan, Management management);
    
    public List<PlTask> findByPlanAndTask_CriticalyLevelsContains(Plan plan, CriticalyLevel criticalyLevel);
    
    public Optional<PlTask> findByPlanAndTaskAndStart(Plan plan, Task task, boolean start);
    
    public Optional<PlTask> findByPlanAndStart(Plan plan, boolean start);
    
    public PlTask findByPlanAndPosition(Plan plan, int peso);
}
