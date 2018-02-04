package com.planning.diagram;

public class Sider implements Comparable<Sider> {

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.idSider;
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
        final Sider other = (Sider) obj;
        return this.idSider == other.idSider;
    }

    @Override
    public int compareTo(Sider o) {
        return peso - o.peso;
    }

}
