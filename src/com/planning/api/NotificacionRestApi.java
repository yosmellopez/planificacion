/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.api;

import com.planning.entity.Notificacion;
import com.planning.service.NotificacionService;
import com.planning.util.HeaderUtil;
import com.planning.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Notificacion.
 */
@RestController
@RequestMapping("/api")
public class NotificacionRestApi {
    
    private final Logger log = LoggerFactory.getLogger(NotificacionRestApi.class);
    
    private static final String ENTITY_NAME = "Notificacion";
    
    @Autowired
    private NotificacionService notificacionService;
    
    @PostMapping("/notificacion")
    public ResponseEntity<ModelMap> createNotificacion(@RequestBody Notificacion notificacion, ModelMap map) throws URISyntaxException {
        log.debug("REST request to save Notificacion : {}", notificacion);
        Notificacion result = notificacionService.save(notificacion);
        map.put("notificaciones", result);
        map.put("success", true);
        return ResponseEntity.created(new URI("/api/notificacion/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(map);
    }
    
    @PutMapping("/notificacion")
    public ResponseEntity<ModelMap> updateNotificacion(@RequestBody Notificacion notificacion, ModelMap map) throws URISyntaxException {
        log.debug("REST request to update Notificacion : {}", notificacion);
        if (notificacion.getId() == null) {
            return createNotificacion(notificacion, map);
        }
        Notificacion result = notificacionService.save(notificacion);
        map.put("notificaciones", result);
        map.put("success", true);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notificacion.getId().toString())).body(map);
    }
    
    @GetMapping("/notificacion")
    public ResponseEntity<ModelMap> listarNotificaciones(ModelMap map) {
        log.debug("REST request to get all Notificacions");
        List<Notificacion> notificacions = notificacionService.findAll();
        map.put("success", true);
        map.put("notificaciones", notificacions);
        return ResponseEntity.ok(map);
    }
    
    @GetMapping("/notificacion/{id}")
    public ResponseEntity<Notificacion> getNotificacion(@PathVariable Long id) {
        log.debug("REST request to get Notificacion : {}", id);
        Notificacion notificacion = notificacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notificacion));
    }
    
    @PutMapping("/notificacion/{id}")
    public ResponseEntity<ModelMap> leerNotificacion(@PathVariable Notificacion notificacion, ModelMap map) {
        log.debug("REST request to get Notificacion : {}", notificacion);
        notificacion.setLeido(true);
        notificacionService.saveAndFlush(notificacion);
        map.put("notificacion", notificacion);
        map.put("success", true);
        return ResponseEntity.ok(map);
    }
    
    @DeleteMapping("/notificacion/{id}")
    public ResponseEntity<Void> deleteNotificacion(@PathVariable Long id) {
        log.debug("REST request to delete Notificacion : {}", id);
        notificacionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
