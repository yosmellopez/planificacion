package com.planning.api;

import com.planning.entity.Users;
import com.planning.security.jwt.Correo;
import com.planning.security.jwt.JwtAuthenticationToken;
import com.planning.service.UsersService;
import com.planning.util.MailMail;
import com.planning.util.RestModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Random;

/**
 * End-point for retrieving logged-in user details.
 *
 * @author vladimir.stankovic
 * <p>
 * Aug 4, 2016
 */
@RestController
public class ProfileEndpoint {
    
    private static final Random random = new Random();
    
    private static char[] symbols;
    
    @Autowired
    private UsersService usuarioService;
    
    @Autowired
    private MailMail mail;
    
    @ResponseBody
    @RequestMapping(value = "/api/me", method = RequestMethod.GET)
    public ResponseEntity<ModelMap> get(JwtAuthenticationToken token, ModelMap map) {
        map.put("usuario", token.getPrincipal());
        map.put("success", true);
        return ResponseEntity.ok(map);
    }
    
    @PostMapping(value = "/apirest/auth/restore")
    public ModelAndView createUsers(@RequestBody Correo correo, ModelMap map) {
        ShaPasswordEncoder encoder = new ShaPasswordEncoder(1);
        Users usuario = usuarioService.findByUsuarioOrEmail(correo.getCorreo(), correo.getCorreo());
        if (usuario != null) {
            String password = prepareRandomString(10);
            String passwordCodificada = encoder.encodePassword(password, null);
            usuario.setKeypass(passwordCodificada);
            mail.setTo(correo.getCorreo());
            mail.sendMail("Su nueva contraseña es: " + password);
            usuarioService.save(usuario);
            map.put("success", true);
            map.put("message", "Contraseña enviada por correo.");
            return RestModelAndView.ok(map);
        } else {
            map.put("success", false);
            map.put("error", "Este correo no existe.");
            return RestModelAndView.ok(map);
        }
    }
    
    public String prepareRandomString(int len) {
        char[] buf = new char[len];
        for (int idx = 0; idx < buf.length; ++idx) {
            buf[idx] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buf);
    }
    
    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }
}
