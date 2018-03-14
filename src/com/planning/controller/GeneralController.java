/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.controller;

import com.planning.entity.Plan;
import com.planning.entity.Users;
import com.planning.service.PlanService;
import com.planning.util.ArregloCreator;
import com.planning.util.MiPlan;
import com.planning.util.RestModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
public class GeneralController {
    
    @Autowired
    private PlanService planService;
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        Optional<Plan> planOptional = planService.findByEjecucion(true);
        ModelMap map = new ModelMap();
        map.put("existePlan", planOptional.isPresent());
        if (planOptional.isPresent()) {
            map.put("plan", new MiPlan(planOptional.get()));
        }
        return new ModelAndView("index", map);
    }
    
    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public ModelAndView login(@AuthenticationPrincipal Users user) {
        if (user == null) {
            return new ModelAndView("login");
        }
        return new ModelAndView(new RedirectView("/iniciado.html", true));
    }
    
    @RequestMapping(value = "/inicio.html", method = RequestMethod.GET)
    public String inicio(@AuthenticationPrincipal Users usuario) {
        return "redirect:/#/" + (usuario.getRol().getId() == 1 ? "dashboard" : "dashboard");
    }
    
    @RequestMapping(value = "/iniciado.html", method = RequestMethod.GET)
    public ModelAndView iniciado() {
        return new ModelAndView(new RedirectView("/planificacion"));
    }
    
    @RequestMapping(value = "/error404.html", method = RequestMethod.GET)
    public ModelAndView error404() {
        return new ModelAndView("error/error404");
    }
    
    @ResponseBody
    @PreAuthorize(value = "isFullyAuthenticated()")
    @RequestMapping(value = "/usuario/devolverUsuarioActual", method = RequestMethod.GET)
    public ModelAndView userActual(@AuthenticationPrincipal Users usuario, ModelMap map) {
        if (usuario != null) {
            map.put("success", true);
            map.put("usuario", ArregloCreator.cargarUsuario(usuario));
        } else {
            map.put("success", false);
            map.put("error", "No ha iniciado sesión");
        }
        return new RestModelAndView(map);
    }
    
    @ResponseBody
    @PreAuthorize(value = "isFullyAuthenticated()")
    @RequestMapping(value = "/authentication/usuario", method = RequestMethod.GET)
    public ModelAndView authenticatedUser(@AuthenticationPrincipal Users usuario, ModelMap map) {
        return userActual(usuario, map);
    }
}
