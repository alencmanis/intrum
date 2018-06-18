package com.intrum.lenc.controller;

import com.intrum.lenc.model.Customer;
import com.intrum.lenc.model.Debt;
import com.intrum.lenc.model.User;
import com.intrum.lenc.security.JWTFilter;
import com.intrum.lenc.service.DebtService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DebtRestControllerTest extends ControllerTest {
    @Autowired
    DebtService debtService;

    @Test
    public void testGet() throws Exception {
        User user = new User("user1", "password");
        userService.save(user);

        Customer customer = new Customer("firstName", "lastName", "email", "22", new Date(), user);
        customerService.save(customer);

        Debt debt = new Debt(new Date(9999999999999L), BigDecimal.valueOf(12.12), customer);
        debtService.createDebt(user.getId(), customer.getId(), debt);
        String token = auth(user);

        String url = "/users/" + user.getId() + "/customers/" + customer.getId() + "/debts/" + debt.getId();
        String resultJson = mockMvc.perform(get(url)
                .header(JWTFilter.AUTHORIZATION_HEADER, JWTFilter.BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Debt result = mapper.readValue(resultJson, Debt.class);
        debt = mapper.readValue(mapper.writeValueAsString(debt), Debt.class); // remove user
        Assert.assertEquals(debt, result);
    }

    @Test
    public void testPost() throws Exception {
        User user = new User("user1", "password");
        userService.save(user);

        Customer customer = new Customer("firstName", "lastName", "email", "22", new Date(), user);
        customerService.save(customer);

        Debt debt = new Debt(new Date(9999999999999L), BigDecimal.valueOf(12.12), null);
        String token = auth(user);

        String url = "/users/" + user.getId() + "/customers/" + customer.getId() + "/debts";
        String json = mapper.writeValueAsString(debt);
        mockMvc.perform(post(url)
                .header(JWTFilter.AUTHORIZATION_HEADER, JWTFilter.BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(header().string("location", containsString("/users/" + user.getId() + "/customers/" + customer.getId() + "/debts")));
    }

    @Test
    public void testPostWrongUserId() throws Exception {
        User user = new User("user1", "password");
        userService.save(user);

        Customer customer = new Customer("firstName", "lastName", "email", "22", new Date(), user);
        customerService.save(customer);

        Debt debt = new Debt(new Date(), BigDecimal.valueOf(12.12), null);
        String token = auth(user);

        String url = "/users/" + (user.getId() + 1) + "/customers/" + customer.getId() + "/debts";
        String json = mapper.writeValueAsString(debt);
        mockMvc.perform(post(url)
                .header(JWTFilter.AUTHORIZATION_HEADER, JWTFilter.BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void testPostWrongDueDate() throws Exception {
        User user = new User("user1", "password");
        userService.save(user);

        Customer customer = new Customer("firstName", "lastName", "email", "22", new Date(), user);
        customerService.save(customer);

        Date dueDate = new Date(500);
        Debt debt = new Debt(dueDate, BigDecimal.valueOf(12.12), null);

        String token = auth(user);

        String url = "/users/" + user.getId() + "/customers/" + customer.getId() + "/debts";
        String json = mapper.writeValueAsString(debt);
        mockMvc.perform(post(url)
                .header(JWTFilter.AUTHORIZATION_HEADER, JWTFilter.BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    public void testPut() throws Exception {
        User user = new User("user1", "password");
        userService.save(user);

        Customer customer = new Customer("firstName", "lastName", "email", "22", new Date(), user);
        customerService.save(customer);

        Date dueDate = new Date(9999999999999L);
        Debt debt = new Debt(dueDate, BigDecimal.valueOf(12.12), null, false);

        debtService.createDebt(user.getId(), customer.getId(), debt);

        debt.setPayed(true);

        String token = auth(user);
        String json = mapper.writeValueAsString(debt);
        String url = "/users/" + user.getId() + "/customers/" + customer.getId() + "/debts";
        mockMvc.perform(put(url)
                .header(JWTFilter.AUTHORIZATION_HEADER, JWTFilter.BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
