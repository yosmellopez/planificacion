/*Generado por Disrupsoft*/
package com.planning.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/
import com.planning.service.RolService;
import com.planning.service.UsersService;
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
import com.wavemaker.runtime.data.exception.EntityNotFoundException;
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
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller object for domain model class Rol.
 *
 * @see Rol
 */
@RestController(value = "Planning.RolController")
@RequestMapping("/perfil")
@Api(description = "Exposes APIs to work with Rol resource.", value = "RolController")
public class RolController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RolController.class);

    @Autowired
    private RolService rolService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ApiOperation(value = "Returns the list of Rol instances matching the search criteria.")
    public Page<Rol> findRols(Pageable pageable) {
        LOGGER.debug("Rendering Rols list");
        return rolService.findAll(pageable);
    }

    @RequestMapping(value = "/listarPerfil", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "Returns the list of Rol instances.")
    public ModelAndView getRols(Pageable pageable) {
        LOGGER.debug("Rendering Rols list");
        return new RestModelAndView(roles(rolService.findAll(pageable)));
    }

    @RequestMapping(value = "/listarTodosPerfiles", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "Returns the list of Rol instances.")
    public ModelAndView listarRoles(ModelMap map) {
        LOGGER.debug("Rendering Rols list");
        map.put("perfiles", rolService.findAll());
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/salvarPerfil", method = RequestMethod.POST)
    @ApiOperation(value = "Returns the Rol instance associated with the given id.")
    public ModelAndView salvarPerfil(@RequestBody Rol rol, ModelMap map) {
        LOGGER.debug("Getting Rol with id: {}", rol);
        Rol instance = rolService.saveAndFlush(rol);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        LOGGER.debug("Rol details with id: {}", instance);
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/eliminarPerfil", method = RequestMethod.POST)
    @ApiOperation(value = "Deletes the Rol instance associated with the given id.")
    public ModelAndView deleteRol(@RequestParam("perfil_id") Integer id, ModelMap map) {
        LOGGER.debug("Deleting Rol with id: {}", id);
        rolService.delete(id);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/eliminarPerfiles", method = RequestMethod.POST)
    @ApiOperation(value = "Deletes the Rol instance associated with the given id.")
    public ModelAndView eliminarPerfiles(@RequestBody JsonId ids, ModelMap map) {
        ArrayList<Integer> integers = ids.getIds();
        ArrayList<Rol> roles = new ArrayList<>();
        for (Integer id : integers) {
            try {
                rolService.delete(id);
            } catch (Exception e) {
                Rol rol = rolService.findOne(id);
                roles.add(rol);
            }
        }
        map.put("success", true);
        if (roles.isEmpty()) {
            map.put("message", "La operación se realizó correctamente");
        } else {
            String mensaje = "No se puedieron eliminar los siguientes roles porque contenian usuarios.";
            for (Rol rol : roles) {
                mensaje += "<p>" + rol.getName() + "</p>";
            }
            map.put("message", mensaje);
        }
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/cargarDatos", method = RequestMethod.POST)
    @ApiOperation(value = "Updates the Rol instance associated with the given id.")
    public ModelAndView cargarDatos(@RequestParam("perfil_id") Integer id, ModelMap map) throws EntityNotFoundException {
        map.put("success", true);
        map.put("perfil", rolService.findOne(id));
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new Rol instance.")
    public Rol createRol(@RequestBody Rol instance) {
        LOGGER.debug("Create Rol with information: {}", instance);
        instance = rolService.saveAndFlush(instance);
        LOGGER.debug("Created Rol with information: {}", instance);
        return instance;
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service
     */
    protected void setRolService(RolService service) {
        this.rolService = service;
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @ApiOperation(value = "Returns the total count of Rol instances.")
    public Long countAllRols() {
        LOGGER.debug("counting Rols");
        Long count = rolService.count();
        return count;
    }

    private ModelMap roles(Page<Rol> page) {
        ModelMap map = new ModelMap();
        map.put("sEcho", "1");
        map.put("iTotalRecords", page.getTotalElements());
        map.put("iTotalDisplayRecords", page.getNumberOfElements());
        List<Rol> content = page.getContent();
        ModelMap[] tasks = new ModelMap[content.size()];
        int cont = content.size();
        for (int i = 0; i < cont; i++) {
            tasks[i] = ArregloCreator.crearRolMap(content.get(i));
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
        String error = e.getMessage();
        if (e.getMessage().contains("unq_facultad_0")) {
            return "Ya existen estas siglas.";
        } else if (e.getMessage().contains("unq_facultad_1")) {
            return "Ya existe este nombre de facultad.";
        } else if (e.getMessage().contains("fk_1r2pil4o5xj68hkjfvgq3ahhb")) {
            return "No se puede eliminar este perfil porque contiene usuarios.";
        } else if (e.getMessage().contains("FK_USER_ROL")) {
            return "No se puede eliminar este perfil porque contiene usuarios.";
        }
        OracleException oe = new OracleException(error);
        return oe.getMensaje();
    }

}
