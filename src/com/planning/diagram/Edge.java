/*Generado por Disrupsoft*/
package com.planning.diagram;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Edge implements Serializable {

    private static final long serialVersionUID = 1L;

    private String from;

    private String to;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String routing;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private ArrayList<Double> points = new ArrayList<>();

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

    public ArrayList<Double> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Double> points) {
        this.points = points;
    }

    public String getRouting() {
        return routing;
    }

    public void setRouting(String routing) {
        this.routing = routing;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.from);
        hash = 67 * hash + Objects.hashCode(this.to);
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
        final Edge other = (Edge) obj;
        if (!Objects.equals(this.from, other.from) || !Objects.equals(this.to, other.to)) {
            return false;
        }
        return Objects.equals(this.from, other.from) && Objects.equals(this.to, other.to);
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
