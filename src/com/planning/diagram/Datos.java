/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.diagram;

/**
 *
 * @author Nodo
 */
public class Datos {

    private Integer id;

    private Integer state;

    private String label;

    private String tags;

    private String hex;

    private Integer resourceId;

    public Datos(Integer id, Integer state, String label, String tags, String hex, Integer resourceId) {
        this.id = id;
        this.state = state;
        this.label = label;
        this.tags = tags;
        this.hex = hex;
        this.resourceId = resourceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }
}
