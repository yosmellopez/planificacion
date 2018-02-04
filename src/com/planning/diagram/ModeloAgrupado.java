package com.planning.diagram;

import java.util.ArrayList;
import java.util.HashSet;

public class ModeloAgrupado {
    
    private HashSet<Node> nodeDataArray = new HashSet<>();
    
    private HashSet<Edge> linkDataArray = new HashSet<>();
    
    public ModeloAgrupado() {
    }
    
    public void addNode(Node node) {
        nodeDataArray.add(node);
    }
    
    public void addLink(Edge edge) {
        linkDataArray.add(edge);
    }
    
    public HashSet<Node> getNodeDataArray() {
        return nodeDataArray;
    }
    
    public void setNodeDataArray(HashSet<Node> nodeDataArray) {
        this.nodeDataArray = nodeDataArray;
    }
    
    public HashSet<Edge> getLinkDataArray() {
        return linkDataArray;
    }
    
    public void setLinkDataArray(HashSet<Edge> linkDataArray) {
        this.linkDataArray = linkDataArray;
    }
    
    public void eliminarNode(Node node) {
        nodeDataArray.remove(node);
    }
    
    public void eliminarEdge(Edge edge) {
        linkDataArray.remove(edge);
    }
    
    public ArrayList<Node> buscarNodo(int idTarea) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node node : nodeDataArray) {
            if (node.getTareaId() == idTarea)
                nodes.add(node);
        }
        return nodes;
    }
    
    public ArrayList<Edge> buscarLink(int idTarea) {
        ArrayList<Edge> edges = new ArrayList<>();
        for (Edge edge : linkDataArray) {
            if (edge.getFrom().compareToIgnoreCase("" + idTarea) == 0 || edge.getTo().compareToIgnoreCase("" + idTarea) == 0)
                edges.add(edge);
        }
        return edges;
    }
}
