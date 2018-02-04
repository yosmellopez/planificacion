/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.api;

import com.planning.entity.Channel;
import com.planning.service.ChannelService;
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
 * REST controller for managing Channel.
 */
@RestController
@RequestMapping("/api")
public class ChannelRestApi {

    private final Logger log = LoggerFactory.getLogger(ChannelRestApi.class);

    private static final String ENTITY_NAME = "canal";

    @Autowired
    private ChannelService canalService;

        @PostMapping("/canal")
    public ResponseEntity<Channel> createChannel(@RequestBody Channel canal) throws URISyntaxException {
        log.debug("REST request to save Channel : {}", canal);
        Channel result = canalService.save(canal);
        return ResponseEntity.created(new URI("/api/canal/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/canal")
    public ResponseEntity<Channel> updateChannel(@RequestBody Channel canal) throws URISyntaxException {
        log.debug("REST request to update Channel : {}", canal);
        if (canal.getId() == null) {
            return createChannel(canal);
        }
        Channel result = canalService.save(canal);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, canal.getId().toString()))
                .body(result);
    }

    @GetMapping("/canal")
    public List<Channel> listarCanales() {
        log.debug("REST request to get all Channels");
        return canalService.findAll();
    }

    @GetMapping("/canal/{id}")
    public ResponseEntity<Channel> getChannel(@PathVariable Integer id) {
        log.debug("REST request to get Channel : {}", id);
        Channel canal = canalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(canal));
    }

    @DeleteMapping("/canal/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable Integer id) {
        log.debug("REST request to delete Channel : {}", id);
        canalService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
