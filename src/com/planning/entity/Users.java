/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.planning.util.SerializadorBackups;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nodo
 */
@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Users implements Serializable, UserDetails, Comparable<Users> {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1, initialValue = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @JsonProperty(value = "usuario_id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "username", nullable = false, length = 50)
    private String usuario = "usuario";
    
    @Basic(optional = false)
    @JsonProperty(value = "apellidos")
    @Column(name = "lastname", nullable = false, length = 50)
    private String lastname;
    
    @Basic(optional = false)
    @Column(name = "rut", nullable = false, length = 10)
    private String rut = "123";
    
    @Column(name = "phone", length = 128)
    private String phone = "12312323";
    
    @Basic(optional = false)
    @Column(name = "cellphone", nullable = false, length = 128)
    private String cellphone = "12312323";
    
    @Basic(optional = false)
    @Column(name = "email", nullable = false, length = 128)
    private String email;
    
    @Basic(optional = false)
    @JsonProperty(value = "nombre")
    @Column(name = "name", nullable = false, length = 128)
    private String name;
    
    @Basic(optional = false)
    @JsonProperty(value = "password")
    @Column(name = "keypass", nullable = false, length = 128)
    private String keypass = "123";
    
    @JoinColumn(name = "positionid", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_position"))
    @ManyToOne(optional = false)
    @JsonProperty(value = "cargo")
    private Position position;
    
    @JoinColumn(name = "idrol", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_rol"))
    @ManyToOne(optional = false)
    private Rol rol;
    
    @Column(name = "active")
    @ColumnDefault(value = "1")
    private boolean active = true;
    
    @Column(name = "titular")
    @ColumnDefault(value = "0")
    private boolean titular = true;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = SerializadorBackups.class)
    @JoinTable(name = "user_backup", joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "fk_usuario_backup")),
            inverseJoinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "fk_backup_usuario")))
    private Set<Users> backups = new HashSet<>();
    
    public Users() {
    }
    
    public Users(Integer id) {
        this.id = id;
    }
    
    public Users(String id) {
        this.id = Integer.parseInt(id);
    }
    
    public Users(Integer id, String username, String lastname, String rut, String cellphone, String email, String name) {
        this.id = id;
        this.usuario = username;
        this.lastname = lastname;
        this.rut = rut;
        this.cellphone = cellphone;
        this.email = email;
        this.name = name;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public String getRut() {
        return rut;
    }
    
    public void setRut(String rut) {
        this.rut = rut;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getCellphone() {
        return cellphone;
    }
    
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getKeypass() {
        return keypass;
    }
    
    public void setKeypass(String keypass) {
        this.keypass = keypass;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public Rol getRol() {
        return rol;
    }
    
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    public Set<Users> getBackups() {
        return backups;
    }
    
    public void setBackups(Set<Users> backups) {
        this.backups = backups;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    
    @Override
    public String toString() {
        return "com.entity.entity.Users[ id=" + id + " ]";
    }
    
    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<Rol> roles = new ArrayList<>();
        roles.add(rol);
        return roles;
    }
    
    public boolean isTitular() {
        return titular;
    }
    
    public void setTitular(boolean titular) {
        this.titular = titular;
    }
    
    @Override
    @JsonIgnore
    public String getPassword() {
        return keypass;
    }
    
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return active;
    }
    
    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }
    
    @Override
    public int compareTo(Users o) {
        return name.compareTo(o.getName());
    }
    
    public String getNombreCompleto() {
        return name + " " + lastname;
    }
}
