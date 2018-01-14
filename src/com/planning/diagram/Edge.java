/*Generado por Disrupsoft*/
package com.planning.diagram;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class Edge implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String from;
    
    private String to;
    
    public Edge() {
    }
    
    public Edge(String from, String to) {
        this.from = from;
        this.to = to;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getTo() {
        return to;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    @JsonIgnore
    public boolean isValid() {
        return from.compareToIgnoreCase(to) != 0;
    }
    
    @Override
    public String toString() {
        return "Edge[from:" + from + ", to:" + to + "]";
    }
}
