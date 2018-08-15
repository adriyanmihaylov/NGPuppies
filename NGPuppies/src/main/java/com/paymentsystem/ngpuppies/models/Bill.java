package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToOne
    @JoinColumn(name= "ServiceID")
    private Service service;
    @OneToOne
    @JoinColumn(name= "SubscriberID")
    private Subscriber subscriber;
    @Column(name="StartDate")
    private Date startDate;
    @Column(name = "EndDate")
    private Date dueDate;
    @OneToOne
    @JoinColumn(name= "CurrencyID")
    private Currency currency;

    public Bill(){

    }

    public Bill(Service service,
                Subscriber subscriber,
                Date startDate, Date dueDate, Currency currency) {
        this.service = service;
        this.subscriber = subscriber;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.currency = currency;
    }
}
