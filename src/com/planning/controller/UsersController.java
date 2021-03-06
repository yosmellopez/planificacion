/*Generado por Disrupsoft*/
package com.planning.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import com.planning.entity.Area;
import com.planning.entity.Management;
import com.planning.entity.UserToken;
import com.planning.entity.Users;
import com.planning.exception.OracleException;
import com.planning.service.*;
import com.planning.util.ArregloCreator;
import com.planning.util.JsonId;
import com.planning.util.RestModelAndView;
import com.wordnik.swagger.annotations.ApiOperation;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/usuario", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
public class UsersController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private AreaService areaService;
    
    @Autowired
    private ManagementService managementService;
    
    @Autowired
    private UserTokenService userTokenService;
    
    @Autowired
    private PositionService positionService;
    
    @RequestMapping(value = "/listarUsuario", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "Returns the list of Users instances.")
    public ModelAndView getUsers(Pageable pageable) {
        LOGGER.debug("Rendering Userss list");
        return new RestModelAndView(usuarios(usersService.findAll(pageable)));
    }
    
    @GetMapping(value = "/listarUsuarios")
    @ApiOperation(value = "Returns the list of Users instances.")
    public ModelAndView listarUsuarios(ModelMap map) {
        LOGGER.debug("Rendering Userss list");
        map.put("usuarios", usersService.findAll());
        map.put("success", true);
        return RestModelAndView.ok(map);
    }
    
    @RequestMapping(value = "/listarTitulares", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "Returns the list of Users instances.")
    public ModelAndView listarTitulares(ModelMap map) {
        List<Users> titulares = usersService.findByTitular(true);
        map.put("titulares", titulares);
        map.put("success", true);
        return RestModelAndView.ok(map);
    }
    
    @RequestMapping(value = {"/insertarUsuario"}, method = RequestMethod.POST)
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @ApiOperation(value = "Returns the Users instance associated with the given id.")
    public ModelAndView insertarUsuario(@RequestBody Users user, ModelMap map) {
        if (user.getKeypass().isEmpty() && user.getId() != null) {
            Users userBd = usersService.findOne(user.getId());
            user.setKeypass(userBd.getKeypass());
        }
        Users instance = usersService.saveAndFlush(user);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        LOGGER.debug("Users details with id: {}", instance);
        return new RestModelAndView(map);
    }
    
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @RequestMapping(value = {"/guardarUsuario/{usuarioId}"}, method = RequestMethod.PUT)
    @ApiOperation(value = "Returns the Users instance associated with the given id.")
    public ModelAndView guardarUsuario(@RequestBody Users user, @PathVariable("usuarioId") Users userBd, ModelMap map) {
        if (user.getKeypass().isEmpty()) {
            user.setKeypass(userBd.getKeypass());
        }
        Users instance = usersService.saveAndFlush(user);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        LOGGER.debug("Users details with id: {}", instance);
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/cargarDatos", method = RequestMethod.POST)
    @ApiOperation(value = "Returns the Users instance associated with the given id.")
    public ModelAndView getUsers(@RequestParam("usuario_id") Integer id, ModelMap map) {
        LOGGER.debug("Getting Users with id: {}", id);
        Users u = usersService.findOne(id);
        map.put("success", true);
        map.put("usuario", ArregloCreator.cargarUsuario(u, areaService.findByManagement(u.getPosition().getArea().getManagement()), positionService.findByArea(u.getPosition().getArea())));
        LOGGER.debug("Users details with id: {}", u);
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/eliminarUsuario", method = RequestMethod.POST)
    @ApiOperation(value = "Deletes the Users instance associated with the given id.")
    public ModelAndView deleteUsers(@RequestParam("usuario_id") Integer id, ModelMap map) {
        LOGGER.debug("Deleting Users with id: {}", id);
        usersService.delete(id);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/eliminarUsuarios", method = RequestMethod.POST)
    @ApiOperation(value = "Deletes the Users instance associated with the given id.")
    public ModelAndView eliminarUsuarios(@RequestBody JsonId ids, ModelMap map) {
        ArrayList<Integer> integers = ids.getIds();
        for (Integer id : integers) {
            usersService.delete(id);
        }
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/activarUsuario", method = RequestMethod.POST)
    @ApiOperation(value = "Updates the Users instance associated with the given id.")
    public ModelAndView activarUsuario(@RequestParam("usuario_id") Integer id, ModelMap map) {
        Users user = usersService.findOne(id);
        user.setActive(!user.isActive());
        user = usersService.saveAndFlush(user);
        LOGGER.debug("Users details with id: {}", user);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/olvidoContrasenna", method = RequestMethod.POST)
    @ApiOperation(value = "Updates the Users instance associated with the given id.")
    public ModelAndView olvidoContrasenna(@RequestParam("email") String email, ModelMap map) {
        Users user = usersService.findByUsuarioOrEmail(email, email);
        user.setActive(!user.isActive());
        user = usersService.saveAndFlush(user);
        LOGGER.debug("Users details with id: {}", user);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente. Espere atentamente su correo");
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/actualizarMisDatos", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new Users instance.")
    public ModelAndView actualizarMisDatos(@ModelAttribute Users instance, ModelMap map) {
        LOGGER.debug("Create Users with information: {}", instance);
        instance = usersService.saveAndFlush(instance);
        LOGGER.debug("Created Users with information: {}", instance);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/player/{usuarioId}", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new Users instance.")
    public ModelAndView guardarDatosPlayer(@PathVariable("usuarioId") Users user, @RequestParam("playerId") String playerId, ModelMap map) {
        Optional<UserToken> optional = userTokenService.findByPlayerId(playerId);
        if (!optional.isPresent()) {
            UserToken userToken = new UserToken("Google Chrome", playerId, user);
            userTokenService.saveAndFlush(userToken);
        }
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
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
        if (message.contains("unq_facultad_0")) {
            return "Ya existen estas siglas.";
        } else if (message.contains("correo_unico")) {
            return "Ya existe este correo está asigado a un usuario.";
        } else if (message.contains("SYS_C0012422")) {
            return "La secuencia de usuarios no está correctamente configurada.";
        } else if (message.contains("fk_departamento_id_facultad")) {
            return "No se puede eliminar esta facultad porque contiene departamentos.";
        }
        OracleException oe = new OracleException(message);
        return oe.getMensaje();
    }
    
    private ModelMap usuarios(Page<Users> page) {
        ModelMap map = new ModelMap();
        map.put("sEcho", "1");
        map.put("iTotalRecords", page.getTotalElements());
        map.put("iTotalDisplayRecords", page.getNumberOfElements());
        List<Users> content = page.getContent();
        ModelMap[] tasks = new ModelMap[content.size()];
        int cont = content.size();
        for (int i = 0; i < cont; i++) {
            tasks[i] = ArregloCreator.crearUsersMap(content.get(i));
        }
        map.put("aaData", tasks);
        return map;
    }
}
