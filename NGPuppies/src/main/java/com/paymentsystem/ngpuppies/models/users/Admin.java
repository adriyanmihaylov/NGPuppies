package com.paymentsystem.ngpuppies.models.users;

import com.paymentsystem.ngpuppies.validation.anotations.ValidEmail;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    @ValidEmail
    @Column(name = "AdminEmail")
    private String email;

    public Admin() {
    }

    public Admin(String username, String password, String email,Authority authority) {
        super(username, password, authority);
        Date today = new Date();
        super.setLastPasswordResetDate(today);
        super.setEnabled(Boolean.FALSE);
        setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}