package com.planning.diagram;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.planning.entity.CriticalyLevel;
import com.planning.entity.PlTask;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public class TareaDiagrama implements Comparable<TareaDiagrama> {

    private String key;

    @JsonProperty(value = "tarea_id")
    private Integer id;

    private Integer idGerencia;

    private Integer idCriticidad;

    @JsonProperty(value = "nombre")
    private String name;

    private String producto;

    private String cargo;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer colSpan;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer col = null;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String grupo = null;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String loc;

    private boolean hito;

    private Boolean partida;

    private boolean agrupado = false;

    private boolean tranversal = false;

    private String color = "#333333";

    public TareaDiagrama(PlTask task, Integer idCriticidad, int orden, String color, boolean primero) {
        int idTask = task.getId();
        key = primero ? "" + idTask : idTask + "-" + idCriticidad;
        id = idTask;
        name = task.getName();
        producto = task.getProduct();
        cargo = task.getPosition().getName();
        partida = task.isStart();
        loc = partida ? "0 0" : null;
        idGerencia = task.getPosition().getArea().getManagement().getId();
        hito = task.isHito();
        this.idCriticidad = idCriticidad;
        this.color = color;
        tranversal = task.isTranversal();
        if (tranversal) {
            LinkedList<CriticalyLevel> criticalyLevels = new LinkedList<>(task.getCriticalyLevels());
            Collections.sort(criticalyLevels, (CriticalyLevel c1, CriticalyLevel c2) -> {
                return c1.getOrder() > c2.getOrder() ? 1 : Objects.equals(c1.getOrder(), c2.getOrder()) ? 0 : -1;
            });
            grupo = criticalyLevels.stream().map(level -> "" + level.getId()).collect(Collectors.joining("-"));
            colSpan = criticalyLevels.size();
            col = orden;
        }
    }

    public TareaDiagrama(PlTask tarea, Punto punto, String color) {
        int idTask = tarea.getId();
        key = "" + idTask;
        id = idTask;
        name = tarea.getName();
        producto = "";
        cargo = "";
        partida = tarea.isStart();
        loc = partida ? "0 0" : null;
        idGerencia = tarea.getPosition().getArea().getManagement().getId();
        hito = tarea.isHito();
        this.color = color;
        tranversal = true;
        agrupado = true;
        this.colSpan = punto.getColSpan();
        tranversal = colSpan != 0;
        this.col = punto.getColumna();
        grupo = "-" + this.col + "-" + this.colSpan;
        idCriticidad = punto.getComienzo();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String product) {
        this.producto = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Boolean getPartida() {
        return partida;
    }

    public void setPartida(Boolean partida) {
        this.partida = partida;
    }

    public Integer getIdGerencia() {
        return idGerencia;
    }

    public void setIdGerencia(Integer idGerencia) {
        this.idGerencia = idGerencia;
    }

    public Integer getIdCriticidad() {
        return idCriticidad;
    }

    public void setIdCriticidad(Integer idCriticidad) {
        this.idCriticidad = idCriticidad;
    }

    public boolean isAgrupado() {
        return agrupado;
    }

    public void setAgrupado(boolean agrupado) {
        this.agrupado = agrupado;
    }

    public boolean isHito() {
        return hito;
    }

    public void setHito(boolean hito) {
        this.hito = hito;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public boolean isTranversal() {
        return tranversal;
    }

    public void setTranversal(boolean tranversal) {
        this.tranversal = tranversal;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    @Override
    public int compareTo(TareaDiagrama o) {
        return key.compareToIgnoreCase(o.key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TareaDiagrama)) {
            return false;
        }
        TareaDiagrama that = (TareaDiagrama) o;
        boolean iguales = key.compareTo(that.key) == 0;
        return iguales;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
