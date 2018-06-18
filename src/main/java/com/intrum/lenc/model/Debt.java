package com.intrum.lenc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "debt")
public class Debt {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "dueDate", nullable = false)
    private Date dueDate;

    @NotNull
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @NotNull
    @OneToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties("user")
    private Customer customer;

    @NotNull
    @Column(name = "payed", nullable = false)
    private boolean payed;

    public Debt() {

    }

    public Debt(Date dueDate, BigDecimal amount, Customer customer) {
        this.dueDate = dueDate;
        this.amount = amount;
        this.customer = customer;
    }


    public Debt(Date dueDate, BigDecimal amount, Customer customer, boolean payed) {
        this.dueDate = dueDate;
        this.amount = amount;
        this.customer = customer;
        this.payed = payed;
    }

    public Debt(Long id, Date dueDate, BigDecimal amount, Customer customer, boolean payed) {
        this.id = id;
        this.dueDate = dueDate;
        this.amount = amount;
        this.customer = customer;
        this.payed = payed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debt debt = (Debt) o;
        return payed == debt.payed &&
                Objects.equals(id, debt.id) &&
                Objects.equals(dueDate, debt.dueDate) &&
                Objects.equals(amount, debt.amount) &&
                Objects.equals(customer, debt.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dueDate, amount, customer, payed);
    }
}
