/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.diagram;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

/**
 * @author Nodo
 */
public class Diagrama {
    
    private TreeSet<Columna> columnas;
    
    private String descripcion;
    
    private String diagrama;
    
    @JsonProperty(value = "plan_id")
    private String planId;
    
    private TreeSet<Sider> siders;
    
    private TreeSet<TareaDiagrama> tareas;
    
    private ArrayList<TareaDiagrama> tareasTranversales;
    
    private ArrayList<Edge> links;
    
    public Diagrama() {
        columnas = new TreeSet<>();
        siders = new TreeSet<>();
        tareas = new TreeSet<>();
        links = new ArrayList<>();
        tareasTranversales = new ArrayList<>();
    }
    
    public TreeSet<Columna> getColumnas() {
        return columnas;
    }
    
    public void addColumna(Columna columna) {
        columnas.add(columna);
    }
    
    public void addEdge(Edge edge) {
        links.add(edge);
    }
    
    public void setColumnas(TreeSet<Columna> columnas) {
        this.columnas = columnas;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDiagrama() {
        return diagrama;
    }
    
    public void setDiagrama(String diagrama) {
        this.diagrama = diagrama;
    }
    
    public String getPlanId() {
        return planId;
    }
    
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    
    public TreeSet<Sider> getSiders() {
        return siders;
    }
    
    public void setSiders(TreeSet<Sider> siders) {
        this.siders = siders;
    }
    
    public void addSiders(Sider sider) {
        siders.add(sider);
    }
    
    public TreeSet<TareaDiagrama> getTareas() {
        return tareas;
    }
    
    public void setTareas(TreeSet<TareaDiagrama> tareas) {
        this.tareas = tareas;
    }
    
    public void addTarea(TareaDiagrama task) {
        tareas.add(task);
    }
    
    public void addTareaTranversal(TareaDiagrama task) {
        tareasTranversales.add(task);
    }
    
    public ArrayList<Edge> getLinks() {
        return links;
    }
    
    public void setLinks(ArrayList<Edge> links) {
        this.links = links;
    }
    
    public ArrayList<TareaDiagrama> getTareasTranversales() {
        return tareasTranversales;
    }
    
    public void setTareasTranversales(ArrayList<TareaDiagrama> tareasTranversales) {
        this.tareasTranversales = tareasTranversales;
    }
    
    public void ordenarTareasTranversales() {
        Collections.sort(tareasTranversales, (o1, o2) -> o1.getColSpan() - o2.getColSpan());
    }
}
