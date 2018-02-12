package com.planning.api;

import com.planning.api.entity.EstadoTarea;
import com.planning.entity.DashBoard;
import com.planning.entity.StatusTask;
import com.planning.entity.Users;
import com.planning.service.NotificacionService;
import com.planning.service.StatusTaskService;
import com.planning.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.TreeSet;

@RestController
@RequestMapping(value = "/api")
public class DashBoardRestApi {
    
    @Autowired
    private StatusTaskService statusTaskService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private NotificacionService notificacionService;
    
    @GetMapping(value = "/dashboard/stats")
    public ResponseEntity<DashBoard> dashboard(@AuthenticationPrincipal Users user) {
        DashBoard dashBoard = new DashBoard();
        List<StatusTask> statusTasks = statusTaskService.findAll();
        Long count = notificacionService.countByPositionAndLeido(user.getPosition(), false);
        TreeSet<EstadoTarea> estadoTareas = new TreeSet<>();
        for (StatusTask statusTask : statusTasks) {
            EstadoTarea estadoTarea = new EstadoTarea(statusTask);
            Integer total = taskService.countByStatusTaskAndPosition(statusTask, user.getPosition());
            estadoTarea.setTotalTareas(total);
            estadoTareas.add(estadoTarea);
        }
        dashBoard.setTotalNotificaciones(count);
        dashBoard.setEstadoTareas(estadoTareas);
        dashBoard.setTotalTareas(taskService.countByPosition(user.getPosition()));
        dashBoard.setTotalNotificaciones(5);
        dashBoard.setSuccess(true);
        return ResponseEntity.ok(dashBoard);
    }
}
