package com.paymentsystem.ngpuppies.models;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "billing_records")
public class Invoice {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @DateTimeFormat(pattern = "YYYY-MM-DD")
    @Column(name = "StartDate")
    private Date startDate;

    @DateTimeFormat(pattern = "YYYY-MM-DD")
    @Column(name = "EndDate")
    private Date endDate;

    @Column(name = "Amount")
    private double amount;

    @Column(name = "BGNAmount")
    private double BGNAmount;

    @OneToOne
    @JoinColumn(name = "OfferedServiceID")
    private OfferedServices offeredServices;

    @OneToOne
    @JoinColumn(name = "CurrencyID")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "SubscriberID")
    private Subscriber subscriber;

    @DateTimeFormat(pattern = "YYYY-MM-DD")
    @Temporal(TemporalType.DATE)
    @Column(name = "PayedDate")
    private Date payedDate;

    public Invoice() {

    }

    public Invoice(Subscriber subscriber, Date fromDate, Date toDate, double amountBGN, OfferedServices offeredServices) {
        setSubscriber(subscriber);
        setStartDate(fromDate);
        setEndDate(toDate);
        setBGNAmount(amountBGN);
        setOfferedServices(offeredServices);
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

    public OfferedServices getOfferedServices() {
        return offeredServices;
    }

    public void setOfferedServices(OfferedServices offeredServices) {
        this.offeredServices = offeredServices;
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

    public Date getPayedDate() {
        return payedDate;
    }

    public void setPayedDate(Date payedDate) {
        this.payedDate = payedDate;
    }

    public double getBGNAmount() {
        return BGNAmount;
    }

    public void setBGNAmount(double BGNAmount) {
        this.BGNAmount = BGNAmount;
    }
}