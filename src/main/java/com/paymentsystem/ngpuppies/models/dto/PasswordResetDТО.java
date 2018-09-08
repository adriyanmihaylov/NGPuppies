package com.paymentsystem.ngpuppies.models.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PasswordResetDТО {

    @NotEmpty(message = "Please enter password")
    @Size(min = 6, max = 30)
    private String password;

    @NotEmpty(message = "Please confirm your password")
    @Size(min = 6, max = 30)
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