package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;

@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "Type")
    private String currencyType;

    public Currency(){

    }

    public Currency(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
}
