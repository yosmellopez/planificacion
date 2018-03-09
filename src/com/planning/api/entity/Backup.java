package com.planning.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Backup {
    
    @JsonProperty(value = "backup_id")
    private Integer backupId;
    
    public Backup() {
    }
    
    public Integer getBackupId() {
        return backupId;
    }
    
    public void setBackupId(Integer backupId) {
        this.backupId = backupId;
    }
}
