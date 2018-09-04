package com.paymentsystem.ngpuppies.models.datatransferobjects;

import com.paymentsystem.ngpuppies.validator.base.ValidMoneyAmount;
import com.paymentsystem.ngpuppies.validator.base.ValidDate;
import com.paymentsystem.ngpuppies.validator.base.ValidPhone;

import java.sql.Date;

public class BillingRecordDto {

    @ValidPhone
    private String subscriberPhone;

    @ValidDate
    private String startDate;

    @ValidDate
    private String endDate;

    @ValidMoneyAmount
    private String amount;

    private String service;

    private String currency;

    public BillingRecordDto() {

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}