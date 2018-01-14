package com.planning.security;


import com.planning.util.MapeadorObjetos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.ui.ModelMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticacionAjaxFallida extends SimpleUrlAuthenticationFailureHandler {
    
    @Autowired
    private MapeadorObjetos mapeadorObjetos;
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ModelMap map = new ModelMap();
        map.put("success", false);
        map.put("error", exception.getLocalizedMessage());
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Accept", "application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(mapeadorObjetos.writeValueAsString(map));
        response.getWriter().flush();
    }
}