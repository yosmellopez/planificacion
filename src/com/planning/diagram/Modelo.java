package com.planning.diagram;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Modelo {
    
    @JsonProperty(value = "class")
    private String clase;
    
    private ModelData modelData;
    
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
    
    
    public void addSider(Node node) {
        if (!existNode(node)) {
            nodeDataArray.add(node);
            modificado = true;
            List<Node> ordenados = buscarSiders().parallelStream().sorted((o1, o2) -> o1.getPeso() > o2.getPeso() ? 1 : o1.getPeso() == o2.getPeso() ? 0 : -1).collect(Collectors.toList());
            int i = 0;
            for (Node elemento : ordenados) {
                deleteNode(elemento);
                ArrayList<Node> grupos = buscarGruposSider(elemento, node);
                elemento.setRow(2 + i);
                for (Node grupo : grupos) {
                    grupo.setRow(elemento.getRow());
                }
                addNode(elemento);
                if (elemento.equals(node))
                    node.setRow(elemento.getRow());
                i++;
            }
        }
    }
    
    private ArrayList<Node> buscarGruposSider(Node sider, Node newNode) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node elemento : nodeDataArray) {
            if (elemento.getRow() != null && Objects.equals(sider.getRow(), elemento.getRow()) && !elemento.equals(newNode))
                nodes.add(elemento);
        }
        return nodes;
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
        for (Node node : nodeDataArray) {
            if (node.getCategory() != null)
                if (node.getCategory().compareToIgnoreCase("Row Sider") == 0)
                    siders.add(node);
        }
        return siders;
    }
    
    public Node buscarSider(String key) {
        int size = nodeDataArray.size();
        for (int i = 0; i < size; i++) {
            if (nodeDataArray.get(i).getKey().compareToIgnoreCase(key) == 0)
                return nodeDataArray.get(i);
        }
        return null;
    }
    
    public Node buscarNodo(String key) {
        int size = nodeDataArray.size();
        for (int i = 0; i < size; i++) {
            if (nodeDataArray.get(i).getKey().compareToIgnoreCase(key) == 0)
                return nodeDataArray.get(i);
        }
        return null;
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
    
    public ArrayList<Node> buscarNodo(int idTarea) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node node : nodeDataArray) {
            if (node.getTareaId() != null && node.getTareaId() == idTarea)
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
    
    public ModelData getModelData() {
        return modelData;
    }
    
    public void setModelData(ModelData modelData) {
        this.modelData = modelData;
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
