/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.util;

import com.planning.entity.*;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nodo
 */
public class ArregloCreator {
    
    public static ModelMap crearTareaMap(Task task, Users usuario) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", task.getId());
        map.put("0", "<input type='checkbox' class='checkboxes' value='1' data-id='" + task.getId() + " '/>");
        map.put("1", task.getCode());
        map.put("2", task.getName());
        map.put("3", task.getPosition().getName());
        map.put("4", task.getCriticidad());
        map.put("5", task.isHito() ? "Si <i class=\"fa fa-check-circle ic-color-ok\"></i>" : "No <i class=\"fa fa-minus-circle ic-color-error\"></i>");
        map.put("6", usuario.getRol().getId() == 1 ? listarAcciones(task.getId()) : listarAccionesUsuarioTarea(task.getId()));
        return map;
    }
    
    public static ModelMap crearTareaPlanMap(Task task) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", task.getId());
        map.put("0", task.getCode());
        map.put("1", task.getName());
        map.put("2", task.getPosition().getName());
        map.put("3", task.getCriticidad());
        map.put("4", "<a class='btn btn-icon-only yellow view' href='javascript:;' data-id='" + task.getId() + "' ><i class='fa fa-eye fa-fw'></i></a>");
        return map;
    }
    
    public static ModelMap crearTareaPlan(Task task) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", task.getId());
        map.put("0", task.getCode());
        map.put("1", task.getName());
        map.put("2", task.getPosition().getName());
        map.put("3", task.getCriticidad());
        map.put("4", task.isStart() ? "Si <i class=\"fa fa-check-circle ic-color-ok\"></i>" : "No <i class=\"fa fa-minus-circle ic-color-error\"></i>");
        map.put("5", listarAccionesTarea(task.getId()));
        return map;
    }
    
    private static String listarAccionesTarea(Integer id) {
        return "<a class='btn btn-icon-only green edit table-action' href='javascript:;' data-id='" + id + "'><i class='fa fa-edit fa-fw'></i></a>" +
                "<a class='btn btn-icon-only red delete table-action' href='javascript:;' data-id='" + id + "'><i class='fa fa-trash-o fa-fw'></i></a>" +
                "<a class='btn btn-icon-only yellow partida table-action' href='javascript:;' data-id='" + id + "'><i class='fa fa-hourglass-start fa-fw'></i></a>";
    }
    
    public static ModelMap crearTareaImportarMap(Task task, int number) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", task.getId());
        map.put("0", number);
        map.put("1", task.getCode());
        map.put("2", task.getName());
        map.put("3", task.getCriticidad());
        map.put("4", task.isStart() ? "Si <i class='fa fa-check-circle ic-color-ok'></i>" : "No <i class='fa fa-minus-circle ic-color-error'></i>");
        map.put("5", listarAcciones(task.getId(), task.getCode()));
        return map;
    }
    
    private static String listarAcciones(Integer id) {
        return "<a class='btn btn-icon-only green edit' href='javascript:;' data-id='" + id + "'><i class='fa fa-edit fa-fw'></i></a>"
                + "<a class='btn btn-icon-only red delete' href='javascript:;' data-id='" + id + "'><i class='fa fa-trash-o fa-fw'></i></a>";
    }
    
    
    private static String listarAccionesUsuarioTarea(Integer id) {
        return "<a class='btn btn-icon-only yellow view' href='javascript:;' data-id='" + id + "'><i class='fa fa-eye fa-fw'></i></a>";
    }
    
    private static String listarAccionesUsuario(Integer id) {
        return "<a class='btn btn-icon-only green edit' href='javascript:;' data-id='" + id + "'><i class='fa fa-edit fa-fw'></i></a>"
                + "<a class='btn btn-icon-only yellow block' href='javascript:;' data-id='" + id + "'><i class='fa fa-lock fa-fw'></i></a>"
                + "<a class='btn btn-icon-only red delete' href='javascript:;' data-id='" + id + "'><i class='fa fa-trash-o fa-fw'></i></a>";
    }
    
    private static String listarAccionesPlan(Integer id, String nombre) {
        return "<a class='btn btn-icon-only green edit' href='javascript:;' data-id='" + id + "'><i class='fa fa-edit fa-fw'></i></a>"
                + "<a class='btn btn-icon-only red delete' href='javascript:;' data-id='" + id + "'><i class='fa fa-trash-o fa-fw'></i></a>"
                + "<a class='btn btn-icon-only purple diagrama' href='javascript:;' data-id='" + id + "'><i class='fa fa-cubes fa-fw'></i></a>"
                + "<a class='btn btn-icon-only blue clonar' href='javascript:;' data-id='" + id + "' data-nombre='" + nombre + "'><i class='fa fa-clone fa-fw'></i></a>";
    }
    
    private static String listarAcciones(Integer id, String codigo) {
        return "<a class='btn btn-icon-only green add' href='javascript:;' data-id='" + id + "' data-codigo='" + codigo + "'><i class='fa fa-plus fa-fw'></i></a>";
    }
    
    public static ModelMap crearPlanMap(Plan plan) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", plan.getId());
        map.put("0", "<input type='checkbox' class='checkboxes' value='1' data-id='" + plan.getId() + " '/>");
        map.put("1", plan.getName());
        map.put("2", plan.getDescription());
        map.put("3", plan.getStatusplanid().getName());
        map.put("4", plan.getFechaCreacion());
        map.put("5", listarAccionesPlan(plan.getId(), plan.getName()));
        return map;
    }
    
    public static ModelMap crearPlanCriticidadMap(CriticalyLevel level) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", level.getId());
        map.put("0", "<input type='checkbox' class='checkboxes' value='1' data-id='" + level.getId() + " '/>");
        map.put("1", level.getName());
        map.put("2", level.getDescription());
        map.put("3", level.getActive() ? "Activo <i class='fa fa-check-circle ic-color-ok'></i>" : "Inactivo <i class='fa fa-minus-circle ic-color-error'></i>");
        map.put("4", "<span style='background-color:" + level.getColor() + "'>" + level.getColor() + "</span>");
        map.put("5", level.getOrder());
        map.put("6", listarAcciones(level.getId()));
        return map;
    }
    
    public static ModelMap crearEstadoPlanMap(StatusPlan plan) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", plan.getId());
        map.put("0", "<input type='checkbox' class='checkboxes' value='1' data-id='" + plan.getId() + " '/>");
        map.put("1", plan.getName());
        map.put("2", plan.isActive() ? "Activo <i class='fa fa-check-circle ic-color-ok'></i>" : "Inactivo <i class='fa fa-minus-circle ic-color-error'></i>");
        map.put("3", listarAcciones(plan.getId()));
        return map;
    }
    
    public static ModelMap cargarPlan(Plan plan) {
        ModelMap map = new ModelMap();
        map.put("plan_id", plan.getId());
        map.put("nombre", plan.getName());
        map.put("descripcion", plan.getDescription());
        map.put("estado_id", plan.getStatusplanid().getId());
        return map;
    }
    
    public static ModelMap cargarChannel(Channel channel) {
        ModelMap map = new ModelMap();
        map.put("canal_id", channel.getId());
        map.put("nombre", channel.getChannel());
        map.put("descripcion", channel.getDescription());
        return map;
    }
    
    public static ModelMap crearEstadoPlan(StatusPlan plan) {
        ModelMap map = new ModelMap();
        map.put("estado_id", plan.getId());
        map.put("descripcion", plan.getName());
        map.put("estado", plan.isActive());
        return map;
    }
    
    public static ModelMap crearEstadoTarea(StatusTask tarea) {
        ModelMap map = new ModelMap();
        map.put("estado_id", tarea.getId());
        map.put("descripcion", tarea.getDescription());
        map.put("nombre", tarea.getName());
        map.put("peso", tarea.getOrder());
        map.put("estado", tarea.isActive());
        map.put("color", tarea.getColor());
        return map;
    }
    
    public static ModelMap cargarUsuario(Users user) {
        ModelMap map = new ModelMap();
        map.put("usuario_id", user.getId());
        map.put("rol", user.getRol().getId());
        map.put("perfil", user.getName());
        map.put("nombre", user.getName());
        map.put("apellidos", user.getLastname());
        map.put("email", user.getEmail());
        map.put("habilitado", user.isEnabled());
        map.put("cargo_id", user.getPosition().getId());
        map.put("area_id", user.getPosition().getArea().getId());
        map.put("gerencia_id", user.getPosition().getArea().getManagement().getId());
        return map;
    }
    
    public static ModelMap cargarTarea(Task task, List<Area> areas, List<Position> positions, List<Document> documents) {
        ModelMap map = new ModelMap();
        map.put("tarea_id", task.getId());
        map.put("nombre", task.getName());
        map.put("descripcion", task.getDescription());
        map.put("codigo", task.getCode());
        map.put("recurrente", task.isIsrecurrent());
        map.put("tiempoRecurrencia", task.getTiempoRecurrencia());
        map.put("canales", task.getCanales());
        map.put("canales_id", task.getChannels());
        map.put("producto", task.getProduct());
        map.put("partida", task.isStart());
        map.put("hito", task.isHito());
        map.put("tranversal", task.isTranversal());
        map.put("fechaCreacion", task.getFechaCreacion());
        map.put("estado", task.getPosition().getName());
        map.put("criticidad_id", task.getCriticalyLevels());
        map.put("criticidad", task.getCriticidad());
        map.put("estado_id", task.getStatusTask().getId());
        map.put("estado", task.getStatusTask().getName());
        map.put("cargo", task.getPosition().getName());
        map.put("cargo_id", task.getPosition().getId());
        map.put("gerencia_id", task.getPosition().getArea().getManagement().getId());
        map.put("area_id", task.getPosition().getArea().getId());
        map.put("areas", areas);
        map.put("cargos", positions);
        map.put("modelos", documents);
        return map;
    }
    
    public static ModelMap cargarTarea(Task task, List<Area> areas, List<Position> positions) {
        ModelMap map = new ModelMap();
        map.put("tarea_id", task.getId());
        map.put("nombre", task.getName());
        map.put("descripcion", task.getDescription());
        map.put("codigo", task.getCode());
        map.put("recurrente", task.isIsrecurrent());
        map.put("tiempoRecurrencia", task.getTiempoRecurrencia());
        map.put("canales", task.getCanales());
        map.put("canales_id", task.getChannels());
        map.put("producto", task.getProduct());
        map.put("partida", task.isStart());
        map.put("hito", task.isHito());
        map.put("tranversal", task.isTranversal());
        map.put("estado", task.getPosition().getName());
        map.put("criticidad_id", task.getCriticalyLevels());
        map.put("criticidad", task.getCriticidad());
        map.put("estado_id", task.getStatusTask().getId());
        map.put("estado", task.getStatusTask().getName());
        map.put("cargo", task.getPosition().getName());
        map.put("cargo_id", task.getPosition().getId());
        map.put("gerencia_id", task.getPosition().getArea().getManagement().getId());
        map.put("area_id", task.getPosition().getArea().getId());
        map.put("areas", areas);
        map.put("cargos", positions);
        return map;
    }
    
    public static ModelMap cargarArea(Area area) {
        ModelMap map = new ModelMap();
        map.put("area_id", area.getId());
        map.put("nombre", area.getName());
        map.put("descripcion", area.getDescription());
        map.put("gerencia_id", area.getManagement().getId());
        return map;
    }
    
    public static ModelMap cargarUsuario(Users user, List<Area> areas, List<Position> positions) {
        ModelMap map = new ModelMap();
        map.put("usuario_id", user.getId());
        map.put("rol", user.getRol().getId());
        map.put("perfil", user.getName());
        map.put("nombre", user.getName());
        map.put("apellidos", user.getLastname());
        map.put("titular", user.isTitular());
        map.put("email", user.getEmail());
        map.put("habilitado", user.isEnabled());
        map.put("cargo_id", user.getPosition().getId());
        map.put("area_id", user.getPosition().getArea().getId());
        map.put("gerencia_id", user.getPosition().getArea().getManagement().getId());
        map.put("cargos", positions);
        map.put("areas", areas);
        return map;
    }
    
    public static ModelMap crearRolMap(Rol rol) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", rol.getId());
        map.put("0", "<input type='checkbox' class='checkboxes' value='1' data-id='" + rol.getId() + " '/>");
        map.put("1", rol.getName());
        map.put("2", rol.getDescription());
        map.put("3", listarAcciones(rol.getId()));
        return map;
    }
    
    public static ModelMap crearGerenciaMap(Management gerencia) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", gerencia.getId());
        map.put("0", "<input type='checkbox' class='checkboxes' value='1' data-id='" + gerencia.getId() + " '/>");
        map.put("1", gerencia.getName());
        map.put("2", gerencia.getOrder());
        map.put("3", listarAcciones(gerencia.getId()));
        return map;
    }
    
    public static ModelMap crearAreaMap(Area area) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", area.getId());
        map.put("0", "<input type='checkbox' class='checkboxes' value='1' data-id='" + area.getId() + " '/>");
        map.put("1", area.getName());
        map.put("2", area.getDescription());
        map.put("3", area.getManagement().getName());
        map.put("4", listarAcciones(area.getId()));
        return map;
    }
    
    public static ModelMap crearCargoMap(Position cargo) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", cargo.getId());
        map.put("0", "<input type='checkbox' class='checkboxes' value='1' data-id='" + cargo.getId() + " '/>");
        map.put("1", cargo.getName());
        map.put("2", cargo.getArea().getName());
        map.put("3", cargo.getArea().getManagement().getName());
        map.put("4", listarAcciones(cargo.getId()));
        return map;
    }
    
    public static ModelMap crearUsersMap(Users user) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", user.getId());
        map.put("0", "<input type='checkbox' class='checkboxes' value='1' data-id='" + user.getId() + " '/>");
        map.put("1", user.getEmail());
        map.put("2", user.getName());
        map.put("3", user.getLastname());
        map.put("4", user.isEnabled() ? "Activo <i class=\"fa fa-check-circle ic-color-ok\"></i>" : "Inactivo <i class=\"fa fa-minus-circle ic-color-error\"></i>");
        map.put("5", user.getRol().getName());
        map.put("6", listarAccionesUsuario(user.getId()));
        return map;
    }
    
    public static ModelMap crearEstadoTareasMap(StatusTask estado) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", estado.getId());
        map.put("0", "<input type='checkbox' class='checkboxes' value='1' data-id='" + estado.getId() + "'/>");
        map.put("1", estado.getName());
        map.put("2", estado.getDescription());
        map.put("3", estado.isActive() ? "Activo <i class=\"fa fa-check-circle ic-color-ok\"></i>" : "Inactivo <i class=\"fa fa-minus-circle ic-color-error\"></i>");
        map.put("4", "<span style='background-color:" + estado.getColor() + "'>" + estado.getColor() + "</span>");
        map.put("5", estado.getOrder());
        map.put("6", listarAcciones(estado.getId()));
        return map;
    }
    
    public static ModelMap crearChannelMap(Channel channel) {
        ModelMap map = new ModelMap();
        map.put("DT_RowId", channel.getId());
        map.put("0", "<input type='checkbox' class='checkboxes' value='1' data-id='" + channel.getId() + "'/>");
        map.put("1", channel.getChannel());
        map.put("2", channel.getDescription());
        map.put("3", listarAcciones(channel.getId()));
        return map;
    }
    
    public static ModelMap cargarTarea(PlTask t, List<Area> areas, List<Position> positions, List<Document> documents) {
        ModelMap map = new ModelMap();
        map.put("tarea_id", t.getTask().getId());
        map.put("modelos", documents);
        map.put("descripcion", t.getTask().getDescription());
        map.put("codigo", t.getTask().getCode());
        map.put("nombre", t.getTask().getName());
        map.put("criticidad", t.getTask().getCriticidad());
        map.put("canales", t.getTask().getChannels());
        map.put("estado", t.getTask().getStatusTask().getName());
        map.put("estado_id", t.getTask().getStatusTask().getId());
        map.put("recurrente", t.isIsrecurrent());
        map.put("partida", t.isStart());
        map.put("tranversal", t.getTask().isTranversal());
        map.put("hito", t.getTask().isHito());
        map.put("producto", t.getTask().getProduct());
        map.put("criticidad_id", t.getTask().getCriticalyLevels());
        map.put("cargo_id", t.getTask().getPosition().getId());
        map.put("cargo", t.getTask().getPosition().getName());
        map.put("gerencia_id", t.getTask().getPosition().getArea().getManagement().getId());
        map.put("area_id", t.getTask().getPosition().getArea().getId());
        map.put("areas", areas);
        map.put("cargos", positions);
        map.put("relacionadas", t.getTask().getRelacionadas());
        return map;
    }
    
    public static ModelMap cargarTareaFicticia(PlTaskUtil t, List<Area> areas, List<Position> positions, List<Document> documents) {
        ModelMap map = new ModelMap();
        map.put("tarea_id", t.getTask().getId());
        map.put("modelos", documents);
        map.put("codigo", t.getTask().getCode());
        map.put("nombre", t.getTask().getName());
        map.put("criticidad", t.getCriticidad());
        map.put("canales", t.getChannels());
        map.put("producto", t.getTask().getProduct());
        map.put("partida", t.isPartida());
        map.put("estado", t.getStatusTask().getName());
        map.put("estado_id", t.getStatusTask().getId());
        map.put("recurrente", t.isIsrecurrent());
        map.put("criticidad_id", t.getCriticalyLevels());
        map.put("cargo_id", t.getPosition().getId());
        map.put("cargo", t.getPosition().getName());
        map.put("gerencia_id", t.getPosition().getArea().getManagement().getId());
        map.put("area_id", t.getPosition().getArea().getId());
        map.put("areas", areas);
        map.put("cargos", positions);
        map.put("relacionadas", new ArrayList<>());
        return map;
    }
}
