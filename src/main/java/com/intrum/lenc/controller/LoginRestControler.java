package com.intrum.lenc.controller;


import com.intrum.lenc.model.User;
import com.intrum.lenc.security.AuthenticationManager;
import com.intrum.lenc.security.TokenProvider;
import com.intrum.lenc.service.UnautorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginRestControler {
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Make authentication for given user
     *
     * @param user
     * @return
     */
    @PostMapping("/auth")
    public String login(@RequestBody User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getName(), user.getPassword());
        try {
            Authentication token = this.authenticationManager.authenticate(authenticationToken);
            return tokenProvider.createToken(token);
        } catch (AuthenticationException e) {
            throw new UnautorizedException();
        }
    }
}
