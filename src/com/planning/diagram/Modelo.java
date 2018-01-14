package com.planning.diagram;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.planning.entity.CriticalyLevel;
import com.planning.entity.Position;

import java.util.ArrayList;

public class Modelo {
    
    @JsonProperty(value = "class")
    private String clase;
    
    private ArrayList<Node> nodeDataArray = new ArrayList<>();
    
    private ArrayList<Edge> linkDataArray = new ArrayList<>();
    
    @JsonIgnore
    private boolean modificado = false;
    
    public Modelo() {
    }
    
    public void addNode(Node node) {
        if (!existNode(node)) {
            nodeDataArray.add(node);
            modificado = true;
        }
    }
    
    public void deleteNode(Node node) {
        int pos = -1;
        if ((pos = buscarNodo(node)) != -1) {
            nodeDataArray.remove(pos);
            modificado = true;
        }
    }
    
    public void addEdge(Edge edge) {
        if (!existEdge(edge) && edge.isValid()) {
            linkDataArray.add(edge);
            modificado = true;
        }
    }
    
    public void deleteEdge(Edge edge) {
        int pos = -1;
        if ((pos = buscarEnge(edge)) != -1) {
            linkDataArray.remove(pos);
            modificado = true;
        }
    }
    
    private ArrayList<Node> buscarSiders() {
        ArrayList<Node> siders = new ArrayList<>();
        int size = nodeDataArray.size();
        for (int i = 0; i < size; i++) {
            if (nodeDataArray.get(i).getCategory().compareToIgnoreCase("Row Sider") == 0)
                siders.add(nodeDataArray.remove(i));
        }
        return siders;
    }
    
    private ArrayList<Node> buscarColumnas() {
        ArrayList<Node> columnas = new ArrayList<>();
        int size = nodeDataArray.size();
        for (int i = 0; i < size; i++) {
            if (nodeDataArray.get(i).getCategory().compareToIgnoreCase("Column Header") == 0)
                columnas.add(nodeDataArray.remove(i));
        }
        return columnas;
    }
    
    private ArrayList<Node> buscarCeldas() {
        ArrayList<Node> columnas = new ArrayList<>();
        int size = nodeDataArray.size();
        for (int i = 0; i < size; i++) {
            if (nodeDataArray.get(i).getEsGrupo() != null && nodeDataArray.get(i).getEsGrupo())
                columnas.add(nodeDataArray.remove(i));
        }
        return columnas;
    }
    
    private Node crearNodoSider(Position position) {
        Node node = new Node();
        node.setKey("" + position.getId());
        node.setRow(2);
        node.setCategory("Row Sider");
        node.setNombre(position.getName());
        return node;
    }
    
    private Node crearNodoColumna(CriticalyLevel level) {
        Node node = new Node();
        node.setKey("" + level.getId());
        node.setCol(2);
        node.setCategory("Column Header");
        node.setNombre(level.getName());
        node.setColorHeader(level.getColor());
        return node;
    }
    
    public String getClase() {
        return clase;
    }
    
    public void setClase(String clase) {
        this.clase = clase;
    }
    
    public ArrayList<Node> getNodeDataArray() {
        return nodeDataArray;
    }
    
    public void setNodeDataArray(ArrayList<Node> nodeDataArray) {
        this.nodeDataArray = nodeDataArray;
    }
    
    public ArrayList<Edge> getLinkDataArray() {
        return linkDataArray;
    }
    
    public void setLinkDataArray(ArrayList<Edge> linkDataArray) {
        this.linkDataArray = linkDataArray;
    }
    
    public boolean isModificado() {
        return modificado;
    }
    
    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }
    
    private boolean existNode(Node node) {
        for (Node nod : nodeDataArray) {
            if (nod.getKey().compareTo(node.getKey()) == 0) {
                return true;
            }
        }
        return false;
    }
    
    private boolean existEdge(Edge edge) {
        for (Edge edg : linkDataArray) {
            if (edg.getFrom().compareTo(edge.getFrom()) == 0 && edg.getTo().compareTo(edge.getTo()) == 0) {
                return true;
            }
        }
        return false;
    }
    
    private int buscarNodo(Node n) {
        int pos = -1, i = 0;
        for (Node node : nodeDataArray) {
            if (node.getKey().compareToIgnoreCase(n.getKey()) == 0) {
                pos = i;
                break;
            }
            i++;
        }
        return pos;
    }
    
    private int buscarEnge(Edge n) {
        int pos = -1, i = 0;
        for (Edge edge : linkDataArray) {
            if (edge.getFrom().compareToIgnoreCase(n.getFrom()) == 0 && edge.getTo().compareToIgnoreCase(n.getTo()) == 0) {
                pos = i;
                break;
            }
            i++;
        }
        return pos;
    }
}
