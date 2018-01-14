/*Generado por Disrupsoft*/
package com.planning.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/
import com.planning.entity.*;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Service object for domain model class Area.
 *
 * @see Area
 */
public interface AreaService extends JpaRepository<Area, Integer> {

    public List<Area> findByManagement(Management m);

    public List<Area> findByManagementId(Integer id);
}
