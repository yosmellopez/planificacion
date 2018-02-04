package com.planning.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PlTaskUtil {

    private String name;

    private int posx;

    private int posy;

    private int width;

    private int height;

    private boolean isrecurrent;

    private Task task;

    private StatusTask statusTask;

    private Set<CriticalyLevel> criticalyLevels = new HashSet<>();

    private Position position;

    private Set<Channel> channels;

    private String cargo = "";

    private String gerencia;

    private String criticidad = "";

    private boolean partida;

    public PlTaskUtil() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosx() {
        return posx;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }

    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean getIsrecurrent() {
        return isrecurrent;
    }

    public void setIsrecurrent(boolean isrecurrent) {
        this.isrecurrent = isrecurrent;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public StatusTask getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    public boolean isPartida() {
        return partida;
    }

    public void setPartida(boolean partida) {
        this.partida = partida;
    }

    public boolean isIsrecurrent() {
        return isrecurrent;
    }

    public Set<Channel> getChannels() {
        return channels;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }

    public Set<CriticalyLevel> getCriticalyLevels() {
        return criticalyLevels;
    }

    public void setCriticalyLevels(Set<CriticalyLevel> criticalyLevels) {
        this.criticalyLevels = criticalyLevels;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getGerencia() {
        gerencia = position != null ? position.getArea().getManagement().getName() : "";
        return gerencia;
    }

    public void setGerencia(String gerencia) {
        gerencia = position != null ? position.getArea().getManagement().getName() : "";
        this.gerencia = gerencia;
    }

    public String getCriticidad() {
        int i = 0;
        for (CriticalyLevel criticalyLevel : criticalyLevels) {
            criticidad += (i == 0 ? "" : ", ") + criticalyLevel.getName();
            i++;
        }
        return criticidad;
    }

    public void setCriticidad(String criticidad) {
        int i = 0;
        for (CriticalyLevel criticalyLevel : criticalyLevels) {
            criticidad += (i == 0 ? "" : ", ") + criticalyLevel.getName();
            i++;
        }
        this.criticidad = criticidad;
    }
}
