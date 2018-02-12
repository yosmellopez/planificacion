package com.planning.security;

import com.planning.config.SecurityConfig;
import com.planning.entity.Users;
import com.planning.security.jwt.JwtSettings;
import com.planning.security.jwt.token.JwtToken;
import com.planning.security.jwt.token.JwtTokenFactory;
import com.planning.service.UserTokenService;
import com.planning.service.UsersService;
import com.planning.util.MapeadorObjetos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class AutenticacionAjaxExitosa implements AuthenticationSuccessHandler {
    
    @Autowired
    private MapeadorObjetos mapeadorObjetos;
    
    @Autowired
    private JwtTokenFactory tokenFactory;
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private UserTokenService tokenService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ModelMap map = new ModelMap();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setHeader("Accept", "application/json");
        Users usuario = (Users) authentication.getPrincipal();
        if (request.getRequestURI().contains(SecurityConfig.AUTHENTICATION_URL)) {
            JwtToken accessToken = tokenFactory.createAccessJwtToken(usuario);
//            JwtToken refreshToken = tokenFactory.createRefreshToken(usuario);
            response.addHeader(JwtSettings.AUTHORIZATION_HEADER, accessToken.getToken());
            map.put("token", accessToken.getToken());
//            map.put("refreshToken", "Bearer " + refreshToken.getToken());
            map.put("user", usuario);
            map.put("success", true);
        } else {
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
        }
        response.getWriter().print(mapeadorObjetos.writeValueAsString(map));
        response.getWriter().flush();
    }
    
    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     * @return 
     */
    public UsersService getUsersService() {
        return usersService;
    }
    
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }
    
    public UserTokenService getTokenService() {
        return tokenService;
    }
    
    public void setTokenService(UserTokenService tokenService) {
        this.tokenService = tokenService;
    }
}
