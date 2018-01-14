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
public class Column {

    private String text;

    private String color;

    private Integer dataField;

    private boolean collapsible;

    private int maxItems;

    public Column(String text, Integer dataField, boolean collapsible, int maxItems, String color) {
        this.text = text;
        this.dataField = dataField;
        this.collapsible = collapsible;
        this.maxItems = maxItems;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getDataField() {
        return dataField;
    }

    public void setDataField(Integer dataField) {
        this.dataField = dataField;
    }

    public boolean isCollapsible() {
        return collapsible;
    }

    public void setCollapsible(boolean collapsible) {
        this.collapsible = collapsible;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
