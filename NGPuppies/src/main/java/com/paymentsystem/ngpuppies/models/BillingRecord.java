package com.paymentsystem.ngpuppies.models;

import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "billing_records")
public class BillingRecord {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "StartDate")
    private Date startDate;

    @Column(name = "EndDate")
    private Date endDate;

    @Column(name = "Amount")
    private double amount;

    @OneToOne
    @JoinColumn(name = "OfferedServiceID")
    private OfferedService offeredService;

    @OneToOne
    @JoinColumn(name = "CurrencyID")
    private Currency currency;

    @OneToOne
    @JoinColumn(name = "SubscriberID")
    private Subscriber subscriber;

    public BillingRecord() {

    }

    public BillingRecord(Date startDate, Date endDate, double amount,
                         OfferedService offeredService, Currency currency, Subscriber subscriber) {
        setStartDate(startDate);
        setEndDate(endDate);
        setAmount(amount);
        setOfferedService(offeredService);
        setCurrency(currency);
        setSubscriber(subscriber);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OfferedService getOfferedService() {
        return offeredService;
    }

    public void setOfferedService(OfferedService offeredService) {
        this.offeredService = offeredService;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }
}