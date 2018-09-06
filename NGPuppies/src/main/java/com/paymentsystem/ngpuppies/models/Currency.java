package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;

@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "Name")
    private String name;

    @Column(name = "CurrencyToBGN")
    private double fixing;

    public Currency() {

    }

    public Currency(String name) {
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFixing() {
        return fixing;
    }

    public void setFixing(double fixing) {
        this.fixing = fixing;
    }
}