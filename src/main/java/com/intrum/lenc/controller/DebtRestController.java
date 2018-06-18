package com.intrum.lenc.controller;

import com.intrum.lenc.dao.UserRepository;
import com.intrum.lenc.model.Debt;
import com.intrum.lenc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/customers/{customerId}/debts")
public class DebtRestController extends AbstractController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Autowired
    DebtService debtService;

    /**
     * Fetch debt by debtId, check userId and customerId
     *
     * @param debtId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{debtId}")
    public Debt get(@PathVariable Long userId, @PathVariable Long customerId, @PathVariable Long debtId
            , HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String principal = getPrincipal(httpServletRequest);
        if (userService.isSame(principal, userId)) {
            Optional<Debt> optional = debtService.findById(customerId, debtId);
            if (optional.isPresent()) {
                return optional.get();
            } else {
                throw new ResourceNotFoundException("Debt not found: " + customerId + " " + debtId);
            }
        } else {
            throw new UnautorizedException();
        }
    }

    /**
     * Create debt for user and customer
     * throws UnautorizedException, if user id not authorized
     * throws ServiceException, if request is wrong
     *
     * @param userId
     * @param customerId
     * @param debt
     * @param httpServletRequest
     * @param response status should be HttpServletResponse.SC_CREATED, if debt created
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping()
    public void post(@PathVariable Long userId, @PathVariable Long customerId, @RequestBody Debt debt
            , HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String principal = getPrincipal(httpServletRequest);
        if (userService.isSame(principal, userId)) {
            debtService.createDebt(userId, customerId, debt);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setHeader("Location", "/users/" + userId + "/customers/" + customerId + "/debts/" + debt.getId());
        } else {
            throw new UnautorizedException();
        }
    }

    /**
     * Update debt for user and customer
     * throws UnautorizedException, if user id not authorized
     * throws ServiceException, if request is wrong
     *
     * @param userId
     * @param customerId
     * @param debt
     * @param httpServletRequest
     * @param response
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping()
    public void put(@PathVariable Long userId, @PathVariable Long customerId, @RequestBody Debt debt
            , HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String principal = getPrincipal(httpServletRequest);
        if (userService.isSame(principal, userId)) {
            debtService.updateDebt(userId, customerId, debt);
        } else {
            throw new UnautorizedException();
        }
    }
}
