/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.diagram;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Nodo
 */
public class MyBoard {

    private ArrayList<Column> columnas;

    private ArrayList<Datos> datos;

    private ArrayList<Recurso> recursos;

    public MyBoard() {
        this.columnas = columnas = new ArrayList<>();
        this.datos = new ArrayList<>();
        this.recursos = new ArrayList<>();
    }

    public void addColum(Column column) {
        if (!existeDataField(column)) {
            columnas.add(column);
        }
    }

    public void addRecurso(Recurso recurso) {
        if (!existeRecurso(recurso)) {
            recursos.add(recurso);
        }
    }

    public ArrayList<Column> getColumnas() {
        return columnas;
    }

    public void setColumnas(ArrayList<Column> columnas) {
        this.columnas = columnas;
    }

    public void addDato(Datos dato) {
        if (!existeData(dato)) {
            datos.add(dato);
        }
    }

    public ArrayList<Datos> getDatos() {
        return datos;
    }

    public void setDatos(ArrayList<Datos> datos) {
        this.datos = datos;
    }

    private boolean existeDataField(Column c) {
        if (columnas.isEmpty()) {
            return false;
        }
        for (Column columna : columnas) {
            if (Objects.equals(columna.getDataField(), c.getDataField())) {
                return true;
            }
        }
        return false;
    }

    private boolean existeData(Datos c) {
        if (datos.isEmpty()) {
            return false;
        }
        for (Datos dato : datos) {
            if (Objects.equals(dato.getId(), c.getId())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(ArrayList<Recurso> recursos) {
        this.recursos = recursos;
    }

    private boolean existeRecurso(Recurso c) {
        if (recursos.isEmpty()) {
            return false;
        }
        for (Recurso dato : recursos) {
            if (Objects.equals(dato.getId(), c.getId())) {
                return true;
            }
        }
        return false;
    }
}
