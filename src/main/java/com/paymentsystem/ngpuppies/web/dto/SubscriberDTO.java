package com.paymentsystem.ngpuppies.web.dto;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.validation.anotations.ValidEgn;
import com.paymentsystem.ngpuppies.validation.anotations.ValidName;
import com.paymentsystem.ngpuppies.validation.anotations.ValidPhone;

import javax.validation.constraints.Size;

public class SubscriberDTO {
    @ValidPhone
    private String phone;

    @ValidName
    @Size.List({
            @Size(min = 3, message = "First name must be at least 3 characters"),
            @Size(max = 20, message = "First name must be less than 20 characters")
    })
    private String firstName;

    @ValidName
    @Size.List({
            @Size(min = 3, message = "Last name must be at least 3 characters"),
            @Size(max = 20, message = "Last name must be less than 20 characters")
    })
    private String lastName;

    @ValidEgn
    private String egn;

    private Address address;

    private String client;

    public SubscriberDTO() {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEgn() {
        return egn;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
