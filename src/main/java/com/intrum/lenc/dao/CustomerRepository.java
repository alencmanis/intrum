package com.intrum.lenc.dao;

import com.intrum.lenc.model.Customer;
import com.intrum.lenc.model.User;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Customer findByIdAndUser(Long id, User user);
}
