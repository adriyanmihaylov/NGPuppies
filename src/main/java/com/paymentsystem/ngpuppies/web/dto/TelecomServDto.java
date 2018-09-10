package com.paymentsystem.ngpuppies.web.dto;

import com.paymentsystem.ngpuppies.validation.anotations.ValidServiceName;


public class TelecomServDto {
    @ValidServiceName
    private String name;

    public TelecomServDto() {

    }
    public TelecomServDto(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}