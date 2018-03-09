/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.api;

import com.planning.api.entity.NotificacionApi;
import com.planning.entity.Notificacion;
import com.planning.entity.NotificacionLeida;
import com.planning.entity.ReadNotificationPK;
import com.planning.entity.Users;
import com.planning.service.NotificacionLeidaService;
import com.planning.service.NotificacionService;
import com.planning.util.HeaderUtil;
import com.planning.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
    
    @Autowired
    private NotificacionLeidaService leidaService;
    
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
    public ResponseEntity<ModelMap> listarNotificaciones(ModelMap map, @AuthenticationPrincipal Users user) {
        log.debug("REST request to get all Notificacions");
        List<Notificacion> notificacions = notificacionService.findByPosition(user.getPosition());
        List<NotificacionApi> notificaciones = new ArrayList<>();
        for (Notificacion notificacion : notificacions) {
            Optional<NotificacionLeida> optional = leidaService.findByNotificacionAndUser(notificacion, user);
            if (!optional.isPresent()) {
                ReadNotificationPK notificationPK = new ReadNotificationPK(notificacion.getId(), user.getId());
                NotificacionLeida notificacionLeida = new NotificacionLeida();
                notificacionLeida.setLeido(false);
                notificacionLeida.setUser(user);
                notificacionLeida.setNotificacion(notificacion);
                notificacionLeida.setNotificationPK(notificationPK);
                leidaService.saveAndFlush(notificacionLeida);
                notificaciones.add(new NotificacionApi(notificacion, false));
            } else {
                NotificacionLeida notificacionLeida = optional.get();
                notificaciones.add(new NotificacionApi(notificacion, notificacionLeida.isLeido()));
            }
        }
        map.put("success", true);
        map.put("notificaciones", notificaciones);
        return ResponseEntity.ok(map);
    }
    
    @GetMapping("/notificacion/{id}")
    public ResponseEntity<Notificacion> getNotificacion(@PathVariable Long id) {
        log.debug("REST request to get Notificacion : {}", id);
        Notificacion notificacion = notificacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notificacion));
    }
    
    @PostMapping("/notificacion/{id}")
    public ResponseEntity<ModelMap> leerNotificacion(@PathVariable("id") Notificacion notificacion, @AuthenticationPrincipal Users user, ModelMap map) {
        Optional<NotificacionLeida> optional = leidaService.findByNotificacionAndUser(notificacion, user);
        log.debug("REST request to get Notificacion : {}", notificacion);
        if (optional.isPresent()) {
            NotificacionLeida notificacionLeida = optional.get();
            notificacionLeida.setLeido(true);
            leidaService.saveAndFlush(notificacionLeida);
        }
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
