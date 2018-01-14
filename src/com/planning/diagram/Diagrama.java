/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.diagram;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Nodo
 */
public class Diagrama {
    
    private ArrayList<Columna> columnas;
    
    private String descripcion;
    
    private String diagrama;
    
    @JsonProperty(value = "plan_id")
    private String planId;
    
    private ArrayList<Sider> siders;
    
    private ArrayList<TareaDiagrama> tareas;
    
    private ArrayList<Edge> links;
    
    public Diagrama() {
        columnas = new ArrayList<>();
        siders = new ArrayList<>();
        tareas = new ArrayList<>();
        links = new ArrayList<>();
    }
    
    public ArrayList<Columna> getColumnas() {
        return columnas;
    }
    
    public void addColumna(Columna columna) {
        if (!existeColumnaEnLista(columnas, columna.getNombre())) {
            columnas.add(columna);
        }
    }
    
    public void addEdge(Edge edge) {
        if (!existEdge(edge) && edge.isValid())
            links.add(edge);
    }
    
    
    public void setColumnas(ArrayList<Columna> columnas) {
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
    
    public ArrayList<Sider> getSiders() {
        return siders;
    }
    
    public void setSiders(ArrayList<Sider> siders) {
        this.siders = siders;
    }
    
    public void addSiders(Sider sider) {
        if (!existeSiderEnLista(siders, sider)) {
            siders.add(sider);
        }
    }
    
    public ArrayList<TareaDiagrama> getTareas() {
        return tareas;
    }
    
    public void setTareas(ArrayList<TareaDiagrama> tareas) {
        this.tareas = tareas;
    }
    
    public void addTarea(TareaDiagrama task) {
        tareas.add(task);
    }
    
    private boolean existeSiderEnLista(ArrayList<Sider> elementos, Sider sider) {
        if (elementos.isEmpty()) {
            return false;
        }
        for (Sider elemento : elementos) {
            if (elemento.getIdSider() == sider.getIdSider()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean existeEnLista(ArrayList<String> elementos, String elem) {
        if (elementos.isEmpty()) {
            return false;
        }
        for (String elemento : elementos) {
            if (elemento.compareToIgnoreCase(elem) == 0) {
                return true;
            }
        }
        return false;
    }
    
    private boolean existeTareaEnLista(ArrayList<TareaDiagrama> elementos, TareaDiagrama elem) {
        if (elementos.isEmpty()) {
            return false;
        }
        for (TareaDiagrama elemento : elementos) {
            if (Objects.equals(elemento.getId(), elem.getId())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean existeColumnaEnLista(ArrayList<Columna> elementos, String elem) {
        if (elementos.isEmpty()) {
            return false;
        }
        for (Columna elemento : elementos) {
            if (elemento.getNombre().compareToIgnoreCase(elem) == 0) {
                return true;
            }
        }
        return false;
    }
    
    private boolean existeColorEnLista(ArrayList<Color> elementos, Color elem) {
        if (elementos.isEmpty()) {
            return false;
        }
        for (Color elemento : elementos) {
            if (elemento.getColor().compareToIgnoreCase(elem.getColor()) == 0) {
                return true;
            }
        }
        return false;
    }
    
    
    private boolean existEdge(Edge edge) {
        for (Edge edg : links) {
            if (edg.getFrom().compareTo(edge.getFrom()) == 0 && edg.getTo().compareTo(edge.getTo()) == 0) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Edge> getLinks() {
        return links;
    }
    
    public void setLinks(ArrayList<Edge> links) {
        this.links = links;
    }
    
    public void sortColumns() {
        Collections.sort(siders, (Sider s1, Sider s2) -> {
            return s1.getPeso() < s2.getPeso() ? -1 : s1.getPeso() == s2.getPeso() ? 0 : 1;
        });
        Collections.sort(columnas, (Columna o1, Columna o2) -> {
            return o1.getPeso() < o2.getPeso() ? -1 : o1.getPeso() == o2.getPeso() ? 0 : 1;
        });
    }
}
