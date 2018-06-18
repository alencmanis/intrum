package com.intrum.lenc.controller;

import com.intrum.lenc.dao.CustomerRepository;
import com.intrum.lenc.model.Customer;
import com.intrum.lenc.model.User;
import com.intrum.lenc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/customers")
public class CustomerRestController extends AbstractController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;

    /**
     * Fetch user by customerId
     *
     * @param customerId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{customerId}")
    public Customer get(@PathVariable Long userId, @PathVariable Long customerId
            , HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String principal = getPrincipal(httpServletRequest);
        if (userService.isSame(principal, userId)) {
            Optional<Customer> customerOptional = customerService.findById(customerId);
            if (customerOptional.isPresent()) {
                return customerOptional.get();
            } else {
                throw new ResourceNotFoundException("Customer not found: " + customerId);
            }
        } else {
            throw new UnautorizedException();
        }
    }

    /**
     * @param userId   userId who creates customer, user property from customer not used
     * @param customer customer json
     * @param response status should be HttpStatus.CREATED, if customer created
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping()
    public void post(@PathVariable Long userId, @RequestBody Customer customer
            , HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String principal = getPrincipal(httpServletRequest);

        if (userService.isSame(principal, userId)) {
            Optional<User> user = userService.findById(userId);
            if (user.isPresent()) {
                customer.setUser(user.get());
                customerService.save(customer);
                response.setStatus(HttpStatus.CREATED.value());
                response.setHeader("Location", "/users/" + userId + "/customers/" + customer.getId());
            } else {
                throw new ServiceException();
            }
        } else {
            throw new UnautorizedException();
        }
    }
}