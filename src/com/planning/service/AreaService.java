/*Generado por Disrupsoft*/
package com.planning.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import com.planning.entity.Area;
import com.planning.entity.Management;
import com.planning.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Service object for domain model class Area.
 *
 * @see Area
 */
public interface AreaService extends JpaRepository<Area, Integer>, JpaSpecificationExecutor<Area> {
    
    public List<Area> findByManagement(Management m);
    
    public Page<Area> findByManagement(Management m, Pageable pageable);
    
    public List<Area> findByManagement(Management m, Sort sort);
    
    public List<Area> findByManagementId(Integer id);
}
