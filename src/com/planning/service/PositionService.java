/*Generado por Disrupsoft*/
package com.planning.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/
import com.planning.entity.Area;
import com.planning.entity.Management;
import com.planning.entity.Position;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionService extends JpaRepository<Position, Integer> {

    @Override
    @EntityGraph(value = "Cargo.area")
    public List<Position> findAll();

    @Override
    @EntityGraph(value = "Cargo.area")
    public Page<Position> findAll(Pageable pageable);

    public List<Position> findByAreaId(Integer id);

    public List<Position> findByArea_Management(Management management);

    public List<Position> findByArea(Area area);
}
