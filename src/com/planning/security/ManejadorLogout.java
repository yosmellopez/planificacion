package com.planning.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class ManejadorLogout extends SimpleUrlLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cooky : cookies) {
            cooky.setMaxAge(0);
            response.addCookie(cooky);
        }
        setDefaultTargetUrl("/login.html");
        super.onLogoutSuccess(request, response, authentication);
    }
}
