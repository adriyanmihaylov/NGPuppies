package com.paymentsystem.ngpuppies.web.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class CurrencyDTO {

    @Length(min = 2,max = 10)
    @NotNull(message = "Invalid currency")
    String name;

    double fixing;

    public CurrencyDTO() {

    }
    public CurrencyDTO(String name, double fixing) {
        setName(name);
        setFixing(fixing);
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
