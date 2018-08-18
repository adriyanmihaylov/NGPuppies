package com.paymentsystem.ngpuppies.models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class AdminUser extends ApplicationUser {

    @Column(name = "email")
    private String email;

    public AdminUser() {

    }

    public AdminUser(String username, String password, String role,String email) {
        super(username, password, role);
        setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}