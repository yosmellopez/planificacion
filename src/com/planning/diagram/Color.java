package com.planning.diagram;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.planning.util.ColorSerializer;

@JsonSerialize(using = ColorSerializer.class)
public class Color {
    
    private int peso;
    
    private String color;
    
    public Color(int peso, String color) {
        this.peso = peso;
        this.color = color;
    }
    
    public int getPeso() {
        return peso;
    }
    
    public void setPeso(int peso) {
        this.peso = peso;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}
