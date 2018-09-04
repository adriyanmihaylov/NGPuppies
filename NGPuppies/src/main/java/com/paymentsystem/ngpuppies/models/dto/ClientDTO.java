package com.paymentsystem.ngpuppies.models.dto;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.validator.base.ValidEik;
import com.paymentsystem.ngpuppies.validator.base.ValidUsername;

import javax.validation.constraints.Size;

public class ClientDTO {
    @ValidUsername
    @Size.List({
            @Size(min = 5, message = "Username must be at least 5 characters"),
            @Size(max = 50, message = "Username must be less than 50 characters")
    })
    private String username;

    @Size.List({
            @Size(min = 6, message = "Password must be at least 6 characters"),
            @Size(max = 100, message = "Password must be less than 100 characters")
    })
    private String password;

    @ValidEik
    private String eik;

    private ClientDetail details;

    public ClientDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEik() {
        return eik;
    }

    public void setEik(String eik) {
        this.eik = eik;
    }

    public ClientDetail getDetails() {
        return details;
    }

    public void setDetails(ClientDetail details) {
        this.details = details;
    }
}