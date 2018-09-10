package com.paymentsystem.ngpuppies.models;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "billing_records")
public class Invoice {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Column(name = "Amount")
    private double amount;

    @Column(name = "BGNAmount")
    private double BGNAmount;

    @OneToOne
    @JoinColumn(name = "OfferedServiceID")
    private TelecomServ telecomServ;

    @OneToOne
    @JoinColumn(name = "CurrencyID")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "SubscriberID")
    private Subscriber subscriber;

    @Column(name = "PayedDate")
    private LocalDate payedDate;

    public Invoice() {

    }

    public Invoice(Subscriber subscriber, LocalDate fromDate, LocalDate toDate, double amountBGN, TelecomServ telecomServ) {
        setSubscriber(subscriber);
        setStartDate(fromDate);
        setEndDate(toDate);
        setBGNAmount(amountBGN);
        setTelecomServ(telecomServ);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TelecomServ getTelecomServ() {
        return telecomServ;
    }

    public void setTelecomServ(TelecomServ telecomServ) {
        this.telecomServ = telecomServ;
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

    public LocalDate getPayedDate() {
        return payedDate;
    }

    public void setPayedDate(LocalDate payedDate) {
        this.payedDate = payedDate;
    }

    public double getBGNAmount() {
        return BGNAmount;
    }

    public void setBGNAmount(double BGNAmount) {
        this.BGNAmount = BGNAmount;
    }
}