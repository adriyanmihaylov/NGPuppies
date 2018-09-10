package com.paymentsystem.ngpuppies.models;

import com.paymentsystem.ngpuppies.validation.anotations.ValidName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @ValidName(message = "Invalid currency name")
    @Column(name = "Name")
    private String name;

    @NotNull
    @Column(name = "FixingBGN")
    private double fixing;

    public Currency() {

    }

    public Currency(String currencyName, double fixing) {
        setName(currencyName);
        setFixing(fixing);
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