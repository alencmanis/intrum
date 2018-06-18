package com.intrum.lenc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intrum.lenc.security.JWTFilter;
import com.intrum.lenc.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public abstract class AbstractController {
    @Autowired
    protected TokenProvider tokenProvider;
    @Autowired
    protected ObjectMapper mapper;

    /**
     * Fetch principal username from httpServletRequest
     *
     * @param httpServletRequest
     * @return logged user username
     */
    public String getPrincipal(HttpServletRequest httpServletRequest) {
        String token = JWTFilter.resolveToken(httpServletRequest);
        Authentication authentication = tokenProvider.getAuthentication(token);
        String principal = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        return principal;
    }
}
