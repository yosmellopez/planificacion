/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.api;

import com.planning.api.entity.EstadoTarea;
import com.planning.entity.StatusTask;
import com.planning.service.StatusTaskService;
import com.planning.util.HeaderUtil;
import com.planning.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * REST controller for managing StatusTask.
 */
@RestController
@RequestMapping("/api")
public class StatusTaskRestApi {
    
    private final Logger log = LoggerFactory.getLogger(StatusTaskRestApi.class);
    
    private static final String ENTITY_NAME = "Estado Tarea";
    
    @Autowired
    private StatusTaskService estadoTareaService;
    
    @GetMapping("/estadoTarea")
    public TreeSet<EstadoTarea> listarCanales() {
        log.debug("REST request to get all StatusTasks");
        TreeSet<EstadoTarea> estadoTareas = new TreeSet<>(estadoTareaService.findAll().parallelStream().map(EstadoTarea::new).collect(Collectors.toSet()));
        return estadoTareas;
    }
    
    @PostMapping("/estadoTarea")
    public ResponseEntity<StatusTask> createStatusTask(@RequestBody StatusTask estadoTarea) throws URISyntaxException {
        log.debug("REST request to save StatusTask : {}", estadoTarea);
        StatusTask result = estadoTareaService.save(estadoTarea);
        return ResponseEntity.created(new URI("/api/estadoTarea/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }
    
    @PutMapping("/estadoTarea")
    public ResponseEntity<StatusTask> updateStatusTask(@RequestBody StatusTask estadoTarea) throws URISyntaxException {
        log.debug("REST request to update StatusTask : {}", estadoTarea);
        if (estadoTarea.getId() == null) {
            return createStatusTask(estadoTarea);
        }
        StatusTask result = estadoTareaService.save(estadoTarea);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, estadoTarea.getId().toString()))
                .body(result);
    }
    
    
    @GetMapping("/estadoTarea/{id}")
    public ResponseEntity<StatusTask> getStatusTask(@PathVariable Integer id) {
        log.debug("REST request to get StatusTask : {}", id);
        StatusTask estadoTarea = estadoTareaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(estadoTarea));
    }
    
    @DeleteMapping("/estadoTarea/{id}")
    public ResponseEntity<Void> deleteStatusTask(@PathVariable Integer id) {
        log.debug("REST request to delete StatusTask : {}", id);
        estadoTareaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
