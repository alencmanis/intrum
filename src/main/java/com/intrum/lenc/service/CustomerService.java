package com.intrum.lenc.service;

import com.intrum.lenc.dao.CustomerRepository;
import com.intrum.lenc.model.Customer;
import com.intrum.lenc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    public static final String EMPTY_CUSTOMER = "Empty customer";
    public static final String WRONG_ID = "Wrong id";
    
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Save customer
     * @param customer
     * @throws ResourceNotFoundException if Id is wrong or customer is null
     */
    public void save(Customer customer) {
        if (customer != null) {
            if (customer.getId() == null || customer.getId() > 0L) {
                customerRepository.save(customer);
            } else {
                throw new ResourceNotFoundException(WRONG_ID);
            }
        } else {
            throw new ResourceNotFoundException(EMPTY_CUSTOMER);
        }
    }

    /**
     * delete all customes
     */
    public void deleteAll() {
        customerRepository.deleteAll();
    }

    /**
     * fetch all customes
     * @return
     */
    public Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }

    /**
     * Find customer by customerId
     * @param customerId
     * @return
     */
    public Optional<Customer> findById(Long customerId) {
        return customerRepository.findById(customerId);
    }

}
