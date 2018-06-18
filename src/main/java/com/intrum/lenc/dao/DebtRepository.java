package com.intrum.lenc.dao;

import com.intrum.lenc.model.Customer;
import com.intrum.lenc.model.Debt;
import com.intrum.lenc.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface DebtRepository extends CrudRepository<Debt, Long> {
}
