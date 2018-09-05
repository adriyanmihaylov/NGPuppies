package com.paymentsystem.ngpuppies.security;

import com.paymentsystem.ngpuppies.validator.base.ValidUsername;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class  JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

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
    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
