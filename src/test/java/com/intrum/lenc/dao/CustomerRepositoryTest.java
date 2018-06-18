package com.intrum.lenc.dao;

import com.intrum.lenc.model.Customer;
import com.intrum.lenc.model.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;


public class CustomerRepositoryTest extends RepositoryTest {
    @Test
    public void testFindByIdAndUser() {
        User user = new User("userName", "userPassword");
        userRepository.save(user);

        Customer customer = new Customer("firstName", "lastName", "email", "22", new Date(), user);
        customerRepository.save(customer);

        Customer found = customerRepository.findByIdAndUser(customer.getId(), user);
        Assert.assertEquals(customer.getFirstName(), found.getFirstName());
    }
}
