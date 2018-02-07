/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.api;

import com.planning.entity.StatusTask;
import com.planning.entity.Task;
import com.planning.service.TaskService;
import com.planning.util.HeaderUtil;
import com.planning.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Task.
 */
@RestController
@RequestMapping("/api")
public class TasklRestApi {

    private final Logger log = LoggerFactory.getLogger(TasklRestApi.class);

    private static final String ENTITY_NAME = "tarea";

    @Autowired
    private TaskService tareaService;

    @PostMapping("/tarea")
    public ResponseEntity<Task> createTask(@RequestBody Task tarea) throws URISyntaxException {
        log.debug("REST request to save Task : {}", tarea);
        Task result = tareaService.save(tarea);
        return ResponseEntity.created(new URI("/api/tarea/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/tarea")
    public ResponseEntity<Task> updateTask(@RequestBody Task tarea) throws URISyntaxException {
        log.debug("REST request to update Task : {}", tarea);
        if (tarea.getId() == null) {
            return createTask(tarea);
        }
        Task result = tareaService.save(tarea);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tarea.getId().toString())).body(result);
    }

    @GetMapping("/tarea")
    public List<Task> listarTareas() {
        log.debug("REST request to get all Tasks");
        return tareaService.findAll();
    }

    @GetMapping("/tarea/{id}")
    public ResponseEntity<Task> getChannel(@PathVariable Integer id) {
        log.debug("REST request to get Channel : {}", id);
        Task task = tareaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(task));
    }

    @GetMapping("/tarea/estado/{estadoId}")
    public List<Task> listarTareasEstado(@PathVariable("estadoId") StatusTask statusTask) {
        log.debug("REST request to get all Tasks");
        return tareaService.findByStatusTask(statusTask);
    }

    @GetMapping("/tarea/count")
    public Long tareaCount() {
        log.debug("REST request to get all Tasks");
        return tareaService.count();
    }

    @GetMapping("/tarea/estado/{estadoId}/count")
    public Long countTaskStatusTask(@PathVariable("estadoId") StatusTask statusTask) {
        log.debug("REST request to get all Tasks");
        return tareaService.countByStatusTask(statusTask);
    }

    @DeleteMapping("/tarea/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        log.debug("REST request to delete Task : {}", id);
        tareaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
