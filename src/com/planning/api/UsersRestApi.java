/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.api;

import com.planning.api.entity.Backup;
import com.planning.entity.Rol;
import com.planning.entity.Users;
import com.planning.service.UsersService;
import com.planning.util.HeaderUtil;
import com.planning.util.MailMail;
import com.planning.util.ResponseUtil;
import com.planning.util.RestModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.Random;

/**
 * REST controller for managing Users.
 */
@RestController
@RequestMapping("/api")
public class UsersRestApi {
    
    private final Logger log = LoggerFactory.getLogger(UsersRestApi.class);
    
    private static final Random random = new Random();
    
    private static char[] symbols;
    
    private static final String ENTITY_NAME = "Usuario";
    
    @Autowired
    private UsersService usuarioService;
    
    @Autowired
    private MailMail mail;
    
    @GetMapping("/usuario")
    public ResponseEntity<ModelMap> listarUsuarios(@AuthenticationPrincipal Users user, ModelMap map) {
        log.debug("REST request to get all Userss");
        map.put("usuarios", usuarioService.findAll());
        map.put("success", true);
        if (user.isTitular()) {
            map.put("backup_id", user.getBackup() != null ? user.getBackup().getId() : "");
        }
        return ResponseEntity.ok(map);
    }
    
    @GetMapping("/usuario/{id}")
    public ResponseEntity<Users> getChannel(@PathVariable Integer id) {
        log.debug("REST request to get Channel : {}", id);
        Users task = usuarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(task));
    }
    
    @PostMapping("/usuario/backup")
    public ModelAndView usuarioBachup(@RequestBody Backup backup, @AuthenticationPrincipal Users user, ModelMap map) {
        Users usuarioBackup = usuarioService.findOne(backup.getBackupId());
        user.setBackup(usuarioBackup);
        usuarioService.saveAndFlush(user);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente.");
        return RestModelAndView.ok(map);
    }
    
    @PostMapping("/usuario/backup/crear")
    public ModelAndView usuarioBachupCreate(@RequestBody Users backup, @AuthenticationPrincipal Users user, ModelMap map) {
        backup.setRol(new Rol(2));
        usuarioService.saveAndFlush(backup);
        user.setBackup(backup);
        usuarioService.saveAndFlush(user);
        map.put("success", true);
        map.put("message", "La operación se realizó correctamente.");
        return RestModelAndView.ok(map);
    }
    
    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable Integer id) {
        log.debug("REST request to delete Users : {}", id);
        usuarioService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
}
