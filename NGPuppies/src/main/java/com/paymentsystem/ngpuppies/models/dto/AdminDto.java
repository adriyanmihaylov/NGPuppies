package com.paymentsystem.ngpuppies.models.dto;

import com.paymentsystem.ngpuppies.validator.ValidEmail;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AdminDto {
    @NotNull
    @NotEmpty
    @Length(min = 4, max = 50)
    private String username;

    @NotNull
    @NotEmpty
    @Length(min = 6, max = 100)
    private String password;

    @ValidEmail
    @NotNull
    @NotEmpty
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