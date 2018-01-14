package com.planning.diagram;

public class Sider {
    
    private int idSider;
    
    private int peso;
    
    private String nombre;
    
    
    public Sider(int idSider, String nombre, int peso) {
        this.idSider = idSider;
        this.nombre = nombre;
        this.peso = peso;
    }
    
    public int getIdSider() {
        return idSider;
    }
    
    public void setIdSider(int idSider) {
        this.idSider = idSider;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getPeso() {
        return peso;
    }
    
    public void setPeso(int peso) {
        this.peso = peso;
    }
}
