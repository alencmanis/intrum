package com.intrum.lenc.controller;

import com.intrum.lenc.model.User;
import com.intrum.lenc.security.JWTFilter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRestControllerTest extends ControllerTest {
    @Test
    public void testGet() throws Exception {
        User user = new User("user1", "password");
        userService.save(user);

        String token = auth(user);

        String resultJson = mockMvc.perform(get("/users/" + user.getId())
                .header(JWTFilter.AUTHORIZATION_HEADER, JWTFilter.BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        User result = mapper.readValue(resultJson, User.class);
        Assert.assertEquals(user, result);
    }

    @Test
    public void testPost() throws Exception {
        User user = new User("user", "admin");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(HttpStatus.CREATED.CREATED.value()))
                .andExpect(header().string("location", containsString("/users/")));
    }
}