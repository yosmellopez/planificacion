/*Generado por Disrupsoft*/
package com.planning.diagram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.planning.entity.CriticalyLevel;
import com.planning.entity.Management;
import com.planning.entity.PlTask;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Node implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String key;

    @JsonProperty(value = "text")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nombre;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer col;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer row;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer peso;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer colSpan;

    @JsonProperty(value = "color_header")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String colorHeader;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String category;

    @JsonProperty(value = "isGroup")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean esGrupo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cargo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean partida;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String producto;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String loc;

    @JsonProperty(value = "tarea_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer tareaId;

    @JsonProperty(value = "color_borde")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String colorBorde;

    @JsonProperty(value = "color")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String color;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String group;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String size;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bounds;

    public Node() {
    }

    public Node(PlTask task) {
        final int[] idCriticidad = {0};
        key = "" + task.getId();
        nombre = task.getName();
        cargo = task.getCargo();
        partida = task.isStart();
        producto = task.getProduct();
        loc = partida ? "0 0" : null;
        tareaId = task.getId();
        task.getCriticalyLevels().forEach((CriticalyLevel c) -> {
            color = c.getColor();
            idCriticidad[0] = c.getId();
        });
        colorBorde = color;
        color = task.isHito() ? color : task.isStart() ? color : "#ffebee";
        Management management = task.getPosition().getArea().getManagement();
        group = "Celda(" + management.getId() + "," + idCriticidad[0] + ")";
        category = task.isHito() ? "Hito" : task.isStart() ? "Start" : "";
    }

    public Node(Node node) {
        key = node.key;
        nombre = node.nombre;
        col = node.col;
        row = node.row;
        peso = node.peso;
        colSpan = node.colSpan;
        colorHeader = node.colorHeader;
        category = node.category;
        esGrupo = node.esGrupo;
        cargo = node.cargo;
        partida = node.partida;
        producto = node.producto;
        loc = node.loc;
        tareaId = node.tareaId;
        colorBorde = node.colorBorde;
        color = node.color;
        group = node.group;
        size = node.size;
        bounds = node.bounds;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public String getColorHeader() {
        return colorHeader;
    }

    public void setColorHeader(String colorHeader) {
        this.colorHeader = colorHeader;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getEsGrupo() {
        return esGrupo;
    }

    public void setEsGrupo(Boolean esGrupo) {
        this.esGrupo = esGrupo;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public Integer getTareaId() {
        return tareaId;
    }

    public void setTareaId(Integer tareaId) {
        this.tareaId = tareaId;
    }

    public String getColorBorde() {
        return colorBorde;
    }

    public void setColorBorde(String colorBorde) {
        this.colorBorde = colorBorde;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public String getBounds() {
        return bounds;
    }

    public void setBounds(String bounds) {
        this.bounds = bounds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        Node node = (Node) o;
        return key.compareToIgnoreCase(node.key) == 0;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return "Node[key:" + key + ", nombre:" + nombre + ", idTarea:" + tareaId + "]";
    }
}
