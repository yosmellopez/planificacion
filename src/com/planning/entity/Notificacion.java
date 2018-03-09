package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.planning.util.SerializadorFechaTiempo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "notification")
public class Notificacion implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_id_seq")
    @SequenceGenerator(name = "notification_id_seq", sequenceName = "notification_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;
    
    @Column(name = "title")
    @JsonProperty(value = "titulo")
    private String title;
    
    @Column(name = "description")
    @JsonProperty(value = "descripcion")
    private String description;
    
    @ManyToOne
    @JsonProperty(value = "cargo")
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_notificacion_position"))
    private Position position;
    
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = SerializadorFechaTiempo.class)
    private Date fecha;
    
    public Notificacion() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String event) {
        this.title = event;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String text) {
        this.description = text;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notificacion)) {
            return false;
        }
        Notificacion that = (Notificacion) o;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
