/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.api;

import com.planning.api.entity.DireccionApi;
import com.planning.entity.Management;
import com.planning.service.ManagementService;
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
import java.util.stream.Collectors;

/**
 * REST controller for managing Management.
 */
@RestController
@RequestMapping("/api")
public class DireccionRestApi {
    
    private final Logger log = LoggerFactory.getLogger(DireccionRestApi.class);
    
    private static final String ENTITY_NAME = "direccion";
    
    @Autowired
    private ManagementService direccionService;
    
    @PostMapping("/direccion")
    public ResponseEntity<Management> createManagement(@RequestBody Management direccion) throws URISyntaxException {
        log.debug("REST request to save Management : {}", direccion);
        Management result = direccionService.save(direccion);
        return ResponseEntity.created(new URI("/api/direccion/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }
    
    @PutMapping("/direccion/{direccionId}")
    public ResponseEntity<Management> updateManagement(@RequestBody Management direccion, @PathVariable("direccionId") Management managementBd) {
        Management result = direccionService.save(direccion);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, direccion.getId().toString())).body(result);
    }
    
    @GetMapping("/direccion")
    public ResponseEntity<ModelMap> listarDirecciones(ModelMap map) {
        log.debug("REST request to get all Managements");
        List<DireccionApi> direccions = direccionService.findAll().parallelStream().map(DireccionApi::new).collect(Collectors.toList());
        map.put("success", true);
        map.put("direcciones", direccions);
        return ResponseEntity.ok(map);
    }
    
    @GetMapping("/direccion/{id}")
    public ResponseEntity<Management> getManagement(@PathVariable Integer id) {
        log.debug("REST request to get Management : {}", id);
        Management direccion = direccionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(direccion));
    }
    
    @DeleteMapping("/direccion/{id}")
    public ResponseEntity<Void> deleteManagement(@PathVariable Integer id) {
        log.debug("REST request to delete Management : {}", id);
        direccionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
