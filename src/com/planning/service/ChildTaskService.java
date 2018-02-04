/*Generado por Disrupsoft*/
package com.planning.service;

import com.planning.entity.ChildTask;
import com.planning.entity.PlTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;

public interface ChildTaskService extends JpaRepository<ChildTask, Integer> {

    public List<ChildTask> findByFrom(PlTask childid);

    public List<ChildTask> findByTo(PlTask childid);

    public Optional<ChildTask> findByFromAndTo(PlTask from, PlTask to);

    @Transactional
    public void deleteById(int id);

    @EntityGraph(value = "Child.listarTodas")
    public List<ChildTask> findByFromAndIsChild(PlTask childid, boolean isChild);

    public List<ChildTask> findByToAndIsChild(PlTask childid, boolean isChild);

    public List<ChildTask> findByFromOrTo(PlTask from, PlTask to);

    public Long countByFromOrTo(PlTask from, PlTask to);
}
