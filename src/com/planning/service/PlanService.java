/*Generado por Disrupsoft*/
package com.planning.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/
import com.planning.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface PlanService extends JpaRepository<Plan, Integer>, JpaSpecificationExecutor<Plan> {

    public List<Plan> findByFechaCreacionBetween(Date fechaInicio, Date fechaFin);

    public Page<Plan> findByFechaCreacionBetween(Date fechaInicio, Date fechaFin, Pageable pageable);

    public List<Plan> findByFechaCreacionBetweenAndStatusplanid(Date fechaInicio, Date fechaFin, StatusPlan statusPlan);

    public Page<Plan> findByFechaCreacionBetweenAndStatusplanid(Date fechaInicio, Date fechaFin, StatusPlan statusPlan, Pageable pageable);

    public List<Plan> findByStatusplanid(StatusPlan statusPlan);

    public Page<Plan> findByStatusplanid(StatusPlan statusPlan, Pageable pageable);
}
