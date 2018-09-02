package com.paymentsystem.ngpuppies.models.datatransferobjects;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.validator.base.ValidEgn;
import com.paymentsystem.ngpuppies.validator.base.ValidName;
import com.paymentsystem.ngpuppies.validator.base.ValidPhone;

import javax.validation.constraints.Size;

public class SubscriberDto {
    @ValidPhone
    private String phone;

    @ValidName
    @Size.List({
            @Size(min = 3, message = "First name must be at least 3 characters"),
            @Size(max = 50, message = "First name must be less than 50 characters")
    })
    private String firstName;

    @ValidName
    @Size.List({
            @Size(min = 3, message = "Last name must be at least 3 characters"),
            @Size(max = 50, message = "Last name must be less than 50 characters")
    })
    private String lastName;

    @ValidEgn
    private String egn;

    private Address address;

    public SubscriberDto() {

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
}
