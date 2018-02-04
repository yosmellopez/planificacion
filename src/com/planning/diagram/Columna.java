/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.diagram;

/**
 * @author Nodo
 */
public class Columna implements Comparable<Columna> {

    private int identificador;

    private int peso;

    private String nombre;

    private String color;

    public Columna(int id, int peso, String nombre, String color) {
        this.identificador = id;
        this.peso = peso;
        this.nombre = nombre;
        this.color = color;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.identificador;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Columna other = (Columna) obj;
        return this.identificador == other.identificador;
    }

    @Override
    public int compareTo(Columna o) {
        return peso - o.peso;
    }

}
