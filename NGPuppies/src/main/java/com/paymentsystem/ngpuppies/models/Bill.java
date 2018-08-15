package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "bills")
public class Bill {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "ServiceID")
    private OfferedService offeredService;

    @OneToOne
    @JoinColumn(name = "SubscriberID")
    private Subscriber subscriber;

    @Column(name = "StartDate")
    private Date startDate;

    @Column(name = "EndDate")
    private Date dueDate;

    @OneToOne
    @JoinColumn(name = "CurrencyID")
    private Currency currency;

    public Bill() {

    }

    public Bill(OfferedService offeredService,
                Subscriber subscriber,
                Date startDate, Date dueDate, Currency currency) {
        this.offeredService = offeredService;
        this.subscriber = subscriber;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OfferedService getOfferedService() {
        return offeredService;
    }

    public void setOfferedService(OfferedService offeredService) {
        this.offeredService = offeredService;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}