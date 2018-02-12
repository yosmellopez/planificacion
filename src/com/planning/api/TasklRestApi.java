/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.api;

import com.planning.api.entity.EstadoTarea;
import com.planning.api.entity.TareaApi;
import com.planning.entity.DashBoard;
import com.planning.entity.StatusTask;
import com.planning.entity.Task;
import com.planning.entity.Users;
import com.planning.service.*;
import com.planning.util.HeaderUtil;
import com.planning.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
    
    @Autowired
    private PositionService positionService;
    
    @Autowired
    private AreaService areaService;
    
    @Autowired
    private ManagementService managementService;
    
    @Autowired
    private StatusTaskService statusTaskService;
    
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
    public ResponseEntity<ModelMap> listarTareasEstado(@PathVariable("estadoId") StatusTask statusTask, Sort sort, @AuthenticationPrincipal Users user) {
        ModelMap map = new ModelMap();
        log.debug("REST request to get all Tasks");
        List<Task> tasks = tareaService.findByStatusTaskAndPosition(statusTask, user.getPosition(), sort);
        map.put("success", true);
        map.put("tareas", tasks.parallelStream().map(TareaApi::new).collect(Collectors.toList()));
        return ResponseEntity.ok(map);
    }
    
    @GetMapping("/tarea/search")
    public ResponseEntity<DashBoard> buscarTareas(@RequestParam Integer direccionId, @RequestParam Integer unidadId, @RequestParam Integer cargoId, @AuthenticationPrincipal Users user) {
        DashBoard dashBoard = new DashBoard();
        List<StatusTask> statusTasks = statusTaskService.findAll();
        TreeSet<EstadoTarea> estadoTareas = new TreeSet<>();
        int totalGeneral = 0;
        for (StatusTask statusTask : statusTasks) {
            int total = 0;
            EstadoTarea estadoTarea = new EstadoTarea(statusTask);
            estadoTareas.add(estadoTarea);
            List<Task> tasks;
            if (cargoId != null) {
                tasks = tareaService.findByPosition(positionService.findOne(cargoId));
            } else if (unidadId != null) {
                tasks = tareaService.findByPosition_Area(areaService.findOne(unidadId));
            } else if (direccionId != null) {
                tasks = tareaService.findByPosition_Area_Management(managementService.findOne(direccionId));
            } else {
                tasks = tareaService.findByPosition(user.getPosition());
            }
            total = (int) tasks.parallelStream().filter(task -> task.getStatusTask().equals(statusTask)).count();
            estadoTarea.setTotalTareas(total);
            totalGeneral += total;
        }
        dashBoard.setEstadoTareas(estadoTareas);
        dashBoard.setTotalTareas(totalGeneral);
        dashBoard.setTotalNotificaciones(0);
        dashBoard.setSuccess(true);
        return ResponseEntity.ok(dashBoard);
    }
    
    @GetMapping("/tarea/search/{estadoId}")
    public ResponseEntity<ModelMap> buscarTareasEstado(@RequestParam Integer direccionId, @RequestParam Integer unidadId, @RequestParam Integer cargoId, @PathVariable("estadoId") StatusTask statusTask, @AuthenticationPrincipal Users user) {
        ModelMap map = new ModelMap();
        List<Task> tasks;
        if (cargoId != null) {
            tasks = tareaService.findByPosition(positionService.findOne(cargoId));
        } else if (unidadId != null) {
            tasks = tareaService.findByPosition_Area(areaService.findOne(unidadId));
        } else if (direccionId != null) {
            tasks = tareaService.findByPosition_Area_Management(managementService.findOne(direccionId));
        } else {
            tasks = tareaService.findByPosition(user.getPosition());
        }
        List<TareaApi> taskList = tasks.parallelStream().filter(task -> task.getStatusTask().equals(statusTask)).map(TareaApi::new).collect(Collectors.toList());
        map.put("tareas", taskList);
        map.put("success", true);
        map.put("total", taskList.size());
        return ResponseEntity.ok(map);
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
