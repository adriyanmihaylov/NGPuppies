package com.paymentsystem.ngpuppies.web.dto;

import com.paymentsystem.ngpuppies.validation.anotations.EqualPasswords;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@EqualPasswords
public class PasswordResetDТО {

    @NotEmpty(message = "Please enter password")
    @Size(min = 6, max = 30)
    private String password;

    @NotEmpty(message = "Please confirm your password")
    @Size.List({
            @Size(min = 6, message = "Password must be more than 6 characters"),
            @Size(max = 50, message = "Password must be less than 50 characters")
    })
    private String confirmPassword;

    public PasswordResetDТО() {
    }

    public PasswordResetDТО(String password, String confirmPassword) {
        setPassword(password);
        setConfirmPassword(confirmPassword);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}