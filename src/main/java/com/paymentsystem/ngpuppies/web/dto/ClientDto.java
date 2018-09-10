package com.paymentsystem.ngpuppies.web.dto;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.validation.anotations.ValidEik;
import com.paymentsystem.ngpuppies.validation.anotations.ValidUsername;

import javax.validation.constraints.Size;

public class ClientDto {
    @ValidUsername
    @Size.List({
            @Size(min = 6, message = "Username must be at least 6 characters"),
            @Size(max = 20, message = "Username must be less than 20 characters")
    })
    private String username;

    @Size.List({
            @Size(min = 6, message = "Password must be at least 6 characters"),
            @Size(max = 30, message = "Password must be less than 30 characters")
    })
    private String password;

    @ValidEik
    private String eik;

    private ClientDetail details;

    public ClientDto() {
    }

    public ClientDto(String username, String password, String eik) {
        setUsername(username);
        setPassword(password);
        setEik(eik);
    }

    public ClientDto(String username, String password, String eik,ClientDetail clientDetail) {
        setUsername(username);
        setPassword(password);
        setEik(eik);
        setDetails(clientDetail);
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