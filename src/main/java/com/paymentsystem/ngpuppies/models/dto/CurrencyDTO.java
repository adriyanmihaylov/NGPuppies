package com.paymentsystem.ngpuppies.models.dto;

import com.paymentsystem.ngpuppies.validation.anotations.ValidMoneyAmount;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class CurrencyDTO {

    @Length(min = 2,max = 10)
    @NotNull(message = "Invalid currency")
    String name;

    @NotNull(message = "Enter fixing")
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
