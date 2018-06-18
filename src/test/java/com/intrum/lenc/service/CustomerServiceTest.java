package com.intrum.lenc.service;

import com.intrum.lenc.dao.CustomerRepository;
import com.intrum.lenc.model.Customer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {
    @InjectMocks
    private CustomerService customerService;
    @Mock
    CustomerRepository customerRepository;
    @Test
    public void saveEmptyUser() {
        try {
            customerService.save(null);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().startsWith(CustomerService.EMPTY_CUSTOMER));
        }
    }

    @Test
    public void saveWrongId() {
        try {
            Customer user = new Customer(-6L, "firstName", "lastName", "email", "44", new Date());
            customerService.save(user);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().startsWith(CustomerService.WRONG_ID));
        }
    }
}
