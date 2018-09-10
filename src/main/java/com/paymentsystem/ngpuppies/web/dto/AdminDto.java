package com.paymentsystem.ngpuppies.web.dto;

import com.paymentsystem.ngpuppies.validation.anotations.ValidEmail;
import com.paymentsystem.ngpuppies.validation.anotations.ValidUsername;

import javax.validation.constraints.Size;

public class AdminDto {
    @ValidUsername
    @Size.List({
            @Size(min = 4, message = "Username must be at least 4 characters"),
            @Size(max = 20, message = "Username must be less than 20 characters")
    })
    private String username;

    @Size.List({
            @Size(min = 6, message = "Password must be at least 6 characters"),
            @Size(max = 30, message = "Password must be less than 30 characters")
    })
    private String password;

    @ValidEmail
    private String email;

    public AdminDto() {

    }

    public AdminDto(String username, String password, String email) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}