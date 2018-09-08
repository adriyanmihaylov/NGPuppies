package com.paymentsystem.ngpuppies.models.dto;

import javax.validation.constraints.NotNull;

public class CurrencyDTO {

    @NotNull(message = "Invalid currency")
    String name;

    Double fixing;

    public CurrencyDTO() {

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
