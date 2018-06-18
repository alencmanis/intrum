package com.intrum.lenc.service;

import com.intrum.lenc.dao.CustomerRepository;
import com.intrum.lenc.dao.DebtRepository;
import com.intrum.lenc.dao.UserRepository;
import com.intrum.lenc.model.Customer;
import com.intrum.lenc.model.Debt;
import com.intrum.lenc.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DebtServiceTest {
    @InjectMocks
    private DebtService debtService;
    @Mock
    UserRepository userRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    DebtRepository debtRepository;

    @Test
    public void createDebtWrongUser() throws Exception {
        try {
            Optional<User> user = Optional.empty();
            when(userRepository.findById(Mockito.anyLong())).thenReturn(user);

            debtService.createDebt(25L, 1L, null);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().startsWith(DebtService.USER_NOT_FOUND_BY_ID));
        }
    }

    @Test
    public void createDebtWrongCustomer() throws Exception {
        try {
            Optional<User> user = Optional.of(new User(1L, "name", "pass"));
            when(userRepository.findById(Mockito.anyLong())).thenReturn(user);

            debtService.createDebt(1L, 1L, null);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().startsWith(DebtService.CUSTOMER_NOT_FOUND_BY_ID));
        }
    }

    @Test
    public void createDebtWrongDebtNull() {
        try {
            Optional<User> user = Optional.of(new User(1L, "name", "pass"));
            when(userRepository.findById(Mockito.anyLong())).thenReturn(user);

            Customer customer = new Customer(1L, "firstName", "lastName", "email", "33", new Date());
            when(customerRepository.findByIdAndUser(Mockito.anyLong(), Mockito.any())).thenReturn(customer);

            debtService.createDebt(1L, 1L, null);
            Assert.fail();
        } catch (ServiceException e) {
            Assert.assertTrue(e.getMessage().startsWith(DebtService.INVALID_DEPT));
        }
    }

    @Test
    public void createDebtWrongDebtsDate() throws Exception {
        try {
            Optional<User> user = Optional.of(new User(1L, "name", "pass"));
            when(userRepository.findById(Mockito.anyLong())).thenReturn(user);

            Customer customer = new Customer(1L, "firstName", "lastName", "email", "33", new Date());
            when(customerRepository.findByIdAndUser(Mockito.anyLong(), Mockito.any())).thenReturn(customer);

            Date dueDate = new Date(6000);
            Debt debt = new Debt(dueDate, BigDecimal.valueOf(15.16), customer);
            debtService.createDebt(1L, 1L, debt);
            Assert.fail();
        } catch (ServiceException e) {
            Assert.assertTrue(e.getMessage().startsWith(DebtService.INVALID_DATE));
        }
    }


    @Test
    public void createDebtWrongDebtAmount() throws Exception {
        try {
            Optional<User> user = Optional.of(new User(1L, "name", "pass"));
            when(userRepository.findById(Mockito.anyLong())).thenReturn(user);

            Customer customer = new Customer(1L, "firstName", "lastName", "email", "33", new Date());
            when(customerRepository.findByIdAndUser(Mockito.anyLong(), Mockito.any())).thenReturn(customer);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, 1);  // number of days to add
            Date debtDate = calendar.getTime();
            Debt debt = new Debt(debtDate, BigDecimal.valueOf(0), customer);
            debtService.createDebt(1L, 1L, debt);
            Assert.fail();
        } catch (ServiceException e) {
            Assert.assertTrue(e.getMessage().startsWith(DebtService.INVALID_AMOUNT));
        }
    }

    @Test
    public void createDebt() throws Exception {
        try {
            Optional<User> user = Optional.of(new User(1L, "name", "pass"));
            when(userRepository.findById(Mockito.anyLong())).thenReturn(user);

            Customer customer = new Customer(1L, "firstName", "lastName", "email", "33", new Date());
            when(customerRepository.findByIdAndUser(Mockito.anyLong(), Mockito.any())).thenReturn(customer);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, 1);  // number of days to add
            Date debtDate = calendar.getTime();
            Debt debt = new Debt(debtDate, BigDecimal.valueOf(15), customer);
            when(debtRepository.save(debt)).thenReturn(debt);

            debtService.createDebt(1L, 1L, debt);
        } catch (ServiceException e) {
            Assert.fail();
        }
    }

    @Test
    public void updateDept() throws Exception {
        Optional<User> user = Optional.of(new User(1L, "name", "pass"));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(user);

        Customer customer = new Customer(1L, "firstName", "lastName", "email", "33", new Date());
        when(customerRepository.findByIdAndUser(Mockito.anyLong(), Mockito.any())).thenReturn(customer);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);  // number of days to add
        Date debtDate = calendar.getTime();
        Debt debt = new Debt(1L, debtDate, BigDecimal.valueOf(15), customer, false);
        when(debtRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(debt));
        when(debtRepository.save(debt)).thenReturn(debt);
        Debt debt2 = new Debt(1L, debtDate, BigDecimal.valueOf(15), customer, false);

        debtService.updateDebt(1L, 1L, debt2);
    }

    @Test
    public void updateDeptSmallAmount() throws Exception {
        try {
            Optional<User> user = Optional.of(new User(1L, "name", "pass"));
            when(userRepository.findById(Mockito.anyLong())).thenReturn(user);

            Customer customer = new Customer(1L, "firstName", "lastName", "email", "33", new Date());
            when(customerRepository.findByIdAndUser(Mockito.anyLong(), Mockito.any())).thenReturn(customer);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, 1);  // number of days to add
            Date debtDate = calendar.getTime();

            Debt debtDb = new Debt(1L, debtDate, BigDecimal.valueOf(16), customer, false);

            Debt debt = new Debt(1L, debtDate, BigDecimal.valueOf(15), customer, true);
            when(debtRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(debtDb));

            debtService.updateDebt(1L, 1L, debt);
            Assert.fail();
        } catch (ServiceException e) {
            Assert.assertTrue(e.getMessage().startsWith(DebtService.INVALID_AMOUNT));
        }
    }

    @Test
    public void updateDeptSecondTime() throws Exception {
        try {
            Optional<User> user = Optional.of(new User(1L, "name", "pass"));
            when(userRepository.findById(Mockito.anyLong())).thenReturn(user);

            Customer customer = new Customer(1L, "firstName", "lastName", "email", "33", new Date());
            when(customerRepository.findByIdAndUser(Mockito.anyLong(), Mockito.any())).thenReturn(customer);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, 1);  // number of days to add
            Date debtDate = calendar.getTime();
            Debt debt = new Debt(1L, debtDate, BigDecimal.valueOf(15), customer, true);
            when(debtRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(debt));

            debtService.updateDebt(1L, 1L, debt);
            Assert.fail();
        } catch (ServiceException e) {
            Assert.assertTrue(e.getMessage().startsWith(DebtService.ALREADY_PAID));
        }
    }

}
