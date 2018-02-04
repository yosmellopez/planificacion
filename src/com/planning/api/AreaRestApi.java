package com.planning.api;

import com.planning.entity.Area;
import com.planning.service.AreaService;
import com.planning.util.HeaderUtil;
import com.planning.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AreaRestApi {

    private final Logger log = LoggerFactory.getLogger(AreaRestApi.class);

    private static final String ENTITY_NAME = "area";

    @Autowired
    private AreaService areaService;

    @PostMapping("/unidad")
    public ResponseEntity<Area> createArea(@RequestBody Area unidad) throws URISyntaxException {
        log.debug("REST request to save Area : {}", unidad);
        Area result = areaService.save(unidad);
        return ResponseEntity.created(new URI("/api/unidad/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/unidad")
    public ResponseEntity<Area> updateArea(@RequestBody Area unidad) throws URISyntaxException {
        log.debug("REST request to update Area : {}", unidad);
        if (unidad.getId() == null) {
            return createArea(unidad);
        }
        Area result = areaService.save(unidad);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, unidad.getId().toString()))
                .body(result);
    }

    @GetMapping("/unidad")
    public List<Area> listarUnidades() {
        log.debug("REST request to get all Areas");
        return areaService.findAll();
    }

    @GetMapping("/unidad/{id}")
    public ResponseEntity<Area> getArea(@PathVariable Integer id) {
        log.debug("REST request to get Area : {}", id);
        Area unidad = areaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(unidad));
    }

    @DeleteMapping("/unidad/{id}")
    public ResponseEntity<Void> deleteArea(@PathVariable Integer id) {
        log.debug("REST request to delete Area : {}", id);
        areaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
