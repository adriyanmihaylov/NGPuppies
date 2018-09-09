package com.paymentsystem.ngpuppies.web.dto;

import com.paymentsystem.ngpuppies.validation.anotations.ValidServiceName;


public class TelecomServiceDTO {

    @ValidServiceName
    private String name;

    public TelecomServiceDTO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}