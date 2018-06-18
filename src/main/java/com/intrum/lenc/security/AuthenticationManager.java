package com.intrum.lenc.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public interface AuthenticationManager {
    /**
     * Authenticate user by username and password
     * @param authentication
     * @return Authentication with roles
     * @throws AuthenticationException
     */
    Authentication authenticate(Authentication authentication) throws AuthenticationException;
}
