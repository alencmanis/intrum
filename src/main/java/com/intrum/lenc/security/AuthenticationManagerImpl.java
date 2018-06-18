package com.intrum.lenc.security;

import com.intrum.lenc.dao.UserRepository;
import com.intrum.lenc.model.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationManagerImpl implements AuthenticationManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationManagerImpl.class);

    @Autowired
    UserRepository userRepository;

    /**
     * Authenticate user by username and password
     * @param authentication
     * @return Authentication with roles
     * @throws AuthenticationException
     */
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = null;
        try {
            //use a rest service to find the user.
            //Spring security provides user login name in authentication.getPrincipal()
            user = userRepository.findByName(authentication.getPrincipal().toString());
        } catch (Exception e) {
            LOGGER.error("Error loading user, not found: " + e.getMessage(), e);
        }

        if (user == null) {
            throw new UsernameNotFoundException(String.format("Invalid credentials", authentication.getPrincipal()));
        }
        //check user password stored in authentication.getCredentials()
        if (StringUtils.isBlank(authentication.getCredentials().toString())
                || !user.getPassword().equals(authentication.getCredentials().toString())
                ) {
            throw new BadCredentialsException("Invalid credentials");
        }

        //doLogin makes whatever is necesary when login is made (put info in session, load other data etc..)
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword(), grantedAuths);
    }

}
