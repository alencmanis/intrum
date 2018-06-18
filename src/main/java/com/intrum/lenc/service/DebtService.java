package com.intrum.lenc.service;

import com.intrum.lenc.dao.CustomerRepository;
import com.intrum.lenc.dao.DebtRepository;
import com.intrum.lenc.dao.UserRepository;
import com.intrum.lenc.model.Customer;
import com.intrum.lenc.model.Debt;
import com.intrum.lenc.model.User;
import com.intrum.lenc.model.util.DebtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class DebtService {
    public static final String INVALID_DATE = "Invalid due date";
    public static final String CUSTOMER_NOT_FOUND_BY_ID = "Customer not found by id: ";
    public static final String USER_NOT_FOUND_BY_ID = "User not found by id: ";
    public static final String INVALID_DEPT = "Invalid Dept";
    public static final String INVALID_AMOUNT = "Invalid Amount";
    public static final String ALREADY_PAID = "Already paid";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private DebtRepository debtRepository;

    /**
     * Create debt for given user and customer
     *
     * @param userId
     * @param customerId
     * @param debt should be not null
     * @throws ResourceNotFoundException if userId is wrong or customerId is wrong, ServiceException if Debts id invalid
     */
    public void createDebt(Long userId, Long customerId, Debt debt) {
        Optional<User> dbUser = userRepository.findById(userId);
        if (dbUser.isPresent()) {
            Customer customer = customerRepository.findByIdAndUser(customerId, dbUser.get());
            if (customer != null) {
                if (debt != null) {
                    if (DebtUtil.validateDueDate(debt)) {
                        if (DebtUtil.validateAmount(debt)) {
                            debt.setCustomer(customer);
                            debtRepository.save(debt);
                        } else {
                            throw new ServiceException(INVALID_AMOUNT);
                        }
                    } else {
                        throw new ServiceException(INVALID_DATE);
                    }
                } else {
                    throw new ServiceException(INVALID_DEPT);
                }
            } else {
                throw new ResourceNotFoundException(CUSTOMER_NOT_FOUND_BY_ID + customerId);
            }
        } else {
            throw new ResourceNotFoundException(USER_NOT_FOUND_BY_ID + userId);
        }
    }

    /**
     * Update debt for given user and customer
     *
     * @param userId
     * @param customerId
     * @param debt
     * @throws ResourceNotFoundException if userId is wrong or customerId is wrong, ServiceException if Debts id invalid
     */
    public void updateDebt(Long userId, Long customerId, Debt debt) throws ServiceException {
        Optional<User> dbUser = userRepository.findById(userId);
        if (dbUser.isPresent()) {
            Customer customer = customerRepository.findByIdAndUser(customerId, dbUser.get());
            if (customer != null) {
                Optional<Debt> debtDbOptional = debtRepository.findById(debt.getId());
                if (debtDbOptional.isPresent()) {
                    Debt debtDb = debtDbOptional.get();
                    if (DebtUtil.validateDueDate(debt)) {
                        BigDecimal amountDb = debtDb.getAmount();
                        if (DebtUtil.validateAmount(debt) && amountDb.compareTo(debt.getAmount()) <= 0) {
                            if (!debtDb.isPayed()) {
                                debtRepository.save(debt);
                            } else {
                                throw new ServiceException(ALREADY_PAID);
                            }
                        } else {
                            throw new ServiceException(INVALID_AMOUNT);
                        }
                    } else {
                        throw new ServiceException(INVALID_DATE);
                    }
                } else {
                    throw new ResourceNotFoundException(INVALID_DEPT);
                }
            } else {
                throw new ResourceNotFoundException(CUSTOMER_NOT_FOUND_BY_ID + customerId);
            }
        } else {
            throw new ResourceNotFoundException(USER_NOT_FOUND_BY_ID + userId);
        }
    }

    /**
     * Delete all debts
     */
    public void deleteAll() {
        debtRepository.deleteAll();
    }

    public Optional<Debt> findById(Long customerId, Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            return debtRepository.findById(id);
        } else {
            throw new ResourceNotFoundException(CUSTOMER_NOT_FOUND_BY_ID + customerId);
        }
    }
}
