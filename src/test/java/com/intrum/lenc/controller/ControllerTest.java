package com.intrum.lenc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intrum.lenc.config.AppConfig;
import com.intrum.lenc.config.SecurityConfig;
import com.intrum.lenc.model.User;
import com.intrum.lenc.service.CustomerService;
import com.intrum.lenc.service.DebtService;
import com.intrum.lenc.service.UserService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, SecurityConfig.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test.properties")
public abstract class ControllerTest {
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    protected WebApplicationContext context;
    @Autowired
    protected UserService userService;
    @Autowired
    protected CustomerService customerService;
    @Autowired
    protected DebtService debtService;

    protected MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        cleanDb();
    }

    public void cleanDb() {
        debtService.deleteAll();
        customerService.deleteAll();
        userService.deleteAll();
    }

    /**
     * @param user
     * @return token by given User
     * @throws Exception
     */
    protected String auth(User user) throws Exception {
        String json = mapper.writeValueAsString(user);
        String string = mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        return string;
    }
}
