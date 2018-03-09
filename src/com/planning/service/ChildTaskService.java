/*Generado por Disrupsoft*/
package com.planning.service;

import com.planning.entity.ChildTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ChildTaskService extends JpaRepository<ChildTask, Integer> {

    public List<ChildTask> findByFrom(Integer childid);

    public List<ChildTask> findByTo(Integer childid);

    public Optional<ChildTask> findByFromAndTo(Integer from, Integer to);

    @Transactional
    public void deleteById(int id);

    public List<ChildTask> findByFromAndIsChild(Integer childid, boolean isChild);

    public List<ChildTask> findByToAndIsChild(Integer childid, boolean isChild);

    public List<ChildTask> findByFromOrTo(Integer from, Integer to);

    public Long countByFromOrTo(Integer from, Integer to);
}
