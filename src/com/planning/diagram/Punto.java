package com.planning.diagram;

public class Punto {
    
    private int columna;
    
    private int colSpan;
    
    private int comienzo;
    
    public Punto(int columna, int colSpan, int comienzo) {
        this.columna = columna;
        this.colSpan = colSpan;
        this.comienzo = comienzo;
    }
    
    public int getColumna() {
        return columna;
    }
    
    public void setColumna(int columna) {
        this.columna = columna;
    }
    
    public int getColSpan() {
        return colSpan;
    }
    
    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }
    
    public int getComienzo() {
        return comienzo;
    }
    
    public void setComienzo(int comienzo) {
        this.comienzo = comienzo;
    }
}
