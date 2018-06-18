package com.intrum.lenc.controller;

import com.intrum.lenc.model.Customer;
import com.intrum.lenc.model.User;
import com.intrum.lenc.security.JWTFilter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.Iterator;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerRestControllerTest extends ControllerTest {
    @Test
    public void testGet() throws Exception {
        User user = new User("user1", "password");
        userService.save(user);

        Customer customer = new Customer("firstName", "lastName", "email", "22", new Date(), user);
        customerService.save(customer);

        String token = auth(user);

        String resultJson = mockMvc.perform(get("/users/" + user.getId() + "/customers/" + customer.getId())
                .header(JWTFilter.AUTHORIZATION_HEADER, JWTFilter.BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        customer = mapper.readValue(mapper.writeValueAsString(customer), Customer.class); // remove user
        Customer result = mapper.readValue(resultJson, Customer.class);
        Assert.assertEquals(customer, result);
    }

    @Test
    public void testGetWrongCustomerId() throws Exception {
        User user = new User("user1", "password");
        userService.save(user);

        Customer customer = new Customer("firstName", "lastName", "email", "22", new Date(), user);
        customerService.save(customer);

        String token = auth(user);

        mockMvc.perform(get("/users/" + user.getId() + "/customers/" + 3333)
                .header(JWTFilter.AUTHORIZATION_HEADER, JWTFilter.BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is((HttpStatus.NOT_FOUND.value())));
    }

    @Test
    public void testPost() throws Exception {
        User user = new User("user1", "password");
        userService.save(user);

        Customer customer = new Customer("firstName", "lastName", "email", "22", new Date(), user);

        String token = auth(user);

        String json = mapper.writeValueAsString(customer);
        mockMvc.perform(post("/users/" + user.getId() + "/customers")
                .header(JWTFilter.AUTHORIZATION_HEADER, JWTFilter.BEARER + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(header().string("location", containsString("/users/" + user.getId() + "/customers/")));
        ;

        Iterable<Customer> list = customerService.findAll();
        Iterator<Customer> i = list.iterator();
        Assert.assertTrue("Customer not created", i.hasNext());
        while (i.hasNext()) {
            Customer c = i.next();
            c.setId(customer.getId()); // customer from DB has Id
            Assert.assertTrue(customer.equals(c));
        }
    }
}
