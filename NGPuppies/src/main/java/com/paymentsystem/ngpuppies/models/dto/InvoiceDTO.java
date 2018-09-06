package com.paymentsystem.ngpuppies.models.dto;

import com.paymentsystem.ngpuppies.validator.base.ValidMoneyAmount;
import com.paymentsystem.ngpuppies.validator.base.ValidDate;
import com.paymentsystem.ngpuppies.validator.base.ValidPhone;

import javax.validation.constraints.NotNull;

public class InvoiceDTO {

    @NotNull
    @ValidPhone
    private String subscriberPhone;

    @NotNull
    @ValidDate
    private String startDate;

    @NotNull
    @ValidDate
    private String endDate;

    @NotNull
    @ValidMoneyAmount
    private String amountBGN;

    @NotNull
    private String service;

    public InvoiceDTO() {

    }

    public InvoiceDTO(String subscriberPhone,String startDate,String endDate,String amountBGN,String service) {
        setSubscriberPhone(subscriberPhone);
        setStartDate(startDate);
        setEndDate(endDate);
        setAmountBGN(amountBGN);
        setService(service);
    }

    public String getSubscriberPhone() {
        return subscriberPhone;
    }

    public void setSubscriberPhone(String subscriberPhone) {
        this.subscriberPhone = subscriberPhone;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAmountBGN() {
        return amountBGN;
    }

    public void setAmountBGN(String amountBGN) {
        this.amountBGN = amountBGN;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}