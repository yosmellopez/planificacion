package com.planning.api;

import com.planning.entity.Users;
import com.planning.security.jwt.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * End-point for retrieving logged-in user details.
 *
 * @author vladimir.stankovic
 * <p>
 * Aug 4, 2016
 */
@RestController
public class ProfileEndpoint {
    
    @ResponseBody
    @RequestMapping(value = "/api/me", method = RequestMethod.GET)
    public Users get(JwtAuthenticationToken token) {
        return (Users) token.getPrincipal();
    }
}
