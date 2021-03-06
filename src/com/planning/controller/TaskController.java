/*Generado por Disrupsoft*/
package com.planning.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/
import com.planning.diagram.*;
import com.planning.entity.*;
import com.planning.exception.OracleException;
import com.planning.exception.TaskException;
import com.planning.filter.PlTaskFilter;
import com.planning.notification.NotificationManager;
import com.planning.service.*;
import com.planning.util.*;
import com.planning.util.Error;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Part;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller(value = "Planning.TaskController")
@RequestMapping("/tarea")
@Api(description = "Exposes APIs to work with Task resource.", value = "TaskController")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private PlanService planService;

    @Autowired
    private PlTaskService planTareaService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private CriticalyLevelService levelService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private StatusTaskService statusTaskService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private ChildTaskService childTaskService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private UserTokenService tokenService;

    @Autowired
    private MapeadorObjetos mapeadorObjetos;

    private Set<Antecesora> antecesoras;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/listarTodas", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "Returns the list of Task instances matching the search criteria.")
    public ModelAndView findTasks(Pageable pageable, ModelMap map) {
        LOGGER.debug("Rendering Tasks list");
        map.put("tareas", taskService.findAll());
        map.put("success", true);
        return RestModelAndView.ok(map);
    }

    @ApiOperation(value = "Returns the list of Task instances.")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @RequestMapping(value = "/listarTareas", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getTasks(@AuthenticationPrincipal Users user, Pageable pageable) {
        Page<Task> tareas = taskService.findAll(pageable);
        return new RestModelAndView(tareas(tareas, user));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/listarTableros", method = {RequestMethod.GET})
    @ApiOperation(value = "Returns the list of Task instances.")
    public ModelAndView listarTableros(PlTaskFilter filter, @AuthenticationPrincipal Users usuario, ModelMap map) {
        List<PlTask> tasks;
        Optional<Plan> planOptional = planService.findByEjecucion(true);
        boolean planEjecucion = planOptional.isPresent();
        if (!planEjecucion) {
            if (filter != null) {
                tasks = planTareaService.findAll(filter);
            } else {
                tasks = new ArrayList<>();
            }
        } else {
            Plan plan = planOptional.get();
            if (filter != null) {
                filter.setPlan(plan);
                tasks = planTareaService.findAll(filter);
            } else {
                if (usuario.getRol().getId() == 1) {
                    tasks = planTareaService.findByPlan(plan);
                } else {
                    if (usuario.isTitular()) {
                        Position position = usuario.getPosition();
                        Set<PlTask> tareas = new HashSet<>();
                        if (position.isDireccion()) {
                            Set<Management> direcciones = position.getDirecciones();
                            for (Management management : direcciones) {
                                if (planEjecucion) {
                                    List<PlTask> tareasDireccion = planTareaService.findByPlanAndPosition_Area_Management(plan, management);
                                    tareas.addAll(tareasDireccion);
                                }
                            }
                        } else if (position.isDireccion()) {
                            List<PlTask> tareasArea = planTareaService.findByPosition_Area(position.getArea());
                            tareas.addAll(tareasArea);
                        } else {
                            List<PlTask> tareasArea = planTareaService.findByPosition(position);
                            tareas.addAll(tareasArea);
                        }
                        tasks = new ArrayList<>(tareas);
                    } else {
                        tasks = planTareaService.findByPosition(usuario.getPosition());
                    }
                }
            }
        }
        MyBoard board = new MyBoard();
        List<StatusTask> statusTasks = statusTaskService.findAll(new Sort(Sort.Direction.ASC, "order"));
        for (StatusTask statusTask : statusTasks) {
            board.addColum(new Column(statusTask.getName(), statusTask.getId(), true, tasks.size(), statusTask.getColor()));
        }
        for (PlTask task : tasks) {
            board.addDato(new Datos(task.getId(), task.getStatusTask().getId(), task.getStatusTask().getName(), task.getDescription(), task.getStatusTask().getColor(), task.getId()));
            board.addRecurso(new Recurso(task.getId(), task.getPosition().getName(), "", Boolean.FALSE, task.getName(), new Date(), task.getUsuario() == null ? "" : task.getUsuario().getNombreCompleto(), task.fechaActual(), task.getStatusTask().getId()));
        }
        map.put("success", true);
        map.put("board", board);
        LOGGER.debug("Rendering Tasks list");
        return new RestModelAndView(map);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @RequestMapping(value = "/listarTableros/{planId}", method = {RequestMethod.GET})
    @ApiOperation(value = "Returns the list of Task instances.")
    public ModelAndView listarTablerosCurrentUser(@PathVariable("planId") Plan planSeleccionado, @AuthenticationPrincipal Users usuario, ModelMap map) {
        List<PlTask> tasks;
        Optional<Plan> planOptional = planService.findByEjecucion(true);
        boolean planEjecucion = planOptional.isPresent();
        if (!planEjecucion) {
            tasks = planTareaService.findByPlan(planSeleccionado);
        } else {
            Plan plan = planOptional.get();
            if (usuario.getRol().getId() == 1) {
                tasks = planTareaService.findByPlan(plan);
            } else {
                if (usuario.isTitular()) {
                    Position position = usuario.getPosition();
                    Set<PlTask> tareas = new HashSet<>();
                    if (position.isDireccion()) {
                        Set<Management> direcciones = position.getDirecciones();
                        for (Management management : direcciones) {
                            if (planEjecucion) {
                                List<PlTask> tareasDireccion = planTareaService.findByPlanAndPosition_Area_Management(plan, management);
                                tareas.addAll(tareasDireccion);
                            }
                        }
                    } else if (position.isDireccion()) {
                        List<PlTask> tareasArea = planTareaService.findByPosition_Area(position.getArea());
                        tareas.addAll(tareasArea);
                    } else {
                        List<PlTask> tareasArea = planTareaService.findByPosition(position);
                        tareas.addAll(tareasArea);
                    }
                    tasks = new ArrayList<>(tareas);
                } else {
                    tasks = planTareaService.findByPosition(usuario.getPosition());
                }
            }
        }
        MyBoard board = new MyBoard();
        List<StatusTask> statusTasks = statusTaskService.findAll(new Sort(Sort.Direction.ASC, "order"));
        for (StatusTask statusTask : statusTasks) {
            board.addColum(new Column(statusTask.getName(), statusTask.getId(), true, tasks.size(), statusTask.getColor()));
        }
        for (PlTask task : tasks) {
            board.addDato(new Datos(task.getId(), task.getStatusTask().getId(), task.getStatusTask().getName(), task.getDescription(), task.getStatusTask().getColor(), task.getId()));
            board.addRecurso(new Recurso(task.getId(), task.getPosition().getName(), "", Boolean.FALSE, task.getName(), new Date(), task.getUsuario() == null ? "" : task.getUsuario().getNombreCompleto(), task.fechaActual(), task.getStatusTask().getId()));
        }
        map.put("success", true);
        map.put("board", board);
        LOGGER.debug("Rendering Tasks list");
        return new RestModelAndView(map);
    }

    private ModelAndView buscarTablero(Plan planSeleccionado, @AuthenticationPrincipal Users usuario, ModelMap map) {
        List<PlTask> tasks;
        Optional<Plan> planOptional = planService.findByEjecucion(true);
        boolean planEjecucion = planOptional.isPresent();
        if (!planEjecucion) {
            tasks = planTareaService.findByPlan(planSeleccionado);
        } else {
            Plan plan = planOptional.get();
            if (usuario.getRol().getId() == 1) {
                tasks = planTareaService.findByPlan(plan);
            } else {
                if (usuario.isTitular()) {
                    Position position = usuario.getPosition();
                    Set<PlTask> tareas = new HashSet<>();
                    if (position.isDireccion()) {
                        Set<Management> direcciones = position.getDirecciones();
                        for (Management management : direcciones) {
                            if (planEjecucion) {
                                List<PlTask> tareasDireccion = planTareaService.findByPlanAndPosition_Area_Management(plan, management);
                                tareas.addAll(tareasDireccion);
                            }
                        }
                    } else if (position.isDireccion()) {
                        List<PlTask> tareasArea = planTareaService.findByPosition_Area(position.getArea());
                        tareas.addAll(tareasArea);
                    } else {
                        List<PlTask> tareasArea = planTareaService.findByPosition(position);
                        tareas.addAll(tareasArea);
                    }
                    tasks = new ArrayList<>(tareas);
                } else {
                    tasks = planTareaService.findByPosition(usuario.getPosition());
                }
            }
        }
        MyBoard board = new MyBoard();
        List<StatusTask> statusTasks = statusTaskService.findAll(new Sort(Sort.Direction.ASC, "order"));
        for (StatusTask statusTask : statusTasks) {
            board.addColum(new Column(statusTask.getName(), statusTask.getId(), true, tasks.size(), statusTask.getColor()));
        }
        for (PlTask task : tasks) {
            board.addDato(new Datos(task.getId(), task.getStatusTask().getId(), task.getStatusTask().getName(), task.getDescription(), task.getStatusTask().getColor(), task.getId()));
            board.addRecurso(new Recurso(task.getId(), task.getPosition().getName(), "", Boolean.FALSE, task.getName(), new Date(), task.getUsuario() == null ? "" : task.getUsuario().getNombreCompleto(), task.fechaActual(), task.getStatusTask().getId()));
        }
        map.put("board", board);
        return new RestModelAndView(map);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping(value = "/listarTablerosUsuario")
    public ModelAndView listarTablerosUsuario(PlTaskFilter filter, Boolean equipo, @AuthenticationPrincipal Users usuario, ModelMap map) {
        Optional<Plan> planOptional = planService.findByEjecucion(true);
        List<PlTask> tareasUsuario = new ArrayList<>();
        if (planOptional.isPresent()) {
            tareasUsuario = planTareaService.findByUsuarioAndPlan(usuario, planOptional.get());
        } else {
            if (filter != null) {
                planTareaService.findByPlan(filter.getPlan());
            }
        }
        if (equipo) {
            if (usuario.isTitular()) {
                Position position = usuario.getPosition();
                if (position.isDeArea()) {
                    tareasUsuario = planTareaService.findByPlanAndPosition_Area(planOptional.orElse(filter.getPlan()), position.getArea());
                } else if (position.isDireccion()) {
                    Set<Management> direcciones = position.getDirecciones();
                    for (Management direccion : direcciones) {
                        tareasUsuario.addAll(planTareaService.findByPlanAndPosition_Area_Management(planOptional.orElse(filter.getPlan()), direccion));
                    }
                } else {
                    tareasUsuario = planTareaService.findByPlanAndPosition(planOptional.orElse(filter.getPlan()), position);
                }
            }
        } else {
            tareasUsuario = planTareaService.findByPlanAndPosition(planOptional.orElse(filter.getPlan()), usuario.getPosition());
        }
        MyBoard board = new MyBoard();
        List<StatusTask> statusTasks = statusTaskService.findAll(new Sort(Sort.Direction.ASC, "order"));
        for (StatusTask statusTask : statusTasks) {
            board.addColum(new Column(statusTask.getName(), statusTask.getId(), true, tareasUsuario.size(), statusTask.getColor()));
        }
        for (PlTask task : tareasUsuario) {
            board.addDato(new Datos(task.getId(), task.getStatusTask().getId(), task.getStatusTask().getName(), task.getDescription(), task.getStatusTask().getColor(), task.getId()));
            board.addRecurso(new Recurso(task.getId(), task.getPosition().getName(), "", Boolean.FALSE, task.getName(), new Date(), task.getUsuario() == null ? "" : task.getUsuario().getNombreCompleto(), task.fechaActual(), task.getStatusTask().getId()));
        }
        map.put("success", true);
        map.put("board", board);
        LOGGER.debug("Rendering Tasks list");
        return RestModelAndView.ok(map);
    }

    @RequestMapping(value = "/buscarTareas", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "Returns the list of Task instances.")
    public ModelAndView buscarTareas(Integer cargo, Integer area, Integer direccion, Integer criticidad, @AuthenticationPrincipal Users users, ModelMap map) {
        if (criticidad != null) {
            CriticalyLevel level = levelService.findOne(criticidad);
            if (cargo != null) {
                Position position = positionService.findOne(cargo);
                Page<PlTask> tasks = planTareaService.findByCriticalyLevelsContainsAndPosition(level, position, new PageRequest(0, Integer.MAX_VALUE));
                return RestModelAndView.ok(planTareas(tasks, users));
            } else if (area != null) {
                Area areaBd = areaService.findOne(area);
                Page<PlTask> tasks = planTareaService.findByCriticalyLevelsContainsAndPosition_Area(level, areaBd, new PageRequest(0, Integer.MAX_VALUE));
                return RestModelAndView.ok(planTareas(tasks, users));
            } else if (direccion != null) {
                Management management = managementService.findOne(direccion);
                Page<PlTask> tasks = planTareaService.findByCriticalyLevelsContainsAndPosition_Area_Management(level, management, new PageRequest(0, Integer.MAX_VALUE));
                return RestModelAndView.ok(planTareas(tasks, users));
            } else {
                Page<PlTask> tasks = planTareaService.findByCriticalyLevelsContains(levelService.findOne(criticidad), new PageRequest(0, Integer.MAX_VALUE));
                return RestModelAndView.ok(planTareas(tasks, users));
            }
        } else if (cargo != null) {
            Page<PlTask> tasks = planTareaService.findByPosition(positionService.findOne(cargo), new PageRequest(0, Integer.MAX_VALUE));
            return RestModelAndView.ok(planTareas(tasks, users));
        } else if (area != null) {
            Page<PlTask> tasks = planTareaService.findByPosition_Area(areaService.findOne(area), new PageRequest(0, Integer.MAX_VALUE));
            return RestModelAndView.ok(planTareas(tasks, users));
        } else if (direccion != null) {
            Page<PlTask> tasks = planTareaService.findByPosition_Area_Management(managementService.findOne(direccion), new PageRequest(0, Integer.MAX_VALUE));
            return RestModelAndView.ok(planTareas(tasks, users));
        } else {
            if (users.isTitular()) {
                Position position = users.getPosition();
                Set<PlTask> tareas = new HashSet<>();
                if (position.isDireccion()) {
                    Set<Management> direcciones = position.getDirecciones();
                    for (Management management : direcciones) {
                        List<PlTask> tareasDireccion = planTareaService.findByPosition_Area_Management(management);
                        tareas.addAll(tareasDireccion);
                    }
                } else if (position.isDireccion()) {
                    List<PlTask> tareasArea = planTareaService.findByPosition_Area(position.getArea());
                    tareas.addAll(tareasArea);
                } else {
                    List<PlTask> tareasArea = planTareaService.findByPosition(position);
                    tareas.addAll(tareasArea);
                }
                return new RestModelAndView(planTareas(tareas, users));
            } else {
                return new RestModelAndView(planTareas(planTareaService.findByPosition(users.getPosition(), new PageRequest(0, Integer.MAX_VALUE)), users));
            }
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @RequestMapping(value = "/moverTarea/{idTarea}/{idEstado}", method = {RequestMethod.POST})
    public ModelAndView moverTarea(@PathVariable("idTarea") PlTask task, @PathVariable("idEstado") StatusTask statusTask, @AuthenticationPrincipal Users usuario, ModelMap map) {
        Position position = task.getPosition();
        Plan plan = task.getPlan();
        if (!plan.isEjecucion()) {
            map.put("success", false);
            map.put("error", "La Tarea \"" + task.getName() + "\" no se puede mover porque esta plan no está activo");
            return buscarTablero(plan, usuario, map);
        }
        switch (task.getStatusTask().getId()) {
            case 1:
                if (usuario.getRol().getId() == 1 && statusTask.getId() == 3) {
                    task.setStatusTask(statusTask);
                    planTareaService.saveAndFlush(task);
                    break;
                } else {
                    map.put("success", false);
                    map.put("error", "La Tarea \"" + task.getName() + "\" la puede disponibilizar solo el administrador");
                    return buscarTablero(plan, usuario, map);
                }
            case 2:
                if (usuario.isTitular() && statusTask.getId() == 4) {
                    if (task.getPosition().equals(usuario.getPosition())) {
                        task.setStatusTask(statusTask);
                        planTareaService.saveAndFlush(task);
                        Set<PlTask> plTasks = buscarSucesoras(task);
                        establecerNotificacion(position, task, statusTask, "Nueva notificación de tarea ejecutada");
                        Optional<StatusTask> optional = statusTaskService.findByName("Disponible");
                        if (optional.isPresent()) {
                            for (PlTask plTask : plTasks) {
                                plTask.setStatusTask(optional.get());
                                planTareaService.saveAndFlush(plTask);
                                establecerNotificacion(position, plTask, optional.get(), "Nueva notificación de tarea disponible");
                            }
                        }
                    } else {
                        map.put("success", false);
                        map.put("error", "La Tarea \"" + task.getName() + "\" solo puede ser asignada por el cargo correspondiente");
                        return buscarTablero(plan, usuario, map);
                    }
                } else {
                    map.put("success", false);
                    map.put("error", "La Tarea \"" + task.getName() + "\" solo puede ser asignada por el cargo correspondiente");
                    return buscarTablero(plan, usuario, map);
                }
                break;
            case 3:
                if (usuario.getRol().getId() == 1 && statusTask.getId() == 4) {
                    task.setStatusTask(statusTask);
                    planTareaService.saveAndFlush(task);
                    Date fecha = new Date();
                    task.setFechaAsignacion(fecha);
                    task.setUsuario(usuario);
                    establecerNotificacion(position, task, statusTask, "Nueva notificación de tarea asignada");
                    break;
                } else if (usuario.isTitular() && statusTask.getId() == 2 || usuario.getRol().getId() == 1) {
                    if (task.getPosition().equals(usuario.getPosition()) || usuario.getRol().getId() == 1) {
                        task.setStatusTask(statusTask);
                        planTareaService.saveAndFlush(task);
                        Date fecha = new Date();
                        task.setFechaAsignacion(fecha);
                        task.setUsuario(usuario);
                        establecerNotificacion(position, task, statusTask, "Nueva notificación de tarea asignada");
                        break;
                    } else {
                        map.put("success", false);
                        map.put("error", "La Tarea \"" + task.getName() + "\" solo puede ser asignada por el cargo correspondiente");
                        return buscarTablero(plan, usuario, map);
                    }
                } else {
                    map.put("success", false);
                    map.put("error", "La Tarea \"" + task.getName() + "\" solo puede ser asignada por el cargo correspondiente");
                    return buscarTablero(plan, usuario, map);
                }
            case 4:
                if (!task.isIsrecurrent()) {
                    map.put("success", false);
                    map.put("error", "La Tarea \"" + task.getName() + "\" está en estado de ejecutada, no se puede cambiar de estado");
                    return buscarTablero(plan, usuario, map);
                } else {
                    task.setStatusTask(statusTask);
                    planTareaService.saveAndFlush(task);
                }
                break;
        }
        LOGGER.debug("Rendering Tasks list");
        map.put("success", true);
        map.put("subirArchivo", true);
        map.put("message", "Tarea \"" + task.getName() + "\" movida exitosamente");
        return new RestModelAndView(map);
    }

    private void establecerNotificacion(Position position, PlTask task, StatusTask statusTask, String titulo) {
        List<UserToken> tokens = tokenService.findByUserPosition(position);
        List<String> playerIds = tokens.parallelStream().filter(userToken -> userToken.getPlayerId() != null && !userToken.getPlayerId().isEmpty()).map(UserToken::getPlayerId).collect(Collectors.toList());
        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(new Date());
        notificacion.setPosition(position);
        notificacion.setDescription("La tarea \"" + task.getName() + "\" ha cambiado de estado a " + statusTask.getName());
        notificacion.setTitle("Nueva notificación de tarea realizada");
        notificacionService.save(notificacion);
        boolean b = enviarNotificacion(notificacion, playerIds);
        LOGGER.debug(b + "");
    }

    private boolean enviarNotificacion(Notificacion notificacion, List<String> playerIds) {
        NotificationManager manager = new NotificationManager();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic MTYzNDAxNzEtZTIwYi00NDY5LTk4NDgtZjJkNzAyZTJjMzE4");
        Map<String, Object> body = new HashMap<>();
        body.put("app_id", "a3ca4ae3-ba22-4004-a14f-7bfe95af0f3e");
        body.put("included_segments", new String[]{"All"});
        Map<String, String> data = new HashMap<>();
        data.put("elementos", "Nueva notificacion.");
        body.put("data", data);
        Map<String, String> contents = new HashMap<>();
        contents.put("es", notificacion.getDescription());
        contents.put("en", notificacion.getDescription());
        body.put("contents", contents);
        Map<String, String> headings = new HashMap<>();
        headings.put("es", notificacion.getTitle());
        headings.put("en", notificacion.getTitle());
        body.put("headings", headings);
        try {
            HashMap<String, Object> hashMap = manager.postForObject("https://onesignal.com/api/v1/notifications", body, HashMap.class, headers);
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
            }
        } catch (Exception e) {
            LOGGER.warn(e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    private Set<PlTask> buscarSucesoras(PlTask plTask) {
        Set<PlTask> plTasks = new HashSet<>();
        List<ChildTask> childTasks = childTaskService.findByFrom(plTask.getId());
        for (ChildTask childTask : childTasks) {
            PlTask task = planTareaService.findOne(childTask.getTo());
            plTasks.add(task);
        }
        return plTasks;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Returns the list of Task instances.")
    @RequestMapping(value = "/listarTareasSinTarea/{idTarea}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView listarTareasSinTarea(@PathVariable Integer idTarea, @AuthenticationPrincipal Users user) {
        LOGGER.debug("Rendering Tasks list");
        ModelMap map = new ModelMap();
        map.put("success", true);
        map.put("tareas", taskService.findAll());
        return new RestModelAndView(map);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/listarTareasParaImportar", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "Returns the list of Task instances.")
    public ModelAndView listarTareasParaImportar() {
        LOGGER.debug("Rendering Tasks list");
        List<Task> taskList = taskService.findAll();
        return new RestModelAndView(tareasImportar(taskList));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/cargarDatos/{tareaId}", method = {RequestMethod.POST})
    @ApiOperation(value = "Returns the Task instance associated with the given id.")
    public ModelAndView cargarDatos(@PathVariable("tareaId") Task task, ModelMap map) {
        map.put("success", true);
        map.put("tarea", ArregloCreator.cargarTarea(task, task.getDocuments()));
        return new RestModelAndView(map);
    }

    private boolean existeAntecesora(Antecesora antecesora) {
        if (antecesoras.isEmpty()) {
            return false;
        }
        for (Antecesora ant : antecesoras) {
            if (ant.getId().equals(antecesora.getId())) {
                return true;
            }
        }
        return false;
    }

    @RequestMapping(value = "/eliminarTarea", method = RequestMethod.POST)
    @ApiOperation(value = "Deletes the Task instance associated with the given id.")
    public ModelAndView eliminarTarea(@RequestParam("tarea_id") Integer id, ModelMap map) {
        LOGGER.debug("Deleting Task with id: {}", id);
        taskService.delete(id);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/eliminarTareas", method = RequestMethod.POST)
    @ApiOperation(value = "Deletes the Task instance associated with the given id.")
    public ModelAndView eliminarTareas(@RequestBody JsonId ids, ModelMap map) {
        ArrayList<Integer> integers = ids.getIds();
        for (Integer id : integers) {
            taskService.delete(id);
        }
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/eliminarModelo", method = RequestMethod.POST)
    @ApiOperation(value = "Deletes the Task instance associated with the given ids.")
    public ModelAndView eliminarModelo(@RequestParam("modelo_id") Integer id, ModelMap map) {
        Document document = documentService.findOne(id);
        documentService.delete(document);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @RequestMapping(value = "/salvarTarea", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new Task instance.")
    public ModelAndView salvarTarea(@RequestBody Task tarea, ModelMap map) {
        taskService.saveAndFlush(tarea);
        Set<Document> documents = tarea.getDocuments();
        if (!documents.isEmpty()) {
            for (Document document : documents) {
                if (document.getTask() == null) {
                    document.setTask(tarea);
                    document.setUltimo(false);
                    documentService.saveAndFlush(document);
                }
            }
        }
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        map.put("tarea", tarea);
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/salvarTarea/{tareaId}", method = RequestMethod.PUT)
    @ApiOperation(value = "Creates a new Task instance.")
    public ModelAndView salvarTarea(@RequestBody Task tarea, @PathVariable("tareaId") Task tareaBd, ModelMap map) {
        LOGGER.debug("Create Task with information: {}", tarea);
        tareaBd.clonarDatos(tarea);
        taskService.saveAndFlush(tareaBd);
        LOGGER.debug("Created Task with information: {}", tareaBd);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        map.put("tarea", tareaBd);
        return new RestModelAndView(map);
    }

    @PostMapping(value = "/documentos")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Returns the list of Task instances.")
    public ModelAndView tareasDocumentos() {
        LOGGER.debug("Rendering Tasks list");
        List<Task> plTaskList = taskService.findAll();
        return RestModelAndView.ok(documentosTareas(plTaskList));
    }

    private ModelMap documentosTareas(Collection<Task> content) {
        ModelMap map = new ModelMap();
        map.put("sEcho", content.size());
        map.put("iTotalRecords", content.size());
        map.put("iTotalDisplayRecords", content.size());
        ArrayList<ModelMap> tasks = new ArrayList<>();
        for (Task task : content) {
            tasks.add(ArregloCreator.crearTareaDocumentosMap(task));
        }
        map.put("aaData", tasks);
        return map;
    }

    @RequestMapping(value = "/salvarModelo", method = RequestMethod.POST)
    public ModelAndView insertarArchivo(@RequestPart Part file, @RequestParam String descripcion, @RequestParam Boolean radioestado, @RequestParam Integer tareaId) throws IOException {
        ModelMap map = new ModelMap();
        String nombreArchivo = file.getSubmittedFileName();
        Date hoy = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        String fecha = dateFormat.format(hoy);
        file.write(nombreArchivo + fecha);
        List<Document> list = documentService.findByDocpath(nombreArchivo);
        if (list.isEmpty()) {
            Document document = new Document(nombreArchivo, descripcion, true, radioestado != null ? radioestado : false);
            if (tareaId != 0) {
                Task task = taskService.findOne(tareaId);
                document.setTask(task);
            }
            document.setUltimo(true);
            documentService.saveAndFlush(document);
            map.put("success", true);
            map.put("modelo", document);
        } else {
            map.put("success", false);
            map.put("error", "Ya existe este nombre de archivo.");
        }
        return new RestModelAndView(map);
    }

    @RequestMapping(value = "/salvarModeloTarea", method = RequestMethod.POST)
    public ModelAndView guardarArchivo(@RequestPart Part file, @RequestParam String descripcion, @RequestParam Integer tareaId, @RequestParam Integer documentId) throws IOException {
        ModelMap map = new ModelMap();
        String nombreArchivo = file.getSubmittedFileName();
        Date hoy = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        String fecha = dateFormat.format(hoy);
        file.write(nombreArchivo + fecha);
        Document document = documentService.findOne(documentId);
        Task task = taskService.findOne(tareaId);
        document.setDocpath(nombreArchivo);
        document.setDescription(descripcion);
        document.setTask(task);
        documentService.saveAndFlush(document);
        map.put("success", true);
        map.put("modelo", document);
        return new RestModelAndView(map);
    }

    private ModelMap tareas(Page<Task> page, Users usuario) {
        ModelMap map = new ModelMap();
        map.put("sEcho", page.getNumber());
        map.put("iTotalRecords", page.getTotalElements());
        map.put("iTotalDisplayRecords", page.getNumberOfElements());
        List<Task> content = page.getContent();
        ModelMap[] tasks = new ModelMap[content.size()];
        int cont = content.size();
        for (int i = 0; i < cont; i++) {
            tasks[i] = ArregloCreator.crearTareaMap(content.get(i), usuario);
        }
        map.put("aaData", tasks);
        return map;
    }

    private ModelMap planTareas(Page<PlTask> page, Users usuario) {
        ModelMap map = new ModelMap();
        map.put("sEcho", page.getNumber());
        map.put("iTotalRecords", page.getTotalElements());
        map.put("iTotalDisplayRecords", page.getNumberOfElements());
        List<PlTask> content = page.getContent();
        ModelMap[] tasks = new ModelMap[content.size()];
        int cont = content.size();
        for (int i = 0; i < cont; i++) {
            tasks[i] = ArregloCreator.crearTareaMap(content.get(i), usuario);
        }
        map.put("aaData", tasks);
        return map;
    }

    private ModelMap planTareas(Set<PlTask> page, Users usuario) {
        ModelMap map = new ModelMap();
        map.put("sEcho", page.size());
        map.put("iTotalRecords", page.size());
        map.put("iTotalDisplayRecords", page.size());
        ArrayList<ModelMap> tasks = new ArrayList<>();
        for (PlTask task : page) {
            tasks.add(ArregloCreator.crearTareaMap(task, usuario));
        }
        map.put("aaData", tasks);
        return map;
    }

    private Map<String, ?> tareasImportar(List<Task> tasks) {
        ModelMap map = new ModelMap();
        map.put("sEcho", "1");
        map.put("iTotalRecords", tasks.size());
        map.put("iTotalDisplayRecords", tasks.size());
        ModelMap[] modelMaps = new ModelMap[tasks.size()];
        int cont = tasks.size();
        for (int i = 0; i < cont; i++) {
            modelMaps[i] = ArregloCreator.crearTareaImportarMap(tasks.get(i), i + 1);
        }
        map.put("aaData", modelMaps);
        return map;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> tratarExcepcion(Exception e) {
        LOGGER.warn(e.getLocalizedMessage(), e);
        OracleException oracleException = new TaskException(e);
        return ResponseEntity.ok(oracleException.getError());
    }
}
