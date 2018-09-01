package com.paymentsystem.ngpuppies.models.datatransferobjects;

import com.paymentsystem.ngpuppies.validator.base.ValidEik;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ClientDto {
    @NotNull
    @NotEmpty
    @Length(min = 4, max = 50)
    private String username;

    @NotNull
    @NotEmpty
    @Length(min = 6, max = 100)
    private String password;

    @ValidEik
    @NotNull
    @NotEmpty
    private String eik;
}
