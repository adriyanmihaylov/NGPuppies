package com.paymentsystem.ngpuppies.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InvoicePaymentDto {

    @NotNull(message = "Please enter id")
    private Integer id;

    @NotNull(message = "Please enter currency!")
    @Size(min = 2,max = 5)
    private String currency;

    public InvoicePaymentDto() {
    }

    public InvoicePaymentDto(Integer id, String currency) {
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