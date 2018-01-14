/*Generado por Disrupsoft*/
package com.planning.diagram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.planning.entity.CriticalyLevel;
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
    
    public Node() {
    }
    
    public Node(PlTask tarea) {
        final int[] idCriticidad = {0};
        key = "" + tarea.getTask().getId();
        nombre = tarea.getTask().getName();
        cargo = tarea.getTask().getCargo();
        partida = tarea.getTask().isStart();
        producto = tarea.getTask().getProduct();
        loc = null;
        tareaId = tarea.getTask().getId();
        tarea.getTask().getCriticalyLevels().forEach((CriticalyLevel c) -> {
            color = c.getColor();
            idCriticidad[0] = c.getId();
        });
        colorBorde = color;
        color = tarea.getTask().isHito() ? color : tarea.getTask().isStart() ? color : "#ffebee";
        group = "Celda(" + tarea.getTask().getPosition().getArea().getManagement().getId() + "," + idCriticidad[0] + ")";
        category = tarea.getTask().isHito() ? "Hito" : tarea.getTask().isStart() ? "Start" : "";
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
    
    @Override
    public String toString() {
        return "Node[key:" + key + ", nombre:" + nombre + "]";
    }
}
