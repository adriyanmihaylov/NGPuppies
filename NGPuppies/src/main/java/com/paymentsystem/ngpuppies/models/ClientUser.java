package com.paymentsystem.ngpuppies.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ClientUser extends ApplicationUser {

    @Column(name = "Eik")
    private String eik;

    @OneToOne
    @JoinColumn(name = "DetailsID")
    private ClientDetail details;

    public ClientUser() {

    }

    public ClientUser(String username, String password, String role,String eik) {
        super(username,password,role);
        setEik(eik);
    }

    public ClientUser(String username, String password, String role,String eik,ClientDetail details) {
        this(username,password,role,eik);
        setDetails(details);
    }

    public String getEik() {
        return eik;
    }

    public void setEik(String eik) {
        this.eik = eik;
    }

    public ClientDetail getDetails() {
        return details;
    }

    public void setDetails(ClientDetail details) {
        this.details = details;
    }
}
