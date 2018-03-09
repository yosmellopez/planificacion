/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.api;

import com.planning.api.entity.EstadoTarea;
import com.planning.api.entity.TareaApi;
import com.planning.entity.*;
import com.planning.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * REST controller for managing Task.
 */
@RestController
@RequestMapping("/api")
public class TasklRestApi {

    private final Logger log = LoggerFactory.getLogger(TasklRestApi.class);

    @Autowired
    private PlTaskService plTaskService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private StatusTaskService statusTaskService;

    @PostMapping("/tarea/{tareaId}/{estadoId}")
    public ResponseEntity<ModelMap> createTask(@RequestPart Part file, @PathVariable("tareaId") PlTask tarea, @PathVariable("estadoId") StatusTask statusTask, ModelMap map) throws IOException {
        System.out.println("Llego al paso 1");
        String nombreArchivo = file.getSubmittedFileName();
        System.out.println("Llego al paso 2");
        file.write(nombreArchivo);
        List<Document> list = documentService.findByDocpath(nombreArchivo);
        if (list.isEmpty()) {
            Document document = new Document();
            document.setDocpath(nombreArchivo);
            document.setDescription("");
            document.setUltimo(true);
            documentService.saveAndFlush(document);
            map.put("success", true);
            map.put("modelo", document);
        } else {
            map.put("success", false);
            map.put("error", "Ya existe este nombre de archivo.");
        }
        tarea.setStatusTask(statusTask);
        plTaskService.saveAndFlush(tarea);
        map.put("success", true);
        map.put("message", "Documento guardado exitosamente.");
        return ResponseEntity.ok(map);
    }

    @PutMapping("/tarea/{tareaId}/{estadoId}")
    public ResponseEntity<ModelMap> editarTask(@PathVariable("tareaId") PlTask tarea, @PathVariable("estadoId") StatusTask statusTask, ModelMap map) {
        tarea.setStatusTask(statusTask);
        plTaskService.saveAndFlush(tarea);
        log.debug("REST request to save Task : {}", tarea);
        map.put("success", true);
        map.put("message", "Tarea guardada exitosamente.");
        return ResponseEntity.ok(map);
    }

    @GetMapping("/tarea/estado/{estadoId}")
    public ResponseEntity<ModelMap> listarTareasEstado(@PathVariable("estadoId") StatusTask statusTask, Sort sort, @AuthenticationPrincipal Users user) {
        ModelMap map = new ModelMap();
        log.debug("REST request to get all Tasks");
        List<PlTask> tasks = plTaskService.findByStatusTaskAndPosition(statusTask, user.getPosition(), sort);
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
            List<PlTask> tasks;
            if (cargoId != null) {
                tasks = plTaskService.findByPosition(positionService.findOne(cargoId));
            } else if (unidadId != null) {
                tasks = plTaskService.findByPosition_Area(areaService.findOne(unidadId));
            } else if (direccionId != null) {
                tasks = plTaskService.findByPosition_Area_Management(managementService.findOne(direccionId));
            } else {
                tasks = plTaskService.findByPosition(user.getPosition());
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
        List<PlTask> tasks;
        if (cargoId != null) {
            tasks = plTaskService.findByPosition(positionService.findOne(cargoId));
        } else if (unidadId != null) {
            tasks = plTaskService.findByPosition_Area(areaService.findOne(unidadId));
        } else if (direccionId != null) {
            tasks = plTaskService.findByPosition_Area_Management(managementService.findOne(direccionId));
        } else {
            tasks = plTaskService.findByPosition(user.getPosition());
        }
        List<TareaApi> taskList = tasks.parallelStream().filter(task -> task.getStatusTask().equals(statusTask)).map(TareaApi::new).collect(Collectors.toList());
        map.put("tareas", taskList);
        map.put("success", true);
        map.put("total", taskList.size());
        return ResponseEntity.ok(map);
    }

    @GetMapping("/tarea/estado/{estadoId}/count")
    public Long countTaskStatusTask(@PathVariable("estadoId") StatusTask statusTask) {
        log.debug("REST request to get all Tasks");
        return plTaskService.countByStatusTask(statusTask);
    }
}
