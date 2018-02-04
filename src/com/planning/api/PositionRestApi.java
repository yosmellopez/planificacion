/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.api;

import com.planning.entity.Position;
import com.planning.service.PositionService;
import com.planning.util.HeaderUtil;
import com.planning.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * REST controller for managing Position.
 */
@RestController
@RequestMapping("/api")
public class PositionRestApi {

    private final Logger log = LoggerFactory.getLogger(PositionRestApi.class);

    private static final String ENTITY_NAME = "cargo";

    @Autowired
    private PositionService cargoService;

    @PostMapping("/cargo")
    public ResponseEntity<Position> createPosition(@RequestBody Position cargo) throws URISyntaxException {
        log.debug("REST request to save Position : {}", cargo);
        Position result = cargoService.save(cargo);
        return ResponseEntity.created(new URI("/api/cargo/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/cargo")
    public ResponseEntity<Position> updatePosition(@RequestBody Position cargo) throws URISyntaxException {
        log.debug("REST request to update Position : {}", cargo);
        if (cargo.getId() == null) {
            return createPosition(cargo);
        }
        Position result = cargoService.save(cargo);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cargo.getId().toString()))
                .body(result);
    }

    @GetMapping("/cargo")
    public List<Position> listarCargos() {
        log.debug("REST request to get all Positions");
        return cargoService.findAll();
    }

    @GetMapping("/cargo/{id}")
    public ResponseEntity<Position> getPosition(@PathVariable Integer id) {
        log.debug("REST request to get Position : {}", id);
        Position cargo = cargoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cargo));
    }

    @DeleteMapping("/cargo/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Integer id) {
        log.debug("REST request to delete Position : {}", id);
        cargoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
