/*Generado por Disrupsoft*/
package com.planning.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/
import com.planning.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Service object for domain model class CriticalyLevel.
 *
 * @see CriticalyLevel
 */
public interface CriticalyLevelService extends JpaRepository<CriticalyLevel, Integer> {

    public CriticalyLevel findByOrder(int i);

}
