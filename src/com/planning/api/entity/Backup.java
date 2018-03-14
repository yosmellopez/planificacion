package com.planning.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Backup {
    
    @JsonProperty(value = "backup_id")
    private ArrayList<Integer> backups = new ArrayList<>();
    
    public Backup() {
    }
    
    public ArrayList<Integer> getBackups() {
        return backups;
    }
    
    public void setBackups(ArrayList<Integer> backups) {
        this.backups = backups;
    }
}
