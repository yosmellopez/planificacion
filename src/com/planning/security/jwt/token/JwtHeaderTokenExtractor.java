package com.planning.security.jwt.token;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

/**
 * An implementation of {@link TokenExtractor} extracts token from
 * Authorization: Bearer scheme.
 *
 * @author vladimir.stankovic
 * <p>
 * Aug 5, 2016
 */
@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {
    
    @Override
    public String extract(String header) {
        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("No está autorizado a acceder a este recurso!");
        }
        return header;
    }
}
