/*Generado por Disrupsoft*/
package com.planning.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/
import com.planning.service.CriticalyLevelService;
import com.planning.util.OracleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wordnik.swagger.annotations.*;
import com.planning.entity.*;
import com.planning.util.ArregloCreator;
import com.planning.util.JsonId;
import com.planning.util.RestModelAndView;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;

import java.util.ArrayList;

import java.util.List;
import javax.persistence.PersistenceException;

import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller object for domain model class CriticalyLevel.
 *
 * @see CriticalyLevel
 */
@RestController(value = "Planning.CriticalyLevelController")
@RequestMapping(value = {"/criticidad-tarea", "criticidad-plan"})
@Api(description = "Exposes APIs to work with CriticalyLevel resource.", value = "CriticalyLevelController")
public class CriticalyLevelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CriticalyLevelController.class);

    @Autowired
    private CriticalyLevelService criticalyLevelService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ApiOperation(value = "Returns the list of CriticalyLevel instances matching the search criteria.")
    public Page<CriticalyLevel> findCriticalyLevels(Pageable pageable, @RequestBody QueryFilter[] queryFilters) {
        LOGGER.debug("Rendering CriticalyLevels list");
        return criticalyLevelService.findAll(pageable);
    }

    @RequestMapping(value = "/listarTodos", method = RequestMethod.GET)
    @ApiOperation(value = "Returns the list of CriticalyLevel instances.")
    public ModelAndView getCriticalyLevels(ModelMap map) {
        LOGGER.debug("Rendering CriticalyLevels list");
        map.put("success", true);
        map.put("criticidades", criticalyLevelService.findAll(new Sort(Sort.Direction.ASC, "order", "id")));
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/listarCriticidad", method = RequestMethod.POST)
    @ApiOperation(value = "Returns the list of CriticalyLevel instances.")
    public ModelAndView listarCriticidad(Pageable pageable) {
        LOGGER.debug("Rendering CriticalyLevels list");
        return new RestModelAndView(criticidad(criticalyLevelService.findAll(pageable)));
    }

    @RequestMapping(value = "/salvarCriticidad", method = RequestMethod.POST)
    @ApiOperation(value = "Returns the CriticalyLevel instance associated with the given id.")
    public ModelAndView salvarCriticidad(@RequestBody CriticalyLevel instance, ModelMap map) {
        criticalyLevelService.saveAndFlush(instance);
        LOGGER.debug("CriticalyLevel details with id: {}", instance);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/eliminarCriticidad", method = RequestMethod.POST)
    @ApiOperation(value = "Deletes the CriticalyLevel instance associated with the given id.")
    public ModelAndView eliminarCriticidad(@RequestParam("criticidad_id") Integer id, ModelMap map) {
        LOGGER.debug("Deleting CriticalyLevel with id: {}", id);
        criticalyLevelService.delete(id);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/eliminarCriticidades", method = RequestMethod.POST)
    @ApiOperation(value = "Deletes the CriticalyLevel instance associated with the given id.")
    public ModelAndView eliminarCriticidades(@RequestBody JsonId ids, ModelMap map) {
        ArrayList<Integer> integers = ids.getIds();
        for (Integer id : integers) {
            criticalyLevelService.delete(id);
        }
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/cargarDatos", method = RequestMethod.POST)
    @ApiOperation(value = "Updates the CriticalyLevel instance associated with the given id.")
    public ModelAndView cargarDatos(@RequestParam("criticidad_id") Integer id, ModelMap map) {
        map.put("success", true);
        map.put("criticidad", criticalyLevelService.findOne(id));
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new CriticalyLevel instance.")
    public CriticalyLevel createCriticalyLevel(@RequestBody CriticalyLevel instance) {
        LOGGER.debug("Create CriticalyLevel with information: {}", instance);
        instance = criticalyLevelService.saveAndFlush(instance);
        LOGGER.debug("Created CriticalyLevel with information: {}", instance);
        return instance;
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "Returns the total count of CriticalyLevel instances.")
    public Long countAllCriticalyLevels() {
        LOGGER.debug("counting CriticalyLevels");
        Long count = criticalyLevelService.count();
        return count;
    }

    private ModelMap criticidad(Page<CriticalyLevel> page) {
        ModelMap map = new ModelMap();
        map.put("sEcho", "1");
        map.put("iTotalRecords", page.getTotalElements());
        map.put("iTotalDisplayRecords", page.getNumberOfElements());
        List<CriticalyLevel> content = page.getContent();
        ModelMap[] tasks = new ModelMap[content.size()];
        int cont = content.size();
        for (int i = 0; i < cont; i++) {
            tasks[i] = ArregloCreator.crearPlanCriticidadMap(content.get(i));
        }
        map.put("aaData", tasks);
        return map;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView tratarExcepcion(Exception e) {
        LOGGER.warn(e.getLocalizedMessage(), e);
        ModelMap modelMap = new ModelMap();
        if (e instanceof JpaSystemException) {
            JpaSystemException jse = (JpaSystemException) e;
            modelMap.put("error", tratarMensaje(jse.getMostSpecificCause()));
        } else if (e instanceof PersistenceException) {
            JpaSystemException exception = new JpaSystemException((PersistenceException) e);
            modelMap.put("error", tratarMensaje(exception.getMostSpecificCause()));
        } else if (e instanceof DataIntegrityViolationException) {
            DataIntegrityViolationException exception = (DataIntegrityViolationException) e;
            modelMap.put("error", tratarMensaje(exception.getMostSpecificCause()));
        } else if (e instanceof SQLGrammarException) {
            SQLGrammarException exception = (SQLGrammarException) e;
            modelMap.put("error", tratarMensaje(exception.getCause()));
        } else {
            OracleException oe = new OracleException(e);
            modelMap.put("error", oe.getMessage());
        }
        modelMap.put("success", false);
        return new RestModelAndView(modelMap);
    }

    private String tratarMensaje(Throwable e) {
        String message = e.getMessage();
        if (e.getMessage().contains("unq_facultad_0")) {
            return "Ya existen estas siglas.";
        } else if (e.getMessage().contains("task")) {
            return "No se puede eliminar porque este nivel de alerta porque contiene tareas.";
        } else if (e.getMessage().contains("FK_LEVEL_TASK")) {
            return "No se puede eliminar este nivel de alerta porque contiene tareas.";
        } else if (e.getMessage().contains("fk_departamento_id_facultad")) {
            return "No se puede eliminar esta facultad porque contiene departamentos.";
        }
        OracleException oe = new OracleException(message);
        return oe.getMensaje();
    }

}
