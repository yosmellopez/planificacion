/*Generado por Disrupsoft*/
package com.planning.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import com.planning.diagram.*;
import com.planning.entity.*;
import com.planning.exception.OracleException;
import com.planning.service.*;
import com.planning.util.ArregloCreator;
import com.planning.util.JsonId;
import com.planning.util.MapeadorObjetos;
import com.planning.util.RestModelAndView;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.PersistenceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller object for domain model class Plan.
 *
 * @see Plan
 */
@RestController(value = "Planning.PlanController")
@RequestMapping("/plan")
@Api(description = "Exposes APIs to work with Plan resource.", value = "PlanController")
public class PlanController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanController.class);
    
    @Autowired
    private PlanService planService;
    
    @Autowired
    private StatusTaskService statusTaskService;
    
    @Autowired
    private PlTaskService plTaskService;
    
    @Autowired
    private PositionService positionService;
    
    @Autowired
    private ManagementService managementService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private CriticalyLevelService levelService;
    
    @Autowired
    private StatusPlanService statusPlanService;
    
    @Autowired
    private AreaService areaService;
    
    @Autowired
    private ChildTaskService childTaskService;
    
    @Autowired
    private GrupoRepository grupoRepository;
    
    @Autowired
    private MapeadorObjetos mapeadorObjetos;
    
    @RequestMapping(value = "/listarTodosPlanes", method = RequestMethod.GET)
    @ApiOperation(value = "Returns the list of Plan instances matching the search criteria.")
    public ModelAndView listarPlanes(@AuthenticationPrincipal Users u, ModelMap map) {
        LOGGER.debug("Rendering Plans list");
        List<Plan> planes = planService.findAll();
        for (Plan instance : planes) {
            Set<PlTask> tasks = new HashSet<>(plTaskService.findByPlan(instance));
            for (PlTask task : tasks) {
                MiTarea miTarea = new MiTarea();
                Task tarea = taskService.findOne(task.getTask().getId());
                miTarea.setCargoId(task.getTask().getPosition().getId());
                miTarea.setCodigo(tarea.getCode());
                miTarea.setCriticidad_id(task.getTask().getCriticalyLevels());
                miTarea.setEstado(task.getTask().getStatusTask().getName());
                miTarea.setGerenciaId(task.getTask().getPosition().getArea().getManagement().getId());
                miTarea.setNombre(tarea.getName());
                miTarea.setTareaId(tarea.getId());
                miTarea.setEsMia(Objects.equals(u.getPosition().getId(), task.getTask().getPosition().getId()));
                miTarea.setDescripcion(tarea.getDescription());
                miTarea.setCargo(task.getTask().getPosition().getName());
                miTarea.setFechaCreacion(tarea.getFechaCreacion());
                miTarea.setModelos(new ArrayList<>(tarea.getDocuments()));
                List<ChildTask> sucesoras = childTaskService.findByFromAndIsChild(task, true);
                for (ChildTask childTask : sucesoras) {
                    miTarea.getSucesoras().add(new Tarea(childTask.getFrom()));
                }
                if (Objects.equals(u.getPosition().getId(), task.getTask().getPosition().getId())) {
                    miTarea.setColor("#235c09");
                } else {
                    miTarea.setColor(task.getTask().getStatusTask().getColor());
                }
                instance.getTareas().add(miTarea);
            }
        }
        map.put("planes", planes);
        map.put("success", true);
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/salvarDiagrama", method = RequestMethod.POST)
    @ApiOperation(value = "Returns the list of Plan instances matching the search criteria.")
    public ModelAndView salvarDiagrama(@RequestParam("plan_id") Integer id, @RequestParam("diagrama") String diagrama, ModelMap map) {
        LOGGER.debug("Rendering Plans list");
        Plan plan = planService.findOne(id);
        plan.setDiagrama(diagrama);
        planService.saveAndFlush(plan);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/borrarDiagrama", method = RequestMethod.POST)
    @ApiOperation(value = "Returns the list of Plan instances matching the search criteria.")
    public ModelAndView borrarDiagrama(@RequestParam("plan_id") Integer id, ModelMap map) {
        LOGGER.debug("Rendering Plans list");
        Plan plan = planService.findOne(id);
        plan.setDiagrama(null);
        plan.setModeloAgrupado(null);
        planService.saveAndFlush(plan);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/listarPlanes", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "Returns the list of Plan instances.")
    public ModelAndView getPlans(String fechaInicio, String fechaFin, @RequestParam("estado_id") Integer estado, Boolean filtrar, Pageable pageable) throws ParseException {
        if (filtrar != null && filtrar) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (fechaInicio != null && !fechaInicio.isEmpty() && fechaFin != null && !fechaFin.isEmpty() && estado != null) {
                StatusPlan statusPlan = statusPlanService.findOne(estado);
                Date fechaInicial = dateFormat.parse(fechaInicio);
                Date fechaFinal = dateFormat.parse(fechaFin);
                Page<Plan> plans = planService.findByFechaCreacionBetweenAndStatusplanid(fechaInicial, fechaFinal, statusPlan, pageable);
                return new RestModelAndView(planes(plans));
            }
            if (fechaInicio != null && !fechaInicio.isEmpty() && fechaFin != null && !fechaFin.isEmpty()) {
                Date fechaInicial = dateFormat.parse(fechaInicio);
                Date fechaFinal = dateFormat.parse(fechaFin);
                Page<Plan> planes = planService.findByFechaCreacionBetween(fechaInicial, fechaFinal, pageable);
                return new RestModelAndView(planes(planes));
            }
            if (estado != null) {
                StatusPlan statusPlan = statusPlanService.findOne(estado);
                Page<Plan> planes = planService.findByStatusplanid(statusPlan, pageable);
                return new RestModelAndView(planes(planes));
            }
        }
        LOGGER.debug("Rendering Plans list");
        return new RestModelAndView(planes(planService.findAll(pageable)));
    }
    
    @RequestMapping(value = "/listarTodos", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "Returns the list of Plan instances.")
    public ModelAndView listarTodos(Pageable pageable) throws ParseException {
        LOGGER.debug("Rendering Plans list");
        return getPlans(null, null, null, false, pageable);
    }
    
    @RequestMapping(value = "/buscarTareas", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "Returns the list of Plan instances.")
    public ModelAndView buscarTareas(@RequestParam("plan_id") Integer id, Integer cargo, Integer area, Integer direccion, Integer criticidad, ModelMap map) {
        LOGGER.debug("Rendering Plans list");
        Plan plan = planService.findOne(id);
        if (cargo != null) {
            Position position = positionService.findOne(cargo);
            List<PlTask> plTasks = plTaskService.findByPlanAndTask_Position(plan, position);
            plan.setPlTasks(new HashSet<>(plTasks));
        } else if (area != null) {
            Area areaBd = areaService.findOne(area);
            List<PlTask> plTasks = plTaskService.findByPlanAndTask_Position_Area(plan, areaBd);
            plan.setPlTasks(new HashSet<>(plTasks));
        } else if (direccion != null) {
            Management management = managementService.findOne(direccion);
            List<PlTask> plTasks = plTaskService.findByPlanAndTask_Position_Area_Management(plan, management);
            plan.setPlTasks(new HashSet<>(plTasks));
        } else if (criticidad != null) {
            CriticalyLevel criticalyLevel = levelService.findOne(criticidad);
            List<PlTask> plTasks = plTaskService.findByPlanAndTask_CriticalyLevelsContains(plan, criticalyLevel);
            plan.setPlTasks(new HashSet<>(plTasks));
        }
        map.put("success", true);
        map.put("plan", plan);
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/buscarTareasPlan", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "Returns the list of Task instances.")
    public ModelAndView buscarTareas(@RequestParam("plan_id") Integer id, Integer cargo, Integer area, Integer direccion, Integer criticidad) {
        LOGGER.debug("Rendering Tasks list");
        Plan plan = planService.findOne(id);
        List<PlTask> plTasks = new ArrayList<>();
        if (criticidad != null) {
            CriticalyLevel criticalyLevel = levelService.findOne(criticidad);
            if (cargo != null) {
                Position position = positionService.findOne(cargo);
                plTasks = plTaskService.findByPlanAndTask_CriticalyLevelsContainsAndTask_Position(plan, criticalyLevel, position);
            } else if (area != null) {
                Area areaBd = areaService.findOne(area);
                plTasks = plTaskService.findByPlanAndTask_CriticalyLevelsContainsAndTask_Position_Area(plan, criticalyLevel, areaBd);
            } else if (direccion != null) {
                Management management = managementService.findOne(direccion);
                plTasks = plTaskService.findByPlanAndTask_CriticalyLevelsContainsAndTask_Position_Area_Management(plan, criticalyLevel, management);
            } else {
                plTasks = plTaskService.findByPlanAndTask_CriticalyLevelsContains(plan, criticalyLevel);
            }
        } else if (cargo != null) {
            Position position = positionService.findOne(cargo);
            plTasks = plTaskService.findByPlanAndTask_Position(plan, position);
        } else if (area != null) {
            Area areaBd = areaService.findOne(area);
            plTasks = plTaskService.findByPlanAndTask_Position_Area(plan, areaBd);
        } else if (direccion != null) {
            Management management = managementService.findOne(direccion);
            plTasks = plTaskService.findByPlanAndTask_Position_Area_Management(plan, management);
        } else {
            plTasks = plTaskService.findByPlan(plan);
        }
        List<Task> tareas = new ArrayList<>();
        plTasks.forEach((PlTask plTask) -> {
            tareas.add(plTask.getTask());
        });
        return new RestModelAndView(tareas(tareas));
    }
    
    @RequestMapping(value = "/listarTareasSinTarea", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "Returns the list of Task instances.")
    public ModelAndView listarTareasSinTarea(@AuthenticationPrincipal Users user, Pageable pageable) {
        LOGGER.debug("Rendering Tasks list");
        return new RestModelAndView(tareas(taskService.findAll(pageable), user));
    }
    
    private ModelMap tareas(Page<Task> page, Users user) {
        ModelMap map = new ModelMap();
        map.put("sEcho", page.getNumber());
        map.put("iTotalRecords", page.getTotalElements());
        map.put("iTotalDisplayRecords", page.getNumberOfElements());
        List<Task> content = page.getContent();
        ModelMap[] tasks = new ModelMap[content.size()];
        int cont = content.size();
        for (int i = 0; i < cont; i++) {
            tasks[i] = ArregloCreator.crearTareaMap(content.get(i), user);
        }
        map.put("aaData", tasks);
        return map;
    }
    
    private ModelMap tareas(List<Task> page) {
        ModelMap map = new ModelMap();
        map.put("sEcho", 0);
        map.put("iTotalRecords", page.size());
        map.put("iTotalDisplayRecords", page.size());
        ModelMap[] tasks = new ModelMap[page.size()];
        int cont = page.size();
        for (int i = 0; i < cont; i++) {
            tasks[i] = ArregloCreator.crearTareaPlanMap(page.get(i));
        }
        map.put("aaData", tasks);
        return map;
    }
    
    @RequestMapping(value = "/cargarDatos", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "Returns the Plan instance associated with the given id.")
    public ModelAndView cargarDatos(@RequestParam("plan_id") Integer id, ModelMap map) {
        Plan instance = planService.findOne(id);
        map.put("success", true);
        map.put("plan", ArregloCreator.cargarPlan(instance));
        map.put("niveles", levelService.findAll());
        LOGGER.debug("Plan details with id: {}", instance);
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/cargarDatosPlan", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "Returns the Plan instance associated with the given id.")
    public ModelAndView cargarDatosPlan(@RequestParam("plan_id") Integer id, ModelMap map, @AuthenticationPrincipal Users u) {
        Plan instance = planService.findOne(id);
        Set<PlTask> tasks = new HashSet<>(plTaskService.findByPlan(instance));
        List<MiTarea> tareas = instance.getTareas();
        for (PlTask task : tasks) {
            MiTarea miTarea = new MiTarea();
            Task tarea = taskService.findOne(task.getTask().getId());
            miTarea.setCargoId(task.getTask().getPosition().getId());
            miTarea.setCodigo(tarea.getCode());
            miTarea.setCriticidad_id(task.getTask().getCriticalyLevels());
            miTarea.setEstado(task.getTask().getStatusTask().getName());
            miTarea.setGerenciaId(task.getTask().getPosition().getArea().getManagement().getId());
            miTarea.setNombre(tarea.getName());
            miTarea.setPeso(task.getHeight());
            miTarea.setTareaId(tarea.getId());
            miTarea.setProducto(task.getTask().getProduct());
            miTarea.setEsMia(Objects.equals(u.getPosition().getId(), task.getTask().getPosition().getId()));
            miTarea.setDescripcion(tarea.getDescription());
            miTarea.setCargo(task.getTask().getPosition().getName());
            if (Objects.equals(u.getPosition().getId(), task.getTask().getPosition().getId())) {
                miTarea.setColor("#235c09");
            } else {
                miTarea.setColor(task.getTask().getStatusTask().getColor());
            }
            tareas.add(miTarea);
        }
        map.put("success", true);
        map.put("plan", instance);
        LOGGER.debug("Plan details with id: {}", instance);
        return new RestModelAndView(map);
    }
    
    private List<MiTarea> cargarTareas(Plan plan, boolean ordenado) {
        List<PlTask> tasks = plTaskService.findByPlan(plan);
        List<PlTask> tareasOrdenadas;
        if (ordenado) {
            tareasOrdenadas = tasks.stream().sorted((PlTask first, PlTask second) -> {
                return first.getPosition() > second.getPosition() ? 1 : first.getPosition() == second.getPosition() ? 0 : -1;
            }).collect(Collectors.toList());
        } else {
            tareasOrdenadas = new LinkedList<>(tasks);
        }
        List<MiTarea> tareas = new LinkedList<>();
        for (PlTask task : tareasOrdenadas) {
            MiTarea miTarea = new MiTarea();
            Task tarea = taskService.findOne(task.getTask().getId());
            miTarea.setCargoId(task.getTask().getPosition().getId());
            miTarea.setCodigo(tarea.getCode());
            miTarea.setPartida(task.isStart());
            miTarea.setPeso(task.getPosition());
            miTarea.setCriticidad_id(task.getTask().getCriticalyLevels());
            miTarea.setEstado(task.getTask().getStatusTask().getName());
            miTarea.setEstadoId(task.getTask().getStatusTask().getId());
            miTarea.setGerenciaId(task.getTask().getPosition().getArea().getManagement().getId());
            miTarea.setNombre(tarea.getName());
            miTarea.setProducto(task.getTask().getProduct());
            miTarea.setTareaId(tarea.getId());
            miTarea.setEsMia(false);
            miTarea.setDescripcion(tarea.getDescription());
            miTarea.setCargo(task.getTask().getPosition().getName());
            tareas.add(miTarea);
        }
        Collections.sort(tareas, (MiTarea a, MiTarea b) -> {
            return a.getPeso() < b.getPeso() ? -1 : a.getPeso() == b.getPeso() ? 0 : 1;
        });
        return tareas;
    }
    
    @RequestMapping(value = "/eliminarPlan", method = {RequestMethod.POST})
    @ApiOperation(value = "Deletes the Plan instance associated with the given id.")
    public ModelAndView deletePlan(@RequestParam("plan_id") Integer id, ModelMap map) {
        LOGGER.debug("Deleting Plan with id: {}", id);
        planService.delete(id);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        map.put("planes", planService.findAll());
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/tareasPlan", method = {RequestMethod.POST})
    @ApiOperation(value = "Deletes the Plan instance associated with the given id.")
    public ModelAndView tareasPlan(@RequestParam("plan_id") Integer id, @RequestParam("tarea_id") Integer idTarea, ModelMap map) {
        LOGGER.debug("Deleting Plan with id: {}", id);
        Plan plan = planService.findOne(id);
        map.put("success", true);
        map.put("tareas", cargarTareas(plan, false));
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/eliminarTarea", method = {RequestMethod.POST})
    @ApiOperation(value = "Deletes the Plan instance associated with the given id.")
    public ModelAndView eliminarTarea(@RequestParam("plan_id") Integer id, @RequestParam("tarea_id") Integer idTarea, ModelMap map) {
        LOGGER.debug("Deleting Plan with id: {}", id);
        Plan plan = planService.findOne(id);
        Task task = taskService.findOne(idTarea);
        PlTask plTask = plTaskService.findByPlanAndTask(plan, task);
        if (plTask != null) {
            Modelo modelo = null;
            if (plan.getDiagrama() != null && !plan.getDiagrama().isEmpty()) {
                modelo = mapeadorObjetos.readValue(plan.getDiagrama(), Modelo.class);
            }
            List<ChildTask> froms = childTaskService.findByFrom(plTask);
            if (!froms.isEmpty()) {
                for (ChildTask childTask : froms) {
                    childTaskService.deleteById(childTask.getId());
                    if (modelo != null) {
                        modelo.deleteEdge(new Edge("" + childTask.getFrom().getTask().getId(), "" + childTask.getTo().getTask().getId()));
                    }
                }
            }
            List<ChildTask> tos = childTaskService.findByTo(plTask);
            if (!tos.isEmpty()) {
                for (ChildTask childTask : tos) {
                    childTaskService.deleteById(childTask.getId());
                    if (modelo != null) {
                        modelo.deleteEdge(new Edge("" + childTask.getFrom().getTask().getId(), "" + childTask.getTo().getTask().getId()));
                    }
                }
            }
            if (modelo != null) {
                modelo.deleteNode(new Node(plTask));
                if (modelo.isModificado()) {
                    plan.setDiagrama(mapeadorObjetos.writeValueAsString(modelo));
                    planService.saveAndFlush(plan);
                }
            }
            plTaskService.delete(plTask.getId());
        }
        
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        map.put("tareas", cargarTareas(planService.findOne(plan.getId()), true));
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/cambiarTareaPartida", method = {RequestMethod.POST})
    @ApiOperation(value = "Deletes the Plan instance associated with the given id.")
    public ModelAndView cambiarTareaPartida(@RequestParam("plan_id") Integer id, @RequestParam("tarea_id") Integer idTarea, ModelMap map) {
        LOGGER.debug("Deleting Plan with id: {}", id);
        PlTask plTask = plTaskService.findByPlanAndTask(planService.findOne(id), taskService.findOne(idTarea));
        plTask.setStart(!plTask.isStart());
        plTaskService.saveAndFlush(plTask);
        Task task = plTask.getTask();
        task.setStart(plTask.isStart());
        taskService.saveAndFlush(task);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        map.put("tareas", cargarTareas(planService.findOne(id), false));
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/eliminarPlanes", method = {RequestMethod.POST})
    @ApiOperation(value = "Deletes the Plan instance associated with the given id.")
    public ModelAndView eliminarPlanes(@RequestBody JsonId jsonId, ModelMap map) {
        ArrayList<Integer> ids = jsonId.getIds();
        for (Integer id : ids) {
            planService.delete(id);
        }
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente");
        map.put("planes", planService.findAll());
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/cargarDatosDiagrama", method = RequestMethod.POST)
    @ApiOperation(value = "Updates the Plan instance associated with the given id.")
    public ModelAndView cargarDatosDiagrama(@RequestParam("plan_id") Integer id, @AuthenticationPrincipal Users u, ModelMap map) {
        Plan plan = planService.findOne(id);
        if (plan.getDiagrama() == null) {
            map.put("plan", construirDiagrama(plan, u));
        } else {
            map.put("plan", plan);
        }
        map.put("success", true);
        return new RestModelAndView(map);
    }
    
    private Diagrama construirDiagrama(Plan plan, Users u) {
        Diagrama diagrama = new Diagrama();
        List<PlTask> planTareas = plTaskService.findByPlan(plan);
        List<CriticalyLevel> criticalyLevels = levelService.findAll();
        for (CriticalyLevel level : criticalyLevels) {
            diagrama.addColumna(new Columna(level.getId(), level.getOrder(), level.getName(), level.getColor()));
        }
        List<Grupo> grupos = grupoRepository.findByPlan(plan);
        for (PlTask planTarea : planTareas) {
            Task task = planTarea.getTask();
            List<Task> taskAgrupadas = grupos.parallelStream().map(grupo -> grupo.getTaskAgrupada()).collect(Collectors.toList());
            Set<Task> setTaskGrupo = grupos.parallelStream().map(grupo -> grupo.getTaskGrupo()).collect(Collectors.toSet());
            planTarea.setStart(planTarea.isStart());
            Management management = task.getPosition().getArea().getManagement();
            diagrama.addSiders(new Sider(management.getId(), management.getName(), management.getOrder()));
            if (Objects.equals(task.getPosition().getId(), u.getPosition().getId())) {
                task.setColor("#10A259");
            }
            List<ChildTask> childTasks = childTaskService.findByFromAndIsChild(planTarea, true);
            for (ChildTask child : childTasks) {
                if (!taskAgrupadas.contains(child.getFrom().getTask()) && !taskAgrupadas.contains(child.getTo().getTask())) {
                    diagrama.addEdge(new Edge("" + child.getFrom().getTask().getId(), "" + child.getTo().getTask().getId()));
                }
            }
            if (setTaskGrupo.contains(task)) {
                List<Task> taskList = grupos.parallelStream().filter(grupo -> grupo.getTaskGrupo().equals(task)).map(grupo -> grupo.getTaskAgrupada()).collect(Collectors.toList());
                if (!taskList.isEmpty()) {
                    Punto point = calcularNivelesAlerta(taskList, planTarea);
                    diagrama.addTarea(new TareaDiagrama(planTarea, point, "red"));
                }
            } else {
                if (!taskAgrupadas.contains(task)) {
                    Set<CriticalyLevel> levels = task.getCriticalyLevels();
                    TreeSet<CriticalyLevel> arrayList = new TreeSet<>(levels);
                    int i = 0;
                    for (CriticalyLevel level : arrayList) {
                        if (planTarea.getTask().isTranversal()) {
                            diagrama.addTareaTranversal(new TareaDiagrama(planTarea, level.getId(), level.getOrder() + 1, level.getColor(), i++ == 0));
                            break;
                        }
                        diagrama.addTarea(new TareaDiagrama(planTarea, level.getId(), level.getOrder(), level.getColor(), i++ == 0));
                    }
                }
            }
        }
        diagrama.ordenarTareasTranversales();
        diagrama.setPlanId("" + plan.getId());
        diagrama.setDescripcion(plan.getName());
        diagrama.setDiagrama(plan.getDiagrama());
        return diagrama;
    }
    
    private Punto calcularNivelesAlerta(List<Task> taskList, PlTask plTask) {
        Set<CriticalyLevel> todosNiveles = new TreeSet<>();
        Set<CriticalyLevel> nivelesAlertaTarea = plTask.getTask().getCriticalyLevels();
        List<CriticalyLevel> nivelesOrdenados = nivelesAlertaTarea.parallelStream().sorted((o1, o2) -> o1.getOrder() > o2.getOrder() ? 1 : o1.getOrder() == o2.getOrder() ? 0 : -1).collect(Collectors.toList());
        for (Task task : taskList) {
            Set<CriticalyLevel> criticalyLevels = task.getCriticalyLevels();
            criticalyLevels.forEach(todosNiveles::add);
        }
        int columna = 0, colSpan = 0, idCriticidadInicial = 0, i = 0;
        int size = todosNiveles.size();
        boolean tengoInicio = false;
        if (!nivelesOrdenados.isEmpty()) {
            idCriticidadInicial = nivelesOrdenados.get(0).getId();
            columna = nivelesOrdenados.get(0).getOrder();
            tengoInicio = true;
        }
        for (CriticalyLevel level : todosNiveles) {
            if (i == 0 && !tengoInicio) {
                columna = level.getOrder() + 1;
                idCriticidadInicial = level.getId();
            }
            if (i == size - 1) {
                colSpan = level.getOrder();
            }
            i++;
        }
        colSpan -= columna;
        colSpan = colSpan < 0 ? 0 : colSpan;
        return new Punto(columna, colSpan, idCriticidadInicial);
    }
    
    @RequestMapping(value = "/salvarPlan", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new Plan instance.")
    public ModelAndView salvarPlan(@RequestBody Plan plan, ModelMap map) {
        LOGGER.debug("Create Plan with information: {}", plan);
        if (plan.getId() != null) {
            Plan planBd = planService.findOne(plan.getId());
            if ((plan.getDiagrama() == null || plan.getDiagrama().isEmpty()) && planBd.getDiagrama() != null && !planBd.getDiagrama().isEmpty()) {
                plan.setDiagrama(planBd.getDiagrama());
            }
        }
        planService.saveAndFlush(plan);
        
        List<MiTarea> tareas = plan.getTareas();
        for (MiTarea tarea : tareas) {
            PlTask plTask = plTaskService.findByPlanAndTask(plan, taskService.findOne(tarea.getTareaId()));
            if (plTask == null) {
                plTask = new PlTask();
                plTask.setPlan(plan);
                plTask.setTask(taskService.findOne(tarea.getTareaId()));
                plTask.getTask().setStatusTask(statusTaskService.findOne(tarea.getEstadoId()));
                plTask.setStart(tarea.isPartida());
                plTask.setName(tarea.getNombre());
                plTask.getTask().setPosition(positionService.findOne(tarea.getCargoId()));
                plTaskService.saveAndFlush(plTask);
            }
        }
        map.put("message", "La operación se realizó correctamente");
        map.put("success", true);
        map.put("planes", planService.findAll());
        return new RestModelAndView(map);
    }
    
    @RequestMapping(value = "/clonarPlan", method = RequestMethod.POST)
    @ApiOperation(value = "Clone a Plan instance.")
    public ModelAndView clonarPlan(@RequestParam("plan_id") Integer id, @RequestParam String nombre, ModelMap map) {
        Plan plan = planService.findOne(id);
        Set<PlTask> plTasks = new HashSet<>(plTaskService.findByPlan(plan));
        List<Grupo> grupos = grupoRepository.findByPlan(plan);
        plan.setId(null);
        plan.setName(nombre);
        plan.setPlTasks(new HashSet<>());
        planService.saveAndFlush(plan);
        LOGGER.debug("Create Plan with information: {}", plan);
        for (PlTask planTarea : plTasks) {
            List<ChildTask> childTasks = childTaskService.findByFromOrTo(planTarea, planTarea);
            HashSet<ChildTask> hashSet = new HashSet<>();
            planTarea.setId(null);
            planTarea.setPlan(plan);
            plTaskService.saveAndFlush(planTarea);
            for (ChildTask childTask : childTasks) {
                if (!hashSet.contains(childTask)) {
                    hashSet.add(childTask);
                    ChildTask clone = childTask.clonar();
                    childTaskService.saveAndFlush(clone);
                }
            }
        }
        for (Grupo grupo : grupos) {
            grupo.setPlan(plan);
            grupoRepository.save(grupo);
        }
        grupoRepository.flush();
        LOGGER.debug("Created Plan with information: {}", plan);
        map.put("message", "La operación se realizó correctamente");
        map.put("success", true);
        return new RestModelAndView(map);
    }
    
    private ModelMap planes(Page<Plan> page) {
        ModelMap map = new ModelMap();
        map.put("sEcho", page.getNumber());
        map.put("iTotalRecords", page.getTotalElements());
        map.put("iTotalDisplayRecords", page.getNumberOfElements());
        List<Plan> content = page.getContent();
        ModelMap[] tasks = new ModelMap[content.size()];
        int cont = content.size();
        for (int i = 0; i < cont; i++) {
            tasks[i] = ArregloCreator.crearPlanMap(content.get(i));
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
        } else if (e instanceof NullPointerException) {
            modelMap.put("error", "Excepción de valores perdidos");
        } else {
            OracleException oe = new OracleException(e);
            modelMap.put("error", oe.getMensaje());
        }
        modelMap.put("success", false);
        return new RestModelAndView(modelMap);
    }
    
    private String tratarMensaje(Throwable e) {
        String message = e.getMessage();
        if (e.getMessage().contains("unq_facultad_0")) {
            return "Ya existen estas siglas.";
        } else if (e.getMessage().contains("UNIQUE_FROM_TO")) {
            return "El nodo de partida no debe ser igual al de destino.";
        } else if (e.getMessage().contains("unq_facultad_1")) {
            return "Ya existe este nombre de facultad.";
        } else if (e.getMessage().contains("CHILDTASK_CHILDID_NULL")) {
            return "La sucesora debe contener tareas de origen.";
        } else if (e.getMessage().contains("CHILDTASK_ID_NULL")) {
            return "La sucesora debe contener tareas de destino.";
        } else if (e.getMessage().contains("CHILDTASK_PREDECESSOR_NULL")) {
            return "La sucesora debe definir si es sucesora o predecesora.";
        } else if (e.getMessage().contains("GRUPO_ID_NULL")) {
            return "La tarea que agrupa no debe ser vacía.";
        } else if (e.getMessage().contains("GRUPO_PLAN_NULL")) {
            return "El plan de la tarea agrupada no debe ser vacío.";
        } else if (e.getMessage().contains("GRUPO_TRASK_NULL")) {
            return "La tarea agrupada no debe estar vacía.";
        } else if (e.getMessage().contains("PL_TASK_ID_NULL")) {
            return "El identificador de tareas del plan no debe estar vacío.";
        } else if (e.getMessage().contains("PL_TASK_HEIGHT_NULL")) {
            return "La altura no debe estar vacía.";
        } else if (e.getMessage().contains("PL_TASK_IDPLAN_NULL")) {
            return "El plan no puede estar vacío en tareas del plan.";
        } else if (e.getMessage().contains("PL_TASK_IDTASK_NULL")) {
            return "La tarea no puede estar vacía en tareas del plan.";
        } else if (e.getMessage().contains("PL_TASK_NAME_NULL")) {
            return "El nombre de la tarea no puede estar vacío en tareas del plan.";
        } else if (e.getMessage().contains("PL_TASK_POSITION_NULL")) {
            return "La posición de la tarea no puede estar vacío en tareas del plan.";
        } else if (e.getMessage().contains("PL_TASK_POSX_NULL")) {
            return "La posición de la tarea no puede estar vacío en tareas del plan.";
        } else if (e.getMessage().contains("PL_TASK_POSY_NULL")) {
            return "La posición de la tarea no puede estar vacío en tareas del plan.";
        } else if (e.getMessage().contains("PL_TASK_RECURRENT_NULL")) {
            return "La posición de la tarea no puede estar vacío en tareas del plan.";
        } else if (e.getMessage().contains("UNIQUE_FROM_TO")) {
            return "No se puede insertar este vínculo porque ya existe.";
        } else if (e.getMessage().contains("fk_departamento_id_facultad")) {
            return "No se puede eliminar esta facultad porque contiene departamentos.";
        }
        OracleException oe = new OracleException(message);
        return oe.getMensaje();
    }
    
}
