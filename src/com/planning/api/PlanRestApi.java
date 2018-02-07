/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.api;

import com.planning.entity.Plan;
import com.planning.service.PlanService;
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
 * REST controller for managing Plan.
 */
@RestController
@RequestMapping("/api")
public class PlanRestApi {

    private final Logger log = LoggerFactory.getLogger(PlanRestApi.class);

    private static final String ENTITY_NAME = "plan";

    @Autowired
    private PlanService planService;

    @PostMapping("/plan")
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) throws URISyntaxException {
        log.debug("REST request to save Plan : {}", plan);
        Plan result = planService.save(plan);
        return ResponseEntity.created(new URI("/api/plan/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/plan")
    public ResponseEntity<Plan> updatePlan(@RequestBody Plan plan) throws URISyntaxException {
        log.debug("REST request to update Plan : {}", plan);
        if (plan.getId() == null) {
            return createPlan(plan);
        }
        Plan result = planService.save(plan);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, plan.getId().toString())).body(result);
    }

    @GetMapping("/plan")
    public List<Plan> listarPlanes() {
        log.debug("REST request to get all Plans");
        return planService.findAll();
    }

    @GetMapping("/plan/{id}")
    public ResponseEntity<Plan> getPlan(@PathVariable Integer id) {
        log.debug("REST request to get Plan : {}", id);
        Plan plan = planService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(plan));
    }

    @DeleteMapping("/plan/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Integer id) {
        log.debug("REST request to delete Plan : {}", id);
        planService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
