package com.paymentsystem.ngpuppies.models.datatransferobjects;

import com.paymentsystem.ngpuppies.validator.base.ValidEik;
import com.paymentsystem.ngpuppies.validator.base.ValidUsername;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ClientDto {
    @ValidUsername
    @Size.List({
            @Size(min = 5, message = "Username must be at least 5 characters"),
            @Size(max = 50, message = "Username must be less than 50 characters")
    })
    private String username;

    @NotNull(message = "Password can not be empty!")
    @Size.List({
            @Size(min = 6, message = "Password must be at least 6 characters"),
            @Size(max = 100, message = "Password must be less than 100 characters")
    })
    private String password;


    @ValidEik
    private String eik;
}