package com.paymentsystem.ngpuppies.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InvoicePayDTO {

    @NotNull
    private Integer id;

    @NotNull
    @Size(min = 2,max = 5)
    private String currency;

    public InvoicePayDTO() {

    }

    public InvoicePayDTO(Integer id, String currency) {
        setId(id);
        setCurrency(currency);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}