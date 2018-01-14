package com.planning.security;

import com.planning.util.MapeadorObjetos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.ui.ModelMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.Cookie;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

public class AutenticacionAjaxExitosa extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private MapeadorObjetos mapeadorObjetos;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ModelMap map = new ModelMap();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Accept", "application/json");
        Cookie cookie = new Cookie("aguas", "" + new Date().getTime());
        response.addCookie(cookie);
        DefaultSavedRequest savedRequest = (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        if (savedRequest != null) {
            String requestURL = savedRequest.getRequestURL();
            map.put("success", true);
            map.put("usuario", authentication.getName());
            map.put("url", requestURL);
        } else {
            map.put("success", true);
            map.put("usuario", authentication.getName());
            map.put("url", "inicio.html");
        }
        response.getWriter().print(mapeadorObjetos.writeValueAsString(map));
        response.getWriter().flush();
    }
}
