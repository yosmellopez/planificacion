/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.diagram;

/**
 * @author Nodo
 */
public class Columna {
    
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
}
