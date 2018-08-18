package com.paymentsystem.ngpuppies.models;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends ApplicationUser {

    @Column(name = "AdminEmail")
    private String email;

    public Admin() {
    }

    public Admin(String username, String password, String email) {
        super(username, password,"ADMIN");
        setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}