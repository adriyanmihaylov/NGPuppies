package com.paymentsystem.ngpuppies.web.dto;

import com.paymentsystem.ngpuppies.validation.anotations.ValidMoneyAmount;
import com.paymentsystem.ngpuppies.validation.anotations.ValidDate;
import com.paymentsystem.ngpuppies.validation.anotations.ValidPhone;
import com.paymentsystem.ngpuppies.validation.anotations.ValidServiceName;

public class InvoiceDTO {

    @ValidPhone()
    private String subscriberPhone;

    @ValidDate
    private String startDate;

    @ValidDate
    private String endDate;

    @ValidMoneyAmount
    private String amountBGN;

    @ValidServiceName
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