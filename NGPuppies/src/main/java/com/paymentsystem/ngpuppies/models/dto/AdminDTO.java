package com.paymentsystem.ngpuppies.models.dto;

import com.paymentsystem.ngpuppies.validator.base.ValidEmail;
import com.paymentsystem.ngpuppies.validator.base.ValidUsername;

import javax.validation.constraints.Size;

public class AdminDTO {
    @ValidUsername
    @Size.List({
            @Size(min = 5, message = "Username must be at least 5 characters"),
            @Size(max = 50, message = "Username must be less than 50 characters")
    })
    private String username;

//    @NotNull(message = "Password can not be empty!")
    @Size.List({
            @Size(min = 6, message = "Password must be at least 6 characters"),
            @Size(max = 100, message = "Password must be less than 100 characters")
    })
    private String password;

    @ValidEmail
    private String email;

    public AdminDTO() {

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