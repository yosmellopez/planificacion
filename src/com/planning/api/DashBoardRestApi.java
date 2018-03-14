package com.planning.api;

import com.planning.api.entity.EstadoTarea;
import com.planning.entity.*;
import com.planning.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class DashBoardRestApi {

    @Autowired
    private StatusTaskService statusTaskService;

    @Autowired
    private PlTaskService plTaskService;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private NotificacionLeidaService leidaService;

    @Autowired
    private PlanService planService;

    @GetMapping(value = "/dashboard/stats")
    public ResponseEntity<DashBoard> dashboard(@AuthenticationPrincipal Users user) {
        DashBoard dashBoard = new DashBoard();
        Optional<Plan> optional = planService.findByEjecucion(true);
        dashBoard.setTotalTareas(0);
        if (optional.isPresent()) {
            dashBoard.setPlan(optional.get());
            dashBoard.setTotalTareas(plTaskService.countByPositionAndPlan(user.getPosition(), optional.get()));
        }
        List<NotificacionLeida> notificacionesNoLeidas = leidaService.findByUser_PositionAndLeido(user.getPosition(), false);
        Set<Notificacion> notificacionSet = notificacionesNoLeidas.parallelStream().map(NotificacionLeida::getNotificacion).collect(Collectors.toSet());
        List<StatusTask> statusTasks = statusTaskService.findAll();
        TreeSet<EstadoTarea> estadoTareas = new TreeSet<>();
        for (StatusTask statusTask : statusTasks) {
            EstadoTarea estadoTarea = new EstadoTarea(statusTask);
            Integer total = optional.isPresent() ? plTaskService.countByStatusTaskAndPositionAndPlan(statusTask, user.getPosition(), optional.get()) : 0;
            estadoTarea.setTotalTareas(total);
            estadoTareas.add(estadoTarea);
        }
        dashBoard.setTotalNotificaciones(notificacionSet.size());
        dashBoard.setEstadoTareas(estadoTareas);
        dashBoard.setSuccess(true);
        return ResponseEntity.ok(dashBoard);
    }
}
