package com.intrum.lenc.controller;

import com.intrum.lenc.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginRestControlerTest extends ControllerTest {
    @Test
    public void wrongLogin() throws Exception {
        User user = new User("wrong", "wrong");

        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void userLogin() throws Exception {
        User user = new User("user", "password");
        userService.save(user);

        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
