/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.api;

import com.planning.entity.Users;
import com.planning.service.UsersService;
import com.planning.util.HeaderUtil;
import com.planning.util.MailMail;
import com.planning.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    
    

    //    @PutMapping("/usuario/{userId}")
//    public ResponseEntity<ModelMap> updateUsers(@RequestBody Users usuario, @PathVariable("userId") Users usuarioBd, ModelMap map) {
//        log.debug("REST request to update Users : {}", usuario);
//        if (usuario.getId() == null) {
//            return createUsers(usuario, map);
//        }
//        usuarioService.saveAndFlush(usuario);
//        return ResponseEntity.ok(map);
//    }
    @GetMapping("/usuario")
    public List<Users> listarTareas() {
        log.debug("REST request to get all Userss");
        return usuarioService.findAll();
    }
    
    @GetMapping("/usuario/{id}")
    public ResponseEntity<Users> getChannel(@PathVariable Integer id) {
        log.debug("REST request to get Channel : {}", id);
        Users task = usuarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(task));
    }
    
    @GetMapping("/usuario/count")
    public Long usuarioCount() {
        log.debug("REST request to get all Userss");
        return usuarioService.count();
    }
    
    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable Integer id) {
        log.debug("REST request to delete Users : {}", id);
        usuarioService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
